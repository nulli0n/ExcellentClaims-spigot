package su.nightexpress.excellentclaims.region.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
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
public class EditorDescriptionDialog extends Dialog<RegionClaim> {

    private static final TextLocale TITLE = LangEntry.builder("Regions.UI.Editor.Dialog.Description.Title")
        .text(title("Region", "Description"));

    private static final DialogElementLocale BODY = LangEntry.builder("Regions.UI.Editor.Dialog.Description.Body")
        .dialogElement(400,
            "Enter new description for the region."
        );

    private static final TextLocale INPUT_ID = LangEntry.builder("Regions.UI.Editor.Dialog.Description.Input.Name")
        .text("Description");

    private static final String JSON_DESCRIPTION = "description";

    private final RegionSettings      settings;
    private final RegionEditorService editor;
    private final MessageDispatcher   dispatcher;

    public EditorDescriptionDialog(RegionSettings settings,
                                   RegionEditorService editor,
                                   MessageDispatcher dispatcher) {
        super();
        this.settings = settings;
        this.editor = editor;
        this.dispatcher = dispatcher;
    }

    @Override
    public WrappedDialog create(Player player, RegionClaim claim) {
        return Dialogs.builder()
            .base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(DialogInputs.text(JSON_DESCRIPTION, INPUT_ID)
                    .maxLength(this.settings.getRegionDescriptionMaxLength() * 2) // x2 for space for color codes
                    .width(200)
                    .initial(String.join("\n", claim.getDescription()))
                    .multiline(new WrappedMultilineOptions(1, 50))
                    .build()
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()))
            .handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String description = nbtHolder.getText(JSON_DESCRIPTION).orElse(null);
                if (description == null) return;

                this.editor.setDescription(player, claim, description).handleFeedback((locale, ctx) -> {
                    this.dispatcher.send(locale, player, ctx);
                });

                viewer.callback();
            })
            .build();
    }
}
