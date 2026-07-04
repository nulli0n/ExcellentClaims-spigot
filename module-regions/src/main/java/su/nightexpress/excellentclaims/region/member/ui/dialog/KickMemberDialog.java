package su.nightexpress.excellentclaims.region.member.ui.dialog;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.member.MemberLang;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberActionUIContext;
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
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class KickMemberDialog extends Dialog<MemberActionUIContext> {

    private final MemberUIController controller;

    public KickMemberDialog(MemberUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, MemberActionUIContext data) {
        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.PLAYER_NAME, () -> data.memberContext().userData().getName())
            .with(data.claim().placeholders())
            .build();

        DialogElementLocale bodyLocale = MemberLang.UI_DIALOG_KICK_MEMBER_BODY;
        WrappedPlainMessageDialogBody body = DialogBodies
            .plainMessage(placeholders.apply(bodyLocale.contents()), bodyLocale.width());

        NightItem icon = NightItem.fromType(Material.PLAYER_HEAD)
            .setPlayerProfile(data.memberContext().userData().getEffectiveProfile());

        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(MemberLang.UI_DIALOG_KICK_MEMBER_TITLE)
                .body(
                    DialogBodies.item(icon).showTooltip(false).build(),
                    body
                )
                .build()
            )
            .type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()))
            .handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                this.controller.onKickMemberConfirm(player, data.claim(), data.memberContext());
                viewer.callback();
            })
            .build()
        );
    }
}
