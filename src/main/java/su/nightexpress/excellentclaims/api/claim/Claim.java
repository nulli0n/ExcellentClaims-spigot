package su.nightexpress.excellentclaims.api.claim;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.excellentclaims.util.list.SmartList;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public interface Claim {

    @NotNull UnaryOperator<String> replacePlaceholders();

    void save();

    void saveSettings();

    void saveMembers();

    void saveFlags();

    boolean isSaveRequired();

    void setSaveRequired(boolean saveRequired);

    @NotNull File getFile();

    boolean isEmpty();

    boolean isActive();

    boolean isInactive();

    boolean isWilderness();

    void reactivate();

    void activate(@NotNull World world);

    void deactivate(@NotNull World world);

    void deactivate();

    boolean isInside(@NotNull Entity entity);

    boolean isInside(@NotNull Block block);

    boolean isInside(@NotNull Location location);

    boolean isInside(@NotNull World world, @NotNull BlockPos blockPos);

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

    @NotNull NightItem getIcon();

    void setIcon(@NotNull NightItem icon);

    @NotNull ExactPos getSpawnLocation();

    void setSpawnLocation(@NotNull ExactPos spawnLocation);

    @NotNull Set<ChunkPos> getEffectiveChunkPositions();

    boolean isIntersecting(@NotNull Cuboid cuboid);



    @NotNull UserInfo getOwner();

    void setOwner(@NotNull Player player);

    void setOwner(@NotNull UserInfo owner);

    @NotNull UUID getOwnerId();

    @NotNull String getOwnerName();

    boolean isOwnerOrMember(@NotNull Player player);

    boolean isOwnerOrMember(@NotNull UUID playerId);

    boolean isOwner(@NotNull Player player);

    boolean isOwner(@NotNull UUID playerId);




    @NotNull Map<UUID, Member> getMemberMap();

    @NotNull Set<Member> getMembers();

    boolean isMember(@NotNull Player player);

    boolean isMember(@NotNull UUID playerId);

    void addMember(@NotNull Player player);

    void addMember(@NotNull Member member);

    void removeMember(@NotNull Player player);

    void removeMember(@NotNull Member member);

    void removeMember(@NotNull UUID playerId);

    @Nullable Member getMember(@NotNull Player player);

    @Nullable Member getMember(@NotNull UUID playerId);

    @Nullable MemberRank getMemberRank(@NotNull Player player);

    @Nullable MemberRank getMemberRank(@NotNull UUID playerId);



    @NotNull SmartList<EntityType> getMobSpawnList();

    @NotNull SmartList<EntityType> getMobInteractList();

    @NotNull SmartList<Material> getBlockUsageList();

    @NotNull SmartList<DamageType> getAnimalDamageList();

    @NotNull SmartList<DamageType> getPlayerDamageList();

    @NotNull SmartList<Command> getCommandUsageList();



    @NotNull Map<String, FlagValue> getFlags();

    <T> boolean hasFlag(@NotNull Flag<T> flag);

    <T> void setFlag(@NotNull Flag<T> flag, @NotNull T value);

    @NotNull <T> FlagValue getFlagValue(@NotNull Flag<T> flag);

    @NotNull <T> T getFlag(@NotNull Flag<T> flag);

    @NotNull <T> T getFlag(@NotNull Flag<T> flag, @NotNull T defaultValue);



    boolean canMobSpawn(@NotNull EntityType type);

    boolean canUseMob(@NotNull EntityType type);

    boolean canUseBlock(@NotNull Material blockType);

    boolean isAnimalDamageAllowed(@NotNull DamageType type);

    boolean isPlayerDamageAllowed(@NotNull DamageType type);

    boolean isCommandAllowed(@NotNull Command command);
}
