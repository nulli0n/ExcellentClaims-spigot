package su.nightexpress.excellentclaims;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderList;

import java.util.stream.Collectors;

public class Placeholders extends su.nightexpress.nightcore.util.Placeholders {

    public static final String GENERIC_TYPE    = "%type%";
    public static final String GENERIC_VOLUME  = "%volume%";
    public static final String GENERIC_AMOUNT  = "%amount%";
    public static final String GENERIC_MAX     = "%max%";
    public static final String GENERIC_MODE    = "%mode%";

    @Deprecated
    public static final String GENERIC_ENTRIES = "%entries%";

    public static final String CLAIM_ID          = "%claim_id%";
    public static final String CLAIM_NAME        = "%claim_name%";
    public static final String CLAIM_DESCRIPTION = "%claim_description%";
    public static final String CLAIM_OWNER_NAME  = "%claim_owner_name%";
    public static final String CLAIM_PRIORITY    = "%claim_priority%";
    public static final String CLAIM_WORLD       = "%claim_world%";

    public static final String LAND_CHUNKS = "%land_chunks%";

    public static final String RANK_ID          = "%rank_id%";
    public static final String RANK_NAME        = "%rank_name%";
    public static final String RANK_PRIORITY    = "%rank_priority%";
    public static final String RANK_PERMISSIONS = "%rank_permissions%";

    public static final String FLAG_ID          = "%flag_id%";
    public static final String FLAG_NAME        = "%flag_name%";
    public static final String FLAG_DESCRIPTION = "%flag_description%";

    @NotNull
    public static String goodEntry(@NotNull String value) {
        return Lang.OTHER_LIST_ENTRY_GOOD.getString().replace(GENERIC_VALUE, value);
    }

    @NotNull
    public static String badEntry(@NotNull String value) {
        return Lang.OTHER_LIST_ENTRY_BAD.getString().replace(GENERIC_VALUE, value);
    }

    public static final PlaceholderList<Claim> CLAIM = PlaceholderList.create(list -> {
        list
            .add(CLAIM_ID, Claim::getId)
            .add(CLAIM_NAME, Claim::getDisplayName)
            .add(CLAIM_DESCRIPTION, claim -> claim.getDescription() == null ? Lang.OTHER_NO_DESCRIPTION.getString() : claim.getDescription())
            .add(CLAIM_OWNER_NAME, Claim::getOwnerName)
            .add(CLAIM_PRIORITY, claim -> NumberUtil.format(claim.getPriority()))
            .add(CLAIM_WORLD, claim -> {
                World world = claim.getWorld();
                return world != null ? LangAssets.get(world) : claim.getWorldName();
            });
    });

    @NotNull
    public static final PlaceholderList<LandClaim> LAND_CLAIM = PlaceholderList.create(list -> list
        .add(CLAIM)
        .add(LAND_CHUNKS, landClaim -> String.valueOf(landClaim.getChunksAmount()))
    );

    public static final PlaceholderList<MemberRank> MEMBER_RANK = PlaceholderList.create(list -> list
        .add(RANK_ID, MemberRank::getId)
        .add(RANK_NAME, MemberRank::getDisplayName)
        .add(RANK_PRIORITY, rank -> NumberUtil.format(rank.getPriority()))
        .add(RANK_PERMISSIONS, rank -> rank.hasPermission(ClaimPermission.ALL) ?
            goodEntry(Lang.CLAIM_PERMISSION.getLocalized(ClaimPermission.ALL)) :
            rank.getPermissions().stream().map(Lang.CLAIM_PERMISSION::getLocalized).map(Placeholders::goodEntry).collect(Collectors.joining("\n")))
    );

    public static final PlaceholderList<Flag<?>> FLAG = PlaceholderList.create(list -> list
        .add(FLAG_ID, Flag::getId)
        .add(FLAG_NAME, Flag::getDisplayName)
        .add(FLAG_DESCRIPTION, flag -> String.join("\n", flag.getDescription()))
    );
}
