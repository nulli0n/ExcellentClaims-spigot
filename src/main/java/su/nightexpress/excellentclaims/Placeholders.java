package su.nightexpress.excellentclaims;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.stream.Collectors;

public class Placeholders extends su.nightexpress.nightcore.util.Placeholders {

    public static final String GENERIC_TYPE = "%type%";

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

    @NotNull
    public static PlaceholderMap forClaim(@NotNull Claim claim) {
        PlaceholderMap map = new PlaceholderMap()
            .add(CLAIM_ID, claim::getId)
            .add(CLAIM_NAME, claim::getDisplayName)
            .add(CLAIM_DESCRIPTION, () -> claim.getDescription() == null ? Lang.OTHER_NO_DESCRIPTION.getString() : claim.getDescription())
            .add(CLAIM_OWNER_NAME, claim::getOwnerName)
            .add(CLAIM_PRIORITY, () -> NumberUtil.format(claim.getPriority()))
            .add(CLAIM_WORLD, () -> {
                World world = claim.getWorld();
                return world != null ? LangAssets.get(world) : claim.getWorldName();
            })
            ;

        if (claim instanceof ChunkClaim chunkClaim) {
            map.add(LAND_CHUNKS, () -> String.valueOf(chunkClaim.getChunksAmount()));
        }

        return map;
    }

    @NotNull
    public static PlaceholderMap forMemberRank(@NotNull MemberRank rank) {
        return new PlaceholderMap()
            .add(RANK_ID, rank::getId)
            .add(RANK_NAME, rank::getDisplayName)
            .add(RANK_PRIORITY, () -> NumberUtil.format(rank.getPriority()))
            .add(RANK_PERMISSIONS, () -> rank.hasPermission(ClaimPermission.ALL) ?
                goodEntry(Lang.CLAIM_PERMISSION.getLocalized(ClaimPermission.ALL)) :
                rank.getPermissions().stream().map(Lang.CLAIM_PERMISSION::getLocalized).map(Placeholders::goodEntry).collect(Collectors.joining("\n")))
            ;
    }

    @NotNull
    public static PlaceholderMap forFlag(@NotNull Flag flag) {
        return new PlaceholderMap()
            .add(FLAG_ID, flag::getId)
            .add(FLAG_NAME, flag::getDisplayName)
            .add(FLAG_DESCRIPTION, () -> String.join("\n", flag.getDescription()))
            ;
    }
}
