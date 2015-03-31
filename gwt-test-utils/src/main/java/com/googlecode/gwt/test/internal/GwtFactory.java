package com.googlecode.gwt.test.internal;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.CompilerContext;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.ModuleDefLoader;
import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.javac.CompilationStateBuilder;
import com.google.gwt.dev.javac.CompilationUnitArchive;
import com.google.gwt.dev.shell.JsValueGlue;
import com.googlecode.gwt.test.GwtTreeLogger;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.rewrite.OverlayTypesRewriter;

/**
 * An unique place for internal singleton which are ClassLoader independent and can eventually be
 * reset. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 *
 */
public class GwtFactory {

   private static GwtFactory instance;

   private static final Pattern SUREFIRE_BOOTER_JAR_PATTERN = Pattern.compile("^.*surefire.*\\.jar$");

   public static GwtFactory get() {
      return instance;
   }

   public static boolean initializeIfNeeded() {

      URL currentSurefireBooterJarUrl = findSureFireBooterJarUrl();

      if (instance == null) {
         // first return
         instance = new GwtFactory(currentSurefireBooterJarUrl);
         return true;
      }

      if (instance.surefireBooterJarUrl != null
               && !instance.surefireBooterJarUrl.equals(currentSurefireBooterJarUrl)
               || instance.surefireBooterJarUrl == null && currentSurefireBooterJarUrl != null) {
         // surefire booter jar has changed : this should never happen since
         // surefire-plugin runs each sub-module tests in an isolated
         // classloader
         // but just in case it would happen in the future, recompute everything
         // :-o
         instance = new GwtFactory(currentSurefireBooterJarUrl);

         return true;
      }

      return false;
   }

   private static URL findSureFireBooterJarUrl() {
      ClassLoader parent = Thread.currentThread().getContextClassLoader();
      if (parent instanceof URLClassLoader) {
         URL[] parentUrls = ((URLClassLoader) parent).getURLs();
         if (parentUrls.length == 1
                  && SUREFIRE_BOOTER_JAR_PATTERN.matcher(parentUrls[0].getPath()).matches()) {
            return parentUrls[0];
         }
      }

      return null;
   }

   private final CompilationState compilationState;
   private final CompilerContext compilerContext;
   private final CompilerContext.Builder compilerContextBuilder = new CompilerContext.Builder();
   private final ConfigurationLoader configurationLoader;
   private final GwtClassLoader gwtClassLoader;
   private final ModuleDef moduleDef;

   private final OverlayTypesRewriter overlayRewriter;

   private final URL surefireBooterJarUrl;

   private GwtFactory(URL surefireBooterJarUrl) {
      configurationLoader = new ConfigurationLoader(surefireBooterJarUrl);

      this.surefireBooterJarUrl = surefireBooterJarUrl;

      // create a the temporary classloader used to build the CompilationState object which needs
      // access to the project's resources (.java file)
      ClassLoader defaultClassLoader = Thread.currentThread().getContextClassLoader();

      CompilationStateClassLoader tempCl = new CompilationStateClassLoader(defaultClassLoader,
               configurationLoader);
      // ClassLoader classLoaderForResources =
      // createTemporaryClassLoaderForResourceLoading(surefireBooterJarUrl);
      Thread.currentThread().setContextClassLoader(tempCl);

      try {
         // create every gwt stuff, searching for resources in the setup
         // 'src-directories' entries in
         // META-INF/gwt-test-utils.properties files
         CompilerContext tempContext = compilerContextBuilder.build();
         moduleDef = createModuleDef(configurationLoader, tempContext);
         compilerContext = compilerContextBuilder.module(moduleDef).build();
         compilationState = createCompilationState(moduleDef, compilerContext);
         overlayRewriter = createOverlayRewriter(compilationState);
         gwtClassLoader = GwtClassLoader.createClassLoader(configurationLoader, compilationState,
                  overlayRewriter);
      } catch (UnableToCompleteException e) {
         // log related errors
         GwtTreeLogger.get().onUnableToCompleteError();
         throw new GwtTestException("Error while generating gwt-test-utils prerequisites", e);
      } finally {
         // reset the default classloader
         Thread.currentThread().setContextClassLoader(defaultClassLoader);
      }
   }

   public GwtClassLoader getClassLoader() {
      return gwtClassLoader;
   }

   public CompilationState getCompilationState() {
      return compilationState;
   }

   public ConfigurationLoader getConfigurationLoader() {
      return configurationLoader;
   }

   public ModuleDef getModuleDef() {
      return moduleDef;
   }

   public OverlayTypesRewriter getOverlayRewriter() {
      return overlayRewriter;
   }

   private CompilationState createCompilationState(ModuleDef moduleDef,
            CompilerContext compilerContext) throws UnableToCompleteException {
      TreeLogger treeLogger = GwtTreeLogger.get();
      return moduleDef.getCompilationState(treeLogger, compilerContext);
   }

   private ModuleDef createModuleDef(ConfigurationLoader configurationLoader,
            CompilerContext compilerContext) throws UnableToCompleteException {
      List<String> gwtModules = configurationLoader.getGwtModules();
      String[] inherits = gwtModules.toArray(new String[gwtModules.size()]);
      return ModuleDefLoader.createSyntheticModule(GwtTreeLogger.get(), compilerContext,
               "com.googlecode.gwt.test.Aggregator", inherits, false);
   }

   private OverlayTypesRewriter createOverlayRewriter(CompilationState compilationState) {
      TypeOracle typeOracle = compilationState.getTypeOracle();
      JClassType jsoType = typeOracle.findType(JsValueGlue.JSO_CLASS);

      // If we couldn't find the JSO class, we don't need to do any rewrites.
      return jsoType != null ? new OverlayTypesRewriter(compilationState, jsoType) : null;
   }

}
