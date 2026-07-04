package su.nightexpress.excellentclaims.wilderness.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
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
public class EditorPriorityDialog extends Dialog<WildernessRegion> {

    private static final TextLocale TITLE = LangEntry.builder("Wilderness.UI.Editor.Dialog.Priority.Title")
        .text(title("Wilderness", "Priority"));

    private static final DialogElementLocale BODY = LangEntry.builder("Wilderness.UI.Editor.Dialog.Priority.Body")
        .dialogElement(400,
            "Enter new priority value for the wilderness region."
        );

    private static final TextLocale INPUT_PRIORITY = LangEntry.builder(
        "Wilderness.UI.Editor.Dialog.Priority.Input.Priority")
        .text("Priority");

    private static final String JSON_PRIORITY = "priority";

    private final WildernessEditorService editor;
    private final MessageDispatcher       dispatcher;

    public EditorPriorityDialog(WildernessEditorService editor, MessageDispatcher dispatcher) {
        super();
        this.editor = editor;
        this.dispatcher = dispatcher;
    }

    @Override
    public WrappedDialog create(Player player, WildernessRegion claim) {
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
