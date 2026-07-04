package su.nightexpress.excellentclaims.admin;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.admin.AdminBypassAPI;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public class AdminBypassModule extends AbstractModule {

    private final AdminBypassService service;

    public AdminBypassModule(Identifier id, AdminBypassService service) {
        super(id);
        this.service = service;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        this.service.clearSessions();
    }

    @Override
    protected void onStart() {

    }

    public AdminBypassAPI getAPI() {
        return this.service;
    }
}
