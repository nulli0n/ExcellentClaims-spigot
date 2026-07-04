package su.nightexpress.excellentclaims.land.editor.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.editor.LandEditorService;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class LandEditorUIController {

    private final LandEditorService   editor;
    private final LandEditorUIService uiService;
    private final MessageDispatcher   dispatcher;

    public LandEditorUIController(LandEditorService editorService, LandEditorUIService uiService,
                                  MessageDispatcher dispatcher) {
        this.editor = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onSetNameClick(Player player, LandClaim claim, Runnable callback) {
        this.uiService.openNameDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetDescriptionClick(Player player, LandClaim claim, Runnable callback) {
        this.uiService.openDescriptionDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetIconClick(Player player, LandClaim claim) {
        this.uiService.openIconSelectionMenu(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSetPriorityClick(Player player, LandClaim claim, Runnable callback) {
        this.uiService.openPriorityDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onIconSelect(Player player, LandClaim claim, ItemStack itemStack) {
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

    public void onHomeClick(Player player, LandClaim claim, boolean force) {
        this.editor.teleport(player, claim, force).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
