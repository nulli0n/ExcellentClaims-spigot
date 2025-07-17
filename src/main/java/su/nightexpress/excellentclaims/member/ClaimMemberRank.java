package su.nightexpress.excellentclaims.member;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

public class ClaimMemberRank implements MemberRank {

    private final String               id;
    private final String               displayName;
    private final int                  priority;
    private final Set<ClaimPermission> permissions;

    public ClaimMemberRank(@NotNull String id, @NotNull String displayName, int priority, @NotNull Set<ClaimPermission> permissions) {
        this.id = id.toLowerCase();
        this.displayName = displayName;
        this.priority = priority;
        this.permissions = permissions;
    }

    @NotNull
    public static List<ClaimMemberRank> getDefaultRanks() {
        List<ClaimMemberRank> list = new ArrayList<>();

        list.add(new ClaimMemberRank("member", "Member", 1, Lists.newSet(
            ClaimPermission.BLOCK_INTERACT, ClaimPermission.VIEW_MEMBERS, ClaimPermission.TELEPORT))
        );

        list.add(new ClaimMemberRank("trusted", "Trusted", 5, Lists.newSet(
            ClaimPermission.BUILDING, ClaimPermission.CONTAINERS, ClaimPermission.VIEW_MEMBERS, ClaimPermission.TELEPORT,
            ClaimPermission.BLOCK_INTERACT, ClaimPermission.ENTITY_INTERACT))
        );

        list.add(new ClaimMemberRank("officer", "Officer", 10, Lists.newSet(
            ClaimPermission.BUILDING, ClaimPermission.CONTAINERS, ClaimPermission.VIEW_MEMBERS, ClaimPermission.TELEPORT,
            ClaimPermission.BLOCK_INTERACT, ClaimPermission.ENTITY_INTERACT,
            ClaimPermission.ADD_MEMBERS, ClaimPermission.REMOVE_MEMBERS, ClaimPermission.MANAGE_MEMBERS))
        );

        list.add(new ClaimMemberRank("owner", "Owner", 100, Lists.newSet(
            ClaimPermission.ALL))
        );

        return list;
    }

    @NotNull
    public static List<ClaimMemberRank> getDummyRanks() {
        List<ClaimMemberRank> list = new ArrayList<>();

        list.add(new ClaimMemberRank("member", "Member", 1, Lists.newSet(
            ClaimPermission.BUILDING, ClaimPermission.BLOCK_INTERACT, ClaimPermission.TELEPORT))
        );

        list.add(new ClaimMemberRank("owner", "Owner", 100, Lists.newSet(
            ClaimPermission.ALL))
        );

        return list;
    }

    @NotNull
    public static ClaimMemberRank read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        String displayName = config.getString(path + ".DisplayName", StringUtil.capitalizeUnderscored(id));
        int priority = config.getInt(path + ".Priority");

        Set<ClaimPermission> permissions = Lists.modify(config.getStringSet(path + ".Permissions"), name -> Enums.get(name, ClaimPermission.class));
        permissions.removeIf(Objects::isNull);

        return new ClaimMemberRank(id, displayName, priority, permissions);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".DisplayName", this.displayName);
        config.set(path + ".Priority", this.priority);
        config.set(path + ".Permissions", Lists.modify(this.permissions, Enum::name));
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.MEMBER_RANK.replacer(this);
    }

    @Override
    public boolean hasPermission(@NotNull ClaimPermission permission) {
        return this.permissions.contains(ClaimPermission.ALL) || this.permissions.contains(permission);
    }

    @Override
    public boolean isAbove(@NotNull MemberRank other) {
        return this.priority > other.getPriority();
    }

    @Override
    public boolean isBehind(@NotNull MemberRank other) {
        return this.priority < other.getPriority();
    }

    @NotNull
    @Override
    public String getId() {
        return this.id;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public Set<ClaimPermission> getPermissions() {
        return this.permissions;
    }
}
