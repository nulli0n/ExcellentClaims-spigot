package su.nightexpress.excellentclaims.region.claim.ui.dialog;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GREEN;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.RED;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.claim.RegionClaimService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class RegionRemoveConfirmDialog extends Dialog<RegionClaim> {

    private static final TextLocale TITLE = LangEntry.builder("Region.Dialog.RemoveConfirm.Title").text(title("Land",
        "Remove Confirmation"));

    private static final DialogElementLocale BODY = LangEntry.builder("Region.Dialog.RemoveConfirm.Body").dialogElement(
        400,
        "Confirm region removal."
    );

    private static final ButtonLocale BUTTON_YES = LangEntry.builder("Region.Dialog.RemoveConfirm.Button.Yes")
        .button(RED.and(BOLD).wrap("✔ Yes"), "Region will be permanently removed with no undo.");

    private static final ButtonLocale BUTTON_NO = LangEntry.builder("Region.Dialog.RemoveConfirm.Button.Yes")
        .button(GREEN.and(BOLD).wrap("✘ No"), "Cancel land removal.");

    private static final String JSON_YES = "yes";
    private static final String JSON_NO  = "no";

    private final RegionClaimService claimService;

    public RegionRemoveConfirmDialog(RegionClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, RegionClaim claim) {
        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .build()
            )
            .type(DialogTypes.confirmation(
                DialogButtons.action(BUTTON_YES).action(DialogActions.customClick(JSON_YES)).build(),
                DialogButtons.action(BUTTON_NO).action(DialogActions.customClick(JSON_NO)).build()
            ))
            .handleResponse(JSON_YES, (viewer, identifier, nbtHolder) -> {
                this.claimService.unclaimChunk(player, claim);

                viewer.callback();
            })
            .handleResponse(JSON_NO, (viewer, identifier, nbtHolder) -> {
                viewer.callback();
            })
            .build()
        );
    }
}
