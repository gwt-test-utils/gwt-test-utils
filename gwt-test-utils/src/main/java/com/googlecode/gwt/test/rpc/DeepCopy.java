package com.googlecode.gwt.test.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects. Objects are first
 * serialized and then deserialized. Error checking is fairly minimal in this implementation. If an
 * object is encountered that cannot be serialized (or that references an object that cannot be
 * serialized) an error is printed to System.err and null is returned. Depending on your specific
 * application, it might make more sense to have copy(...) re-throw the exception.
 */
class DeepCopy {

    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T orig) throws Exception {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            in = new ObjectInputStream(fbos.getInputStream());
            return (T) in.readObject();
        } finally {
            if (out != null)
                out.close();

            if (in != null)
                in.close();
        }
    }
}
