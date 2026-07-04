package su.nightexpress.excellentclaims.engine.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.engine.ModuleRegistry;
import su.nightexpress.excellentclaims.engine.settings.EngineSettings;

@NullMarked
public class ClaimAutoSaveController extends AbstractController {

    private final ModuleRegistry modules;
    private final EngineSettings settings;

    public ClaimAutoSaveController(ClaimPlugin plugin, ModuleRegistry modules, EngineSettings settings) {
        super(plugin);
        this.modules = modules;
        this.settings = settings;
    }

    @Override
    public void onStart() {
        this.runSaveTask();
    }

    @Override
    protected void onReload() {
        this.stopTasks();
        this.runSaveTask();
    }

    @Override
    protected void onShutdown() {
        this.saveClaims();
    }

    private void runSaveTask() {
        this.addAsyncSecondsTask(this::saveClaims, this.settings.getAutoSaveInterval());
    }

    public void saveClaims() {
        this.modules.values().forEach(module -> {
            module.getDataService().saveDirty();
        });
    }
}
