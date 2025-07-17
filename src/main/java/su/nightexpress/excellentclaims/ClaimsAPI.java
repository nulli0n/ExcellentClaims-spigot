package su.nightexpress.excellentclaims;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.claim.ClaimManager;
import su.nightexpress.excellentclaims.member.MemberManager;
import su.nightexpress.excellentclaims.menu.MenuManager;
import su.nightexpress.excellentclaims.selection.SelectionManager;

public class ClaimsAPI {

    private static ClaimPlugin plugin;

    static void load(@NotNull ClaimPlugin plugin) {
        ClaimsAPI.plugin = plugin;
    }

    static void shutdown() {
        plugin = null;
    }

    public static boolean isLoaded() {
        return plugin != null;
    }

    @NotNull
    public static ClaimPlugin getPlugin() {
        if (plugin == null) throw new IllegalStateException("API is not yet initialized!");

        return plugin;
    }

    public static boolean isAdminMode(@NotNull Player player) {
        return getMemberManager().isAdminMode(player);
    }

    @NotNull
    public static ClaimManager getClaimManager() {
        return getPlugin().getClaimManager();
    }

    @NotNull
    public static MemberManager getMemberManager() {
        return getPlugin().getMemberManager();
    }

    @NotNull
    public static SelectionManager getSelectionManager() {
        return getPlugin().getSelectionManager();
    }

    @NotNull
    public static MenuManager getMenuManager() {
        return getPlugin().getMenuManager();
    }
}
