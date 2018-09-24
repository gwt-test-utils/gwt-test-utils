package com.googlecode.gwt.test.internal;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.*;
import javassist.bytecode.Descriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * A {@link JavaClassModifier} which enable the simulation of GWT-RPC serialization. <strong>For
 * internal use only.</strong>
 * </p>
 * <p>
 * <p>
 * GWT-RPC serialization means translation from JavaScript to Java and to Java back to JavaScript.
 * It's not just like Java Serialization : transient fields are not set to null after the GWT-RPC
 * serialization, they are set to theirs initial value, after the default constructor has been
 * called.
 * </p>
 * <p>
 * <p>
 * To simulate this behaviour, a lot of work is done behind the scene by this class. While it is
 * loading a class, the {@link GwtClassLoader} knows if the class is attempt to be compiled in
 * JavaScript. If it is and if it can be serialized (by implementing {@link IsSerializable} or
 * {@link Serializable} interface), it does a lot of bytecode manipulation (through the internal
 * {@link SerializableModifier} class) :
 * <ul>
 * <li>it replaces the potential {@link IsSerializable} reference to {@link Serializable} to make
 * the class Java-serializable.</li>
 * <li>if the class does not provide a default constructor (which is required by GWT-RPC
 * serializer), it means the class will never be RPC-Serialized. The class is not modified.</li>
 * <li>if the class extends {@link Externalizable} instead of {@link Serializable} : it means the
 * class will never be RPC-Serialized. The class is not modified.</li>
 * <li>Otherwise, it copies the default constructor instructions in a new member method.</li>
 * <li>then, it override the private Object.readObject(ObjectInputStream), a callback which is
 * called during a Java-Serialization : it call the default serialization method and then the new
 * method, so transient field are set with theirs initial value.</li>
 * </ul>
 * After those bytecode modification on a class, its Java-serialization will have the same behavior
 * as the GWT-RPC one.
 * </p>
 * <p>
 * <p>
 * The java serialization is then done by a set of optimized object from the internal API.
 * (com.googlecode.gwt.test.rpc.DeepCopy).
 * </p>
 *
 * @author Gael Lazzari
 */
// TODO: make package-private
public class SerializableModifier implements JavaClassModifier {

