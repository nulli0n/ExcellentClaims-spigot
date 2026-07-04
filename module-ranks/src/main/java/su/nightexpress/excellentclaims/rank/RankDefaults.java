package su.nightexpress.excellentclaims.rank;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.rank.model.DefaultRankDefinition;

public final class RankDefaults {

    private RankDefaults() {
    }

    public static List<DefaultRank> getDefaultRanks() {
        List<DefaultRank> list = new ArrayList<>();

        Set<ClaimPermission> memberPerms = EnumSet.of(
            ClaimPermission.BLOCK_INTERACT,
            ClaimPermission.ITEM_INTERACT,
            ClaimPermission.DROP_ITEMS,
            ClaimPermission.PICKUP_ITEMS,
            ClaimPermission.VIEW_MEMBERS,
            ClaimPermission.TELEPORT
        );

        Set<ClaimPermission> trustedPerms = EnumSet.of(
            ClaimPermission.BUILDING,
            ClaimPermission.CONTAINERS,
            ClaimPermission.CHEST_ACCESS,
            ClaimPermission.DAMAGE_MOBS,
            ClaimPermission.ENTITY_INTERACT,
            ClaimPermission.USE_COMMANDS,
            ClaimPermission.USE_PORTALS
        );

        Set<ClaimPermission> moderPerms = EnumSet.of(
            ClaimPermission.MANAGE_CLAIM,
            ClaimPermission.MANAGE_MEMBERS,
            ClaimPermission.ADD_MEMBERS,
            ClaimPermission.REMOVE_MEMBERS,
            ClaimPermission.DEMOTE_MEMBERS,
            ClaimPermission.PROMOTE_MEMBERS
        );

        Set<ClaimPermission> ownerPerms = EnumSet.of(
            ClaimPermission.ADMIN
        );

        DefaultRankDefinition member = new DefaultRankDefinition("Member", 1, memberPerms);
        DefaultRankDefinition trusted = new DefaultRankDefinition("Trusted", 10, trustedPerms);
        DefaultRankDefinition moderator = new DefaultRankDefinition("Moderator", 25, moderPerms);
        DefaultRankDefinition owner = new DefaultRankDefinition("Owner", 100, ownerPerms);

        list.add(new DefaultRank(Identifier.of("member"), member));
        list.add(new DefaultRank(Identifier.of("trusted"), trusted));
        list.add(new DefaultRank(Identifier.of("moderator"), moderator));
        list.add(new DefaultRank(Identifier.of("owner"), owner));

        return list;
    }
}
