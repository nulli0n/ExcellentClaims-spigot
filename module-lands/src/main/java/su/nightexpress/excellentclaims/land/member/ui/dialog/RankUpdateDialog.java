package su.nightexpress.excellentclaims.land.member.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.MemberContext;
import su.nightexpress.excellentclaims.land.member.MemberLang;
import su.nightexpress.excellentclaims.land.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.land.member.ui.context.MemberActionUIContext;
import su.nightexpress.excellentclaims.land.member.ui.context.RankUpdateContext;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
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
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RankUpdateDialog extends Dialog<RankUpdateContext> {

    private static final String ACTION_RANK_BUTTON = "rank_button";

    private static final String JSON_RANK_ID = "rank_id";

    private final MemberUIController controller;

    public RankUpdateDialog(MemberUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RankUpdateContext data) {
        MemberActionUIContext actionContext = data.actionContext();
        MemberContext memberContext = actionContext.memberContext();
        LandClaim claim = actionContext.claim();
        UserData userData = memberContext.userData();
        List<Rank> ranks = data.ranks();
        boolean promotion = data.promotion();

        List<WrappedActionButton> buttons = new ArrayList<>();

        ranks.forEach(rank -> {
            PlaceholderContext placeholders = PlaceholderContext.builder()
                .with(rank.placeholders())
                .build();

            String rankId = rank.id().value();
            ButtonLocale locale = promotion ? MemberLang.UI_DIALOG_RANK_PROMOTION_RANK_BUTTON : MemberLang.UI_DIALOG_RANK_DEMOTION_RANK_BUTTON;

            NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_RANK_ID, rankId).build();

            WrappedActionButton button = DialogButtons.action(locale)
                .placeholders(placeholders)
                .action(DialogActions.customClick(ACTION_RANK_BUTTON, nbt))
                .build();

            buttons.add(button);
        });

        PlaceholderContext bodyPlaceholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.PLAYER_NAME, () -> userData.getName())
            .with(claim.placeholders())
            .build();

        TextLocale title;
        DialogElementLocale bodyLocale;

        if (promotion) {
            title = MemberLang.UI_DIALOG_RANK_PROMOTION_TITLE;
            bodyLocale = MemberLang.UI_DIALOG_RANK_PROMOTION_BODY;
        }
        else {
            title = MemberLang.UI_DIALOG_RANK_DEMOTION_TITLE;
            bodyLocale = MemberLang.UI_DIALOG_RANK_DEMOTION_BODY;
        }

        WrappedPlainMessageDialogBody body = DialogBodies
            .plainMessage(bodyPlaceholders.apply(bodyLocale.contents()), bodyLocale.width());

        return Dialogs.create(builder -> builder
            .base(DialogBases.builder(title)
                .body(
                    DialogBodies.item(NightItem.fromType(Material.PLAYER_HEAD)
                        .setPlayerProfile(userData.getEffectiveProfile()))
                        .showTooltip(false)
                        .build(),
                    body
                )
                .build()
            )
            .type(DialogTypes.multiAction(buttons)
                .columns(3)
                .exitAction(DialogButtons.cancel())
                .build()
            )
            .handleResponse(ACTION_RANK_BUTTON, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String rankId = nbtHolder.getText(JSON_RANK_ID).orElse(null);
                if (rankId == null) return;

                Identifier id = Identifier.parse(rankId).orElse(null);
                if (id == null) return;

                Rank rank = ranks.stream()
                    .filter(other -> other.id().equals(id))
                    .findFirst()
                    .orElse(null);
                if (rank == null) return;

                if (promotion) {
                    this.controller.onPromotionRankSelect(player, claim, memberContext, rank);
                }
                else {
                    this.controller.onDemotionRankSelect(player, claim, memberContext, rank);
                }

                viewer.callback();
            })
            .build()
        );
    }
}
