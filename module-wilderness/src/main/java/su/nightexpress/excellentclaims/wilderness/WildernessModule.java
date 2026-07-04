package su.nightexpress.excellentclaims.wilderness;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;

@NullMarked
public class WildernessModule extends AbstractModule implements ClaimModule {

    private final Path moduleDir;

    private final WildernessRepository  repository;
    private final WildernessDataService dataService;

    public WildernessModule(Identifier id, Path moduleDir, WildernessRepository repository,
                            WildernessDataService dataService) {
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
    public WildernessRepository getRepository() {
        return this.repository;
    }

    @Override
    public WildernessDataService getDataService() {
        return this.dataService;
    }
}
