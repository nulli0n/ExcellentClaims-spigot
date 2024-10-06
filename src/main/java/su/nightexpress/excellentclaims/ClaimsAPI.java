package su.nightexpress.excellentclaims;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.claim.ClaimManager;
import su.nightexpress.excellentclaims.member.MemberManager;
import su.nightexpress.excellentclaims.selection.SelectionManager;

public class ClaimsAPI {

    private static ClaimPlugin plugin;

    static void load(@NotNull ClaimPlugin plugin) {
        ClaimsAPI.plugin = plugin;
    }

    static void shutdown() {
        plugin = null;
    }

    @NotNull
    public static ClaimPlugin getPlugin() {
        return plugin;
    }

    @NotNull
    public static ClaimManager getClaimManager() {
        return plugin.getClaimManager();
    }

    @NotNull
    public static MemberManager getMemberManager() {
        return plugin.getMemberManager();
    }

    @NotNull
    public static SelectionManager getSelectionManager() {
        return plugin.getSelectionManager();
    }
}
