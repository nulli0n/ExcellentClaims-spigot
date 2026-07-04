package su.nightexpress.excellentclaims.engine.module;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.engine.ModuleRegistry;

public class EngineModuleLoader extends AbstractModule {

    private final ModuleRegistry modules;
    private final ClaimRegistry  claims;

    public EngineModuleLoader(ModuleRegistry modules, ClaimRegistry claims) {
        super(Identifier.of("engine_module_loader"));
        this.modules = modules;
        this.claims = claims;
    }

    public void registerModule(ClaimModule module) {
        this.modules.register(module);
        this.claims.registerRepository(module.getRepository());
        this.addComponent(module);
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }
}
