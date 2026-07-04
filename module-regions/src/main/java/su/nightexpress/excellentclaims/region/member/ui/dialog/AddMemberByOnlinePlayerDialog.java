package su.nightexpress.excellentclaims.region.member.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.region.member.MemberLang;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.region.member.ui.context.OnlineMemberContext;
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
public class AddMemberByOnlinePlayerDialog extends Dialog<OnlineMemberContext> {

    private static final String ACTION_PLAYER_NAME = "player_name";

    private static final String JSON_PLAYER_NAME = "name";

    private final MemberUIController controller;

    public AddMemberByOnlinePlayerDialog(MemberUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, OnlineMemberContext context) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        context.users().forEach(user -> {
            PlaceholderContext placeholders = PlaceholderContext.builder()
                .with(CommonPlaceholders.PLAYER_NAME, user::getName)
                .with(ClaimsPlaceholders.GENERIC_ICON, () -> {
                    return TagWrappers.HEAD_HAT.apply(user.getName());
                })
                .build();

            NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_PLAYER_NAME, user.getId().toString())
                .build();

            buttons.add(DialogButtons
                .action(MemberLang.UI_DIALOG_ADD_MEMBER_BY_ONLINE_PLAYER_BUTTON)
                .placeholders(placeholders)
                .action(DialogActions.customClick(ACTION_PLAYER_NAME, nbt))
                .build()
            );
        });

        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(MemberLang.UI_DIALOG_ADD_MEMBER_BY_ONLINE_TITLE)
                .body(DialogBodies.plainMessage(MemberLang.UI_DIALOG_ADD_MEMBER_BY_ONLINE_BODY))
                .build()
            )
            .type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            )
            .handleResponse(ACTION_PLAYER_NAME, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String clickedId = nbtHolder.getText(JSON_PLAYER_NAME).orElse(null);
                if (clickedId == null) return;

                UUID playerId = UUID.fromString(clickedId);
                this.controller.onSelectMemberToAdd(player, context.claim(), playerId);

                viewer.callback();
            })
            .build()
        );
    }
}
