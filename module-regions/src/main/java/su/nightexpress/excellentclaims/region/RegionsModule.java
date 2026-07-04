package su.nightexpress.excellentclaims.region;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.region.data.RegionDataService;

@NullMarked
public class RegionsModule extends AbstractModule implements ClaimModule {

    private final Path moduleDir;

    private final RegionsRepository repository;
    private final RegionDataService dataService;

    public RegionsModule(Identifier id, Path moduleDir, RegionsRepository repository, RegionDataService dataService) {
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
    public RegionsRepository getRepository() {
        return this.repository;
    }

    @Override
    public RegionDataService getDataService() {
        return this.dataService;
    }
}
