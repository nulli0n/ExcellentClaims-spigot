package su.nightexpress.excellentclaims.rank;

import java.util.stream.Collectors;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;

public final class RanksPlaceholders {

    public static final TypedPlaceholder<Rank> MEMBER_RANK = TypedPlaceholder.builder(Rank.class)
        .with(ClaimsPlaceholders.RANK_NAME, Rank::getDisplayName)
        .with(ClaimsPlaceholders.RANK_PRIORITY, rank -> NumberUtil.format(rank.getPriority()))
        .with(ClaimsPlaceholders.RANK_PERMISSIONS, rank -> rank.hasPermission(ClaimPermission.ADMIN) ? CoreLang
            .goodEntry(
                Lang.CLAIM_PERMISSION
                    .getLocalized(ClaimPermission.ADMIN)) : rank.getEffectivePermissions().stream().map(
                        Lang.CLAIM_PERMISSION::getLocalized).map(CoreLang::goodEntry).collect(Collectors.joining(
                            "\n")))
        .build();

    private RanksPlaceholders() {
    }
}
