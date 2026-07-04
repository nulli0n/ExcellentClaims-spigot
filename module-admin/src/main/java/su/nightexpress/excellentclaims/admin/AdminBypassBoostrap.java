package su.nightexpress.excellentclaims.admin;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.admin.AdminBypassAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public final class AdminBypassBoostrap {

    private static final Identifier ID = Identifier.of("admin_bypass");

    private AdminBypassBoostrap() {
    }

    public static AdminBypassModule bootstrap(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        plugin.injectLang(AdminModeLang.class);
        plugin.registerPermissions(AdminModePerms.ROOT);

        AdminBypassService bypassService = new AdminBypassService();
        AdminBypassModule module = new AdminBypassModule(ID, bypassService);

        container.register(AdminBypassAPI.class, bypassService);

        module.addComponent(new AdminModeCommand(plugin, bypassService));

        return module;
    }
}
