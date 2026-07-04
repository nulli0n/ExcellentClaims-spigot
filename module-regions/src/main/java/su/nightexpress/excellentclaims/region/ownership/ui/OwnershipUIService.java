package su.nightexpress.excellentclaims.region.ownership.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.ownership.OwnershipService;
import su.nightexpress.excellentclaims.region.ownership.ui.context.TransferTargetContext;
import su.nightexpress.excellentclaims.region.ownership.ui.dialog.OwnershipDialogKeys;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

@NullMarked
public class OwnershipUIService {

    private final DialogRegistry dialogs;
    //private final LandOwnershipService ownershipService;
    //private final LandsUIService       coreUI;

    public OwnershipUIService(DialogRegistry dialogs,
                              OwnershipService ownershipService,
                              RegionUIService coreUI) {
        this.dialogs = dialogs;
        //this.ownershipService = ownershipService;
        //this.coreUI = coreUI;
    }

    public void showTransferTargetSelectDialog(Player player, TransferTargetContext context) {
        this.dialogs.show(player, OwnershipDialogKeys.TRANSFER_SELECT_TARGET, context, null);
    }
}
