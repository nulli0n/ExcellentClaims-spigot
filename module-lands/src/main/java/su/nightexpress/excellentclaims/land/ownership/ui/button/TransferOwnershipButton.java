package su.nightexpress.excellentclaims.land.ownership.ui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ownership.LandOwnershipService;
import su.nightexpress.excellentclaims.land.ownership.ui.OwnershipUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class TransferOwnershipButton implements DefaultButtonExtension {

    private final OwnershipUIController controller;
    private final LandOwnershipService  service;

    public TransferOwnershipButton(OwnershipUIController controller, LandOwnershipService service) {
        this.controller = controller;
        this.service = service;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("land_transfer_ownership", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.TRIAL_KEY)
                    .setDisplayName(TagWrappers.ORANGE.wrap(TagWrappers.BOLD.wrap("Transfer Ownership")))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Transfer claim to other player."),
                        "",
                        TagWrappers.ORANGE.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(33)
            .build()
        );
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        return this.service.canTransferOwnership(player, claim).success();
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        this.controller.onTransferClick(player, claim);
    }
}
