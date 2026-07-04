package su.nightexpress.excellentclaims.land.member.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.MemberLang;
import su.nightexpress.excellentclaims.land.member.ui.MemberUIController;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class PurgeDialog extends Dialog<LandClaim> {

    private final MemberUIController controller;

    public PurgeDialog(MemberUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, LandClaim claim) {
        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(claim.placeholders())
            .build();

        DialogElementLocale bodyLocale = MemberLang.UI_DIALOG_PURGE_MEMBERS_BODY;
        WrappedPlainMessageDialogBody body = DialogBodies
            .plainMessage(placeholders.apply(bodyLocale.contents()), bodyLocale.width());

        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(MemberLang.UI_DIALOG_PURGE_MEMBERS_TITLE)
                .body(
                    DialogBodies.item(claim.getIcon()).showTooltip(false).build(),
                    body
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()))
            .handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                this.controller.onPurgeMembersConfirm(player, claim);
                viewer.callback();
            })
            .build()
        );
    }
}
