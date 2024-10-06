package su.nightexpress.excellentclaims.api.claim;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.ClaimFlag;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.DirectionalPos;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.util.placeholder.Placeholder;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Claim extends Placeholder {

    void save();

    void saveSettings();

    void saveMembers();

    void saveFlags();

    boolean isSaveRequired();

    void setSaveRequired(boolean saveRequired);

    @NotNull File getFile();

    boolean isEmpty();

    boolean isActive();

    default boolean isInactive() {
        return !this.isActive();
    }

    void reactivate();

    void activate(@NotNull World world);

    void deactivate(@NotNull World world);

    void deactivate();

    default boolean isInside(@NotNull Entity entity) {
        return this.isInside(entity.getLocation());
    }

    default boolean isInside(@NotNull Block block) {
        return this.isInside(block.getLocation());
    }

    default boolean isInside(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        return this.isInside(world, BlockPos.from(location));
    }

    default boolean isInside(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.isInside(world.getName(), blockPos);
    }

    boolean isInside(@NotNull String worldName, @NotNull BlockPos blockPos);

    boolean isInside(@NotNull BlockPos blockPos);

    boolean hasPermission(@NotNull Player player, @NotNull ClaimPermission permission);

    boolean teleport(@NotNull Player player);

    boolean teleport(@NotNull Player player, boolean force);




    @NotNull String getWorldName();

    @Nullable World getWorld();

    @NotNull ClaimType getType();

    @NotNull String getId();

    @NotNull String getDisplayName();

    void setDisplayName(@NotNull String displayName);

    @Nullable String getDescription();

    void setDescription(@Nullable String description);

    int getPriority();

    void setPriority(int priority);

    @NotNull ItemStack getIcon();

    void setIcon(@NotNull ItemStack icon);

    @NotNull DirectionalPos getSpawnLocation();

    void setSpawnLocation(@NotNull DirectionalPos spawnLocation);



    @NotNull UserInfo getOwner();

    void setOwner(@NotNull Player player);

    void setOwner(@NotNull UserInfo owner);

    @NotNull
    default UUID getOwnerId() {
        return this.getOwner().getPlayerId();
    }

    @NotNull
    default String getOwnerName() {
        return this.getOwner().getPlayerName();
    }

    default boolean isOwnerOrMember(@NotNull Player player) {
        return this.isOwnerOrMember(player.getUniqueId());
    }

    default boolean isOwnerOrMember(@NotNull UUID playerId) {
        return this.isOwner(playerId) || this.isMember(playerId);
    }

    default boolean isOwner(@NotNull Player player) {
        return this.isOwner(player.getUniqueId());
    }

    boolean isOwner(@NotNull UUID playerId);




    @NotNull Map<UUID, Member> getMemberMap();

    @NotNull Set<Member> getMembers();

    default boolean isMember(@NotNull Player player) {
        return this.isMember(player.getUniqueId());
    }

    boolean isMember(@NotNull UUID playerId);

    void addMember(@NotNull Player player);

    void addMember(@NotNull Member member);

    void removeMember(@NotNull Player player);

    void removeMember(@NotNull Member member);

    void removeMember(@NotNull UUID playerId);

    @Nullable
    default Member getMember(@NotNull Player player) {
        return this.getMember(player.getUniqueId());
    }

    @Nullable Member getMember(@NotNull UUID playerId);

    @Nullable
    default MemberRank getMemberRank(@NotNull Player player) {
        return this.getMemberRank(player.getUniqueId());
    }

    @Nullable MemberRank getMemberRank(@NotNull UUID playerId);



    @NotNull Map<String, FlagValue> getFlags();

    boolean hasFlag(@NotNull Flag flag);

    <T> void setFlag(@NotNull ClaimFlag<T> flag, @NotNull T value);

    @NotNull <T> FlagValue getFlagValue(@NotNull ClaimFlag<T> flag);

    @NotNull <T> T getFlag(@NotNull ClaimFlag<T> flag);

    @NotNull <T> T getFlag(@NotNull ClaimFlag<T> flag, @NotNull T defaultValue);

    @NotNull <T> T getFlag(@NotNull String name, @NotNull Class<T> clazz, @NotNull T defaultValue);
}
