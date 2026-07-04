package su.nightexpress.excellentclaims.wilderness.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
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
public class EditorNameDialog extends Dialog<WildernessRegion> {

    private static final TextLocale TITLE = LangEntry.builder("Wilderness.UI.Editor.Dialog.Name.Title")
        .text(title("Wilderness", "Name"));

    private static final DialogElementLocale BODY = LangEntry.builder("Wilderness.UI.Editor.Dialog.Name.Body")
        .dialogElement(400,
            "Enter a new name for the wilderness region."
        );

    private static final TextLocale INPUT_NAME = LangEntry.builder("Wilderness.UI.Editor.Dialog.Name.Input.Name")
        .text("Name");

    private static final String JSON_NAME = "name";

    private final WildernessSettings      settings;
    private final WildernessEditorService editor;
    private final MessageDispatcher       dispatcher;

    public EditorNameDialog(WildernessSettings settings, WildernessEditorService editor, MessageDispatcher dispatcher) {
        super();
        this.settings = settings;
        this.editor = editor;
        this.dispatcher = dispatcher;
    }

    @Override
    public WrappedDialog create(Player player, WildernessRegion claim) {
        return Dialogs.builder()
            .base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.text(JSON_NAME, INPUT_NAME)
                        .initial(claim.getName())
                        .maxLength(this.settings.getWildernessNameMaxLength() * 2) // x2 for space for color codes
                        .build()
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()))
            .handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_NAME, claim.getName());
                this.editor.setName(player, claim, name).handleFeedback((locale, ctx) -> {
                    this.dispatcher.send(locale, player, ctx);
                });

                viewer.callback();
            })
            .build();
    }
}
