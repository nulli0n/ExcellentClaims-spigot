package su.nightexpress.excellentclaims.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.menu.impl.ClaimMenu;
import su.nightexpress.excellentclaims.menu.impl.Confirmation;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;

public class MenuClicks {

    @NotNull
    public static ItemClick forClaimRemove(@NotNull ClaimPlugin plugin, @NotNull ClaimMenu menu) {
        return (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = menu.getLink(player);

            plugin.getMenuManager().openConfirm(player, Confirmation.create(
                (viewer1, event1) -> {
                    if (claim instanceof RegionClaim regionClaim) {
                        plugin.getClaimManager().removeRegion(player, regionClaim);
                    }
                    else if (claim instanceof LandClaim landClaim) {
                        plugin.getClaimManager().unclaimChunk(player, landClaim);
                    }
                    menu.runNextTick(player::closeInventory);
                },
                (viewer1, event1) -> {
                    menu.runNextTick(() -> menu.open(player, claim));
                }
            ));
        };
    }
}