    private static final String DEFAULT_CONS_METHOD_NAME = "DEFAULT_CONS_METHOD";
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializableModifier.class);

    public static void readObject(Serializable ex, ObjectInputStream ois) {
        try {
            // call the default read method
            ois.defaultReadObject();

            // keep non transient/static/final value somhere
            Map<Field, Object> buffer = getFieldValues(ex);

            // call the exported default constructor to reinitialise triansient
            // field
            // values
            // which are not expected to be null
            GwtReflectionUtils.callPrivateMethod(ex, DEFAULT_CONS_METHOD_NAME);

            // set the kept field values
            for (Map.Entry<Field, Object> entry : buffer.entrySet()) {
                entry.getKey().set(ex, entry.getValue());
            }

        } catch (Exception e) {
            throw new GwtTestRpcException("Error during deserialization of object "
                    + ex.getClass().getName(), e);
        }

    }

    private static Map<Field, Object> getFieldValues(Serializable o)
            throws IllegalArgumentException, IllegalAccessException {
        Map<Field, Object> result = new HashMap<>();

        for (Field field : GwtReflectionUtils.getFields(o.getClass())) {
            int fieldModifier = field.getModifiers();
            if (!Modifier.isStatic(fieldModifier) && !Modifier.isTransient(fieldModifier)) {
                result.put(field, field.get(o));
            }
        }

        return result;
    }

    private final CtClass charSequenceCtClass;
    private final CtClass externalizableCtClass;
    private final CtClass isSerializableCtClass;
    private final CtClass serializableCtClass;

    public SerializableModifier() {
        serializableCtClass = GwtClassPool.getCtClass(Serializable.class);
        isSerializableCtClass = GwtClassPool.getCtClass(IsSerializable.class);
        externalizableCtClass = GwtClassPool.getCtClass(Externalizable.class);
        charSequenceCtClass = GwtClassPool.getCtClass(CharSequence.class);
    }

    public void modify(CtClass classToModify) throws Exception {

        if (classToModify.isInterface() || classToModify.isPrimitive() || classToModify.isEnum()
                || classToModify.isArray() || classToModify.isAnnotation()
                || Modifier.isAbstract(classToModify.getModifiers())) {
            return;
        }

        if (classToModify.subtypeOf(charSequenceCtClass)) {
            return;
        }

        // Externalizable object which is not serialized by GWT RPC
        if (classToModify.subtypeOf(externalizableCtClass)) {
            return;
        }

        // substitute isSerializable with Serializable
        if (classToModify.subtypeOf(isSerializableCtClass)
                && !classToModify.subtypeOf(serializableCtClass)) {
            CtClass[] interfaces = classToModify.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (isSerializableCtClass.equals(interfaces[i])) {
                    interfaces[i] = serializableCtClass;
                }
            }
            classToModify.setInterfaces(interfaces);
        }

        if (!classToModify.subtypeOf(serializableCtClass)) {
            return;
        }

        if (getReadObjectMethod(classToModify) != null) {
            // this class should never be serialized by GWT RPC
            return;
        }

        // GWT RPC Serializable objects should have an empty constructor
        CtConstructor defaultCons = getDefaultConstructor(classToModify);
        if (defaultCons == null) {
            return;
        }

        LOGGER.debug("Apply serializable bytecode modifier");

        CtMethod defaultConstMethod = defaultCons.toMethod(DEFAULT_CONS_METHOD_NAME, classToModify);
        defaultConstMethod.setModifiers(Modifier.PUBLIC);

        if (hasDefaultConstMethod(classToModify.getSuperclass())) {
            defaultConstMethod.insertBefore("super." + DEFAULT_CONS_METHOD_NAME + "();");
        }
        classToModify.addMethod(defaultConstMethod);

        overrideReadObject(classToModify);
    }

    private String callStaticReadObject() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        buffer.append(this.getClass().getName() + ".readObject(");
        buffer.append("(").append(Serializable.class.getName()).append(")");
        buffer.append(" this, $1);}");

        return buffer.toString();
    }

    private CtConstructor getDefaultConstructor(CtClass ctClass) {
        try {
            return ctClass.getConstructor(Descriptor.ofConstructor(new CtClass[0]));
        } catch (NotFoundException e) {
            return null;
        }
    }

    private CtMethod getReadObjectMethod(CtClass ctClass) {
        CtClass[] paramTypes = new CtClass[]{GwtClassPool.getCtClass(ObjectInputStream.class)};
        try {
            return ctClass.getDeclaredMethod("readObject", paramTypes);
        } catch (NotFoundException e) {
            return null;
        }

    }

    private boolean hasDefaultConstMethod(CtClass ctClass) {
        try {
            CtMethod m = ctClass.getMethod(DEFAULT_CONS_METHOD_NAME,
                    Descriptor.ofMethod(CtClass.voidType, new CtClass[0]));
            return m != null;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private void overrideReadObject(CtClass classToModify) throws NotFoundException,
            CannotCompileException {
        CtClass[] paramTypes = new CtClass[]{GwtClassPool.getCtClass(ObjectInputStream.class)};
        CtMethod readObjectMethod = new CtMethod(CtClass.voidType, "readObject", paramTypes,
                classToModify);
        readObjectMethod.setModifiers(Modifier.PRIVATE);

        // add exception types
        CtClass classNotFoundException = GwtClassPool.getCtClass(ClassNotFoundException.class);
        CtClass ioException = GwtClassPool.getCtClass(IOException.class);
        readObjectMethod.setExceptionTypes(new CtClass[]{classNotFoundException, ioException});

        // set body (call static readObject(Serializable, ObjectInputStream)
        readObjectMethod.setBody(callStaticReadObject());

        classToModify.addMethod(readObjectMethod);
    }
}
