package su.nightexpress.excellentclaims.region.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class EditorPriorityDialog extends Dialog<RegionClaim> {

    private static final TextLocale TITLE = LangEntry.builder("Regions.UI.Editor.Dialog.Priority.Title")
        .text(title("Region", "Priority"));

    private static final DialogElementLocale BODY = LangEntry.builder("Regions.UI.Editor.Dialog.Priority.Body")
        .dialogElement(400,
            "Enter new priority value for the region."
        );

    private static final TextLocale INPUT_PRIORITY = LangEntry.builder(
        "Regions.UI.Editor.Dialog.Priority.Input.Priority")
        .text("Priority");

    private static final String JSON_PRIORITY = "priority";

    private final RegionEditorService editor;
    private final MessageDispatcher   dispatcher;

    public EditorPriorityDialog(RegionEditorService editor, MessageDispatcher dispatcher) {
        super();
        this.editor = editor;
        this.dispatcher = dispatcher;
    }

    @Override
    public WrappedDialog create(Player player, RegionClaim claim) {
        return Dialogs.builder()
            .base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.text(JSON_PRIORITY, INPUT_PRIORITY)
                        .initial(String.valueOf(claim.getPriority()))
                        .maxLength(5)
                        .build()
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()))
            .handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                int priority = nbtHolder.getInt(JSON_PRIORITY, claim.getPriority());

                this.editor.setPriority(player, claim, priority).handleFeedback((locale, ctx) -> {
                    this.dispatcher.send(locale, player, ctx);
                });

                viewer.callback();
            })
            .build();
    }
}
