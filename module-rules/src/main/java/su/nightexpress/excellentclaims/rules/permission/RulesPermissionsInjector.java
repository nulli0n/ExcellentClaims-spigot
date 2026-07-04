package su.nightexpress.excellentclaims.rules.permission;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;

@NullMarked
public final class RulesPermissionsInjector {

    private RulesPermissionsInjector() {
    }

    public static void inject(ClaimPlugin plugin) {
        plugin.registerPermissions(RulesPermissions.ROOT);
    }
}
