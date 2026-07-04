package su.nightexpress.excellentclaims.land.ownership.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.land.ownership.OwnershipLang;
import su.nightexpress.excellentclaims.land.ownership.ui.OwnershipUIController;
import su.nightexpress.excellentclaims.land.ownership.ui.context.TransferTargetContext;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class TransferSelectTargetDialog extends Dialog<TransferTargetContext> {

    private static final String ACTION_PLAYER = "player";

    private static final String JSON_PLAYER_ID = "player_id";

    private final OwnershipUIController controller;

    public TransferSelectTargetDialog(OwnershipUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, TransferTargetContext context) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        context.eligibles().forEach(user -> {
            PlaceholderContext placeholders = PlaceholderContext.builder()
                .with(CommonPlaceholders.PLAYER_NAME, user::getName)
                .with(ClaimsPlaceholders.GENERIC_ICON, () -> {
                    return TagWrappers.HEAD_HAT.apply(user.getName());
                })
                .build();

            NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_PLAYER_ID, user.getId().toString())
                .build();

            buttons.add(DialogButtons
                .action(OwnershipLang.UI_DIALOG_TRANSFER_SELECT_TARGET_PLAYER_BUTTON)
                .placeholders(placeholders)
                .action(DialogActions.customClick(ACTION_PLAYER, nbt))
                .build()
            );
        });

        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(OwnershipLang.UI_DIALOG_TRANSFER_SELECT_TARGET_TITLE)
                .body(DialogBodies.plainMessage(OwnershipLang.UI_DIALOG_TRANSFER_SELECT_TARGET_BODY))
                .build()
            )
            .type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            )
            .handleResponse(ACTION_PLAYER, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String clickedId = nbtHolder.getText(JSON_PLAYER_ID).orElse(null);
                if (clickedId == null) return;

                UUID playerId = UUID.fromString(clickedId);
                this.controller.onTransferTargetSelect(player, context.claim(), playerId);

                viewer.callback();
            })
            .build()
        );
    }
}
