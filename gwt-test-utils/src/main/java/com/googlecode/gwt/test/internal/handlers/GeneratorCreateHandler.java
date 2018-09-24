/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.dev.RebindCache;
import com.google.gwt.dev.cfg.BindingProperty;
import com.google.gwt.dev.cfg.ConfigurationProperty;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.PropertyCombinations;
import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.javac.CompiledClass;
import com.google.gwt.dev.javac.JsniMethod;
import com.google.gwt.dev.shell.*;
import com.google.gwt.dev.util.Name;
import com.google.gwt.junit.client.WithProperties;
import com.google.gwt.junit.client.WithProperties.Property;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtTreeLogger;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.GwtFactory;
import com.googlecode.gwt.test.internal.GwtTestDataHolder;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * This is a wrapper around GWT's compilation tools
 */
public class GeneratorCreateHandler implements GwtCreateHandler {

    private static final ArtifactAcceptor ARTIFACT_ACCEPTOR = new ArtifactAcceptor() {

        public void accept(TreeLogger logger, ArtifactSet newlyGeneratedArtifacts) {

        }
    };

    private static Map<String, ModuleSpaceHost> moduleSpaceHosts = new HashMap<>();

    private static final RebindCache REBIND_CACHE = new RebindCache();

    protected Map<String, CompiledClass> compiledClassMap = new HashMap<>();

    private final CompilationState compilationState;
    private final TreeLogger logger = GwtTreeLogger.get();

    private final ModuleDef moduleDef;

    private PropertyOracle propertyOracle;

    public GeneratorCreateHandler(CompilationState compilationState, ModuleDef moduleDef) {
        this.compilationState = compilationState;
        this.moduleDef = moduleDef;
    }

    public Object create(Class<?> classLiteral) throws Exception {
        Class<?> c = generate(classLiteral);

        if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
            return null;
        }
        return GwtReflectionUtils.instantiateClass(c);
    }

    public Class<?> generate(Class<?> classLiteral) throws UnableToCompleteException {
        CompiledClass compiledClass = compile(classLiteral.getCanonicalName());
        if (compiledClass == null) {
            return classLiteral;
        }

        try {
            return GwtFactory.get().getClassLoader().loadClass(compiledClass.getSourceName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ModuleDef getGwtModule() {
        return moduleDef;
    }

    protected PropertyOracle getPropertyOracle() {
        if (propertyOracle == null) {
            PropertyCombinations permutations = new PropertyCombinations(moduleDef.getProperties(),
                    moduleDef.getActiveLinkerNames());

            SortedSet<ConfigurationProperty> configPropSet = moduleDef.getProperties().getConfigurationProperties();
            ConfigurationProperty[] configProps = configPropSet.toArray(new ConfigurationProperty[configPropSet.size()]);

            BindingProperty[] orderedProperties = permutations.getOrderedProperties();

            WithProperties withProperties = GwtTestDataHolder.get().getCurrentWithProperties();

            String[] processedProperties;
            if (withProperties != null) {
                processedProperties = replaceOrderedPropertyValues(orderedProperties,
                        permutations.getOrderedPropertyValues(0), withProperties);
            } else {
                processedProperties = permutations.getOrderedPropertyValues(0);
            }

            propertyOracle = new StaticPropertyOracle(orderedProperties, processedProperties,
                    configProps);
        }

        return propertyOracle;
    }

    private CompiledClass compile(String literalName) throws UnableToCompleteException {
        CompiledClass compiledClass = compiledClassMap.get(literalName);
        if (compiledClass != null) {
            logger.log(Type.INFO, "Using cached resource for " + literalName);
            return compiledClass;
        }

        if (moduleDef == null) {
            throw new RuntimeException("Gwt module is not set.");
        }

        logger.log(Type.INFO, "Generating " + literalName);

        String className;
        className = getModuleSpaceHost().rebind(logger, literalName);

        String internalName = Name.BinaryName.toInternalName(className);

        compiledClass = compilationState.getClassFileMap().get(internalName);

        if (compiledClass != null) {
            compiledClassMap.put(literalName, compiledClass);
        }

        return compiledClass;
    }

    private ModuleSpace createModuleSpace(ModuleSpaceHost host) {

        return new ModuleSpace(GwtTreeLogger.get(), host, moduleDef.getCanonicalName()) {

            public void createNativeMethods(TreeLogger logger, List<JsniMethod> jsniMethods,
                                            DispatchIdOracle dispatchIdOracle) {
                // this method should never be called
                throw new UnsupportedOperationException(
                        "ModuleSpace.createNativeMethods(..) not supported by gwt-test-utils");
            }

            @Override
            protected void createStaticDispatcher(TreeLogger logger) {
                // this method should never be called
                throw new UnsupportedOperationException(
                        "ModuleSpace.createStaticDispatcher(..) not supported by gwt-test-utils");

            }

            @Override
            protected JsValue doInvoke(String name, Object jthis, Class<?>[] types, Object[] args)
                    throws Throwable {
                // this method should never be called
                throw new UnsupportedOperationException(
                        "ModuleSpace.doInvoke(..) not supported by gwt-test-utils");
            }

            @Override
            protected Object getStaticDispatcher() {
                // this method should never be called
                throw new UnsupportedOperationException(
                        "ModuleSpace.getStaticDispatcher() not supported by gwt-test-utils");

            }
        };

    }

    private ModuleSpaceHost createModuleSpaceHost(CompilationState compilationState,
                                                  ModuleDef moduleDef) {
        try {
            ModuleSpaceHost moduleSpaceHost = new GwtTestModuleSpaceHost(GwtTreeLogger.get(),
                    compilationState, moduleDef, null, ARTIFACT_ACCEPTOR, REBIND_CACHE);
            ModuleSpace moduleSpace = createModuleSpace(moduleSpaceHost);
            moduleSpaceHost.onModuleReady(moduleSpace);

            return moduleSpaceHost;
        } catch (UnableToCompleteException e) {
            throw new GwtTestConfigurationException("Error while creating global ModuleSpaceHost :", e);
        }
    }

    private ModuleSpaceHost getModuleSpaceHost() {
        WithProperties withProperties = GwtTestDataHolder.get().getCurrentWithProperties();
        String serializedProperties = serialize(withProperties);

        ModuleSpaceHost moduleSpaceHost = moduleSpaceHosts.get(serializedProperties);

        if (moduleSpaceHost == null) {
            moduleSpaceHost = createModuleSpaceHost(compilationState, moduleDef);
            moduleSpaceHosts.put(serializedProperties, moduleSpaceHost);
        }

        return moduleSpaceHost;
    }

    private TreeMap<String, String> propertiesToTreeMap(WithProperties withProperties) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (Property property : withProperties.value()) {
            treeMap.put(property.name(), property.value());
        }

        return treeMap;
    }

    private String[] replaceOrderedPropertyValues(BindingProperty[] orderedProperties,
                                                  String[] orderedOriginalValues, WithProperties withProperties) {

        Map<String, String> propertiesOverrides = propertiesToTreeMap(withProperties);

        String[] result = new String[orderedProperties.length];
        for (int i = 0; i < orderedProperties.length; i++) {
            if (propertiesOverrides.containsKey(orderedProperties[i].getName())) {
                result[i] = propertiesOverrides.get(orderedProperties[i].getName());
            } else {
                result[i] = orderedOriginalValues[i];
            }
        }
        return result;
    }

    private String serialize(WithProperties withProperties) {
        return withProperties != null ? propertiesToTreeMap(withProperties).toString() : "";
    }
}
