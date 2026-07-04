package su.nightexpress.excellentclaims.land.member.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.MemberLang;
import su.nightexpress.excellentclaims.land.member.ui.MemberUIController;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class AddMemberByNameDialog extends Dialog<LandClaim> {

    private static final String JSON_PLAYER_INPUT = "player_name";

    private final MemberUIController controller;

    public AddMemberByNameDialog(MemberUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, LandClaim claim) {
        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(MemberLang.UI_DIALOG_ADD_MEMBER_BY_ONLINE_TITLE)
                .body(DialogBodies.plainMessage(MemberLang.UI_DIALOG_ADD_MEMBER_BY_ONLINE_BODY))
                .inputs(DialogInputs.text(JSON_PLAYER_INPUT, "Player Name")
                    .maxLength(24)
                    .build()
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()))
            .handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_PLAYER_INPUT).orElse(null);
                if (name == null) return;

                //this.controller.onSelectMemberToAdd(player, claim, playerId);
                viewer.callback();
            })
            .build()
        );
    }
}
