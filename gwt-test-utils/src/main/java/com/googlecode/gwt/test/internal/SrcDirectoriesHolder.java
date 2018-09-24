package com.googlecode.gwt.test.internal;

import java.util.HashSet;
import java.util.Set;

/**
 * Holder for 'src-directory' values added in META-INF/gwt-test-utils.properties file, which can be
 * manipulate by both {@link GwtClassLoader} and System classloader. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class SrcDirectoriesHolder {

    public static final Set<String> SRC_DIRECTORIES = new HashSet<>();

}
