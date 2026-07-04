package su.nightexpress.excellentclaims.region.editor.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class RegionEditorUIController {

    private final RegionEditorService   editor;
    private final RegionEditorUIService uiService;
    private final MessageDispatcher     dispatcher;

    public RegionEditorUIController(RegionEditorService editorService, RegionEditorUIService uiService,
                                    MessageDispatcher dispatcher) {
        this.editor = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onSetNameClick(Player player, RegionClaim claim, Runnable callback) {
        this.uiService.openNameDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetDescriptionClick(Player player, RegionClaim claim, Runnable callback) {
        this.uiService.openDescriptionDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetIconClick(Player player, RegionClaim claim) {
        this.uiService.openIconSelectionMenu(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetPriorityClick(Player player, RegionClaim claim, Runnable callback) {
        this.uiService.openPriorityDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onIconSelect(Player player, RegionClaim claim, ItemStack itemStack) {
        ActionResult result = this.editor.setIcon(player, claim, NightItem.fromItemStack(itemStack));

        result.handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });

        if (result.failure()) {
            player.closeInventory();
            return;
        }

        if (result.success()) {
            ActionResult backResult = this.uiService.openClaimMenu(player, claim);
            backResult.handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });

            // Close icon selection menu if player lost access to claim management (failed to return back to claim menu)
            if (backResult.failure()) {
                player.closeInventory();
            }
        }
    }

    public void onHomeClick(Player player, RegionClaim claim, boolean force) {
        this.editor.teleport(player, claim, force).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
