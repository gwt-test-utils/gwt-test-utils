package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.dev.CompilerContext;
import com.google.gwt.dev.PrecompileTaskOptionsImpl;
import com.google.gwt.dev.RebindCache;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.Rule;
import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.javac.StandardGeneratorContext;
import com.google.gwt.dev.shell.*;
import com.google.gwt.dev.util.log.speedtracer.DevModeEventType;
import com.google.gwt.dev.util.log.speedtracer.SpeedTracerLogger;
import com.google.gwt.dev.util.log.speedtracer.SpeedTracerLogger.Event;

import java.io.File;
import java.util.Deque;

/**
 * Provides an environment for a {@link com.google.gwt.dev.shell.ModuleSpace} that works
 * appropriately in gwt-test-utils. Almost copied from {@link ShellModuleSpaceHost} without the need
 * of an initialized {@link CompilingClassLoader}.
 */
class GwtTestModuleSpaceHost implements ModuleSpaceHost {

    // TODO(jat): hack to try and serialize rebinds
    private static final Object rebindLock = new Object[0];

    protected final CompilationState compilationState;

    protected final File genDir;

    private final ArtifactAcceptor artifactAcceptor;

    private final TreeLogger logger;

    private final ModuleDef module;

    private final RebindCache rebindCache;

    private StandardRebindOracle2 rebindOracle;

    private ModuleSpace space;

    /**
     * @param module the module associated with the hosted module space
     */
    public GwtTestModuleSpaceHost(TreeLogger logger, CompilationState compilationState,
                                  ModuleDef module, File genDir, ArtifactAcceptor artifactAcceptor,
                                  RebindCache rebindCache) {
        this.logger = logger;
        this.compilationState = compilationState;
        this.module = module;
        this.genDir = genDir;
        this.artifactAcceptor = artifactAcceptor;
        this.rebindCache = rebindCache;
    }

    public CompilingClassLoader getClassLoader() {
        return null;
    }

    public String[] getEntryPointTypeNames() {
        checkForModuleSpace();
        return module.getEntryPointTypeNames();
    }

    public TreeLogger getLogger() {
        return logger;
    }

    /**
     * Invalidates the given source type name, so the next rebind request will generate a type again.
     */
    public void invalidateRebind(String sourceTypeName) {
        checkForModuleSpace();
        rebindOracle.invalidateRebind(sourceTypeName);
    }

    public void onModuleReady(ModuleSpace readySpace) throws UnableToCompleteException {
        this.space = readySpace;

        Event moduleSpaceHostReadyEvent = SpeedTracerLogger.start(DevModeEventType.MODULE_SPACE_HOST_READY);
        try {
            // Establish an environment for JavaScript property providers to run.
            //
            ModuleSpacePropertyOracle propOracle = new ModuleSpacePropertyOracle(
                    module.getProperties(), module.getActiveLinkerNames(), readySpace);

            // Set up the rebind oracle for the module.
            // It has to wait until now because we need to inject javascript.
            //
            Deque<Rule> rules = module.getRules();
            PrecompileTaskOptionsImpl options = new PrecompileTaskOptionsImpl();
            options.setGenDir(genDir);
            CompilerContext compilerContext = new CompilerContext.Builder().module(module).options(
                    options).build();
            StandardGeneratorContext genCtx = new StandardGeneratorContext(compilerContext,
                    compilationState, new ArtifactSet(), false);

            // Only enable generator result caching if we have a valid rebindCache
            genCtx.setGeneratorResultCachingEnabled(rebindCache != null);

            rebindOracle = new StandardRebindOracle2(propOracle, rules, genCtx);
            rebindOracle.setRebindCache(rebindCache);
        } finally {
            moduleSpaceHostReadyEvent.end();
        }
    }

    public String rebind(TreeLogger logger, String sourceTypeName) throws UnableToCompleteException {
        synchronized (rebindLock) {
            checkForModuleSpace();
            return rebindOracle.rebind(logger, sourceTypeName, new ArtifactAcceptor() {
                public void accept(TreeLogger logger, ArtifactSet newlyGeneratedArtifacts)
                        throws UnableToCompleteException {
                    artifactAcceptor.accept(logger, newlyGeneratedArtifacts);
                }
            });
        }
    }

    private void checkForModuleSpace() {
        if (space == null) {
            throw new IllegalStateException("Module initialization error");
        }
    }
}
