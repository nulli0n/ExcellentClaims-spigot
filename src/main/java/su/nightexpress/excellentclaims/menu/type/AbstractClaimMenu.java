package su.nightexpress.excellentclaims.menu.type;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;

public abstract class AbstractClaimMenu extends LinkedMenu<ClaimPlugin, Claim> implements ClaimMenu {

    protected final ClaimPermission permission;

    public AbstractClaimMenu(@NotNull ClaimPlugin plugin, @NotNull MenuType menuType, @NotNull String title, @NotNull ClaimPermission permission) {
        super(plugin, menuType, title);
        this.permission = permission;
    }

    @Nullable
    protected abstract AbstractClaimMenu getBackMenu();

    protected boolean canReturn(@NotNull MenuViewer viewer) {
        ClaimMenu claimMenu = this.getBackMenu();

        return claimMenu != null && claimMenu.hasPermission(viewer.getPlayer(), this.getLink(viewer));
    }

    @Override
    public boolean hasPermission(@NotNull Player player, @NotNull Claim claim) {
        return claim.hasPermission(player, this.permission);
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).replacePlaceholders().apply(this.title);
    }

    protected void handleReturn(@NotNull MenuViewer viewer) {
        this.handleReturn(viewer.getPlayer());
    }

    protected void handleReturn(@NotNull Player player) {
        AbstractClaimMenu menu = this.getBackMenu();
        if (menu == null) return;

        this.runNextTick(() -> menu.open(player, this.getLink(player)));
    }

    @NotNull
    protected MenuItem createBackButton(int slot) {
        return MenuItem.buildReturn(this, slot, (viewer, event) -> this.handleReturn(viewer), ItemOptions.builder().setVisibilityPolicy(this::canReturn).build()).build();
    }
}
