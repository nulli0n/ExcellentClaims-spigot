package su.nightexpress.excellentclaims.land;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.land.data.LandDataService;

@NullMarked
public class LandsModule extends AbstractModule implements ClaimModule {

    private final Path            moduleDir;
    private final LandsRepository repository;
    private final LandDataService dataService;

    public LandsModule(Identifier id, Path moduleDir, LandsRepository repository, LandDataService dataService) {
        super(id);
        this.moduleDir = moduleDir;
        this.repository = repository;
        this.dataService = dataService;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    public Path getModuleDir() {
        return this.moduleDir;
    }

    @Override
    public LandsRepository getRepository() {
        return this.repository;
    }

    @Override
    public LandDataService getDataService() {
        return dataService;
    }
}
