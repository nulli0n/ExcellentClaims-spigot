package su.nightexpress.excellentclaims.claim.impl;

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
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.FlagRegistry;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.excellentclaims.member.ClaimMember;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.excellentclaims.util.list.ListMode;
import su.nightexpress.excellentclaims.util.list.ListTypes;
import su.nightexpress.excellentclaims.util.list.SmartList;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractClaim extends AbstractFileData<ClaimPlugin> implements Claim {

    protected final ClaimType              type;
    protected final Map<UUID, Member>      members;
    protected final Map<String, FlagValue> flags;

    private boolean active;
    private boolean saveRequired;

    protected String         worldName;
    protected World          world;
    protected UserInfo       owner;
    protected String         displayName;
    protected String         description;
    protected int            priority;
    protected NightItem      icon;
    protected ExactPos spawnLocation;

    protected final SmartList<EntityType> mobSpawnList;
    protected final SmartList<EntityType> mobInteractList;
    protected final SmartList<Material> blockUsageList;
    protected final SmartList<DamageType> animalDamageList;
    protected final SmartList<DamageType> playerDamageList;
    protected final SmartList<Command>    commandUsageList;

    public AbstractClaim(@NotNull ClaimPlugin plugin, @NotNull ClaimType type, @NotNull File file) {
        super(plugin, file);
        this.type = type;
        this.members = new HashMap<>();
        this.flags = new HashMap<>();

        this.mobSpawnList = SmartList.empty(ListMode.BLACKLIST, ListTypes.SPAWNABLE_MOB);
        this.mobInteractList = SmartList.empty(ListMode.BLACKLIST, ListTypes.USABLE_MOB);
        this.blockUsageList = SmartList.empty(ListMode.BLACKLIST, ListTypes.USABLE_BLOCK);
        this.animalDamageList = SmartList.empty(ListMode.BLACKLIST, ListTypes.DAMAGE_TYPE);
        this.playerDamageList = SmartList.empty(ListMode.BLACKLIST, ListTypes.DAMAGE_TYPE);
        this.commandUsageList = SmartList.rawValues(ListMode.BLACKLIST, ListTypes.COMMAND, Config.GENERAL_DEFAULT_BANNED_COMMANDS.get());
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.loadWorldInfo(config);

        UserInfo owner = UserInfo.read(config, "Data.Owner");
        if (owner == null) {
            this.plugin.error("Invalid owner UUID in '" + this.getFile().getPath() + "' claim.");
            return false;
        }
        this.setOwner(owner);

        config.getSection("Data.Members").forEach(id -> {
            ClaimMember member = ClaimMember.read(config, "Data.Members." + id, id);
            if (member == null) return;

            this.members.put(member.getPlayerId(), member);
        });

        this.setSpawnLocation(ExactPos.read(config, "Settings.SpawnPos"));
        this.setDisplayName(ConfigValue.create("Settings.DisplayName", StringUtil.capitalizeUnderscored(this.getId())).read(config));
        this.setDescription(config.getString("Settings.Description"));
        this.setPriority(config.getInt("Settings.Priority", 0));
        this.setIcon(config.getCosmeticItem("Settings.Icon"));

        this.mobSpawnList.load(config, "Settings.Advanced.MobSpawnList");
        this.mobInteractList.load(config, "Settings.Advanced.MobInteractList");
        this.blockUsageList.load(config, "Settings.Advanced.BlockUsageList");
        this.animalDamageList.load(config, "Settings.Advanced.AnimalDamageList");
        this.playerDamageList.load(config, "Settings.Advanced.PlayerDamageList");
        this.commandUsageList.load(config, "Settings.Advanced.CommandUsageList");

        this.loadFlags(config);

        return this.loadAdditional(config);
    }

    protected void loadWorldInfo(@NotNull FileConfig config) {
        this.setWorldName(config.getString("Data.World", "null"));
    }

    protected void loadFlags(@NotNull FileConfig config) {
        config.getSection("Flags").forEach(flagName -> {
            ClaimFlag<?> flag = FlagRegistry.getFlag(flagName);
            if (flag == null) {
                this.plugin.warn("Unknown flag '" + flagName + "' in '" + this.getFile().getPath() + "' claim. Skipping...");
                return;
            }

            FlagValue value = flag.parse(config, "Flags." + flag.getId());
            this.flags.put(flag.getId(), value);
        });
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        this.writeWorldInfo(config);
        this.writeMembers(config);
        this.writeSettings(config);
        this.writeFlags(config);

        this.saveAdditional(config);
    }

    @Override
    public void saveSettings() {
        this.writeSection(this::writeSettings);
    }

    @Override
    public void saveMembers() {
        this.writeSection(this::writeMembers);
    }

    @Override
    public void saveFlags() {
        this.writeSection(this::writeFlags);
    }

    private void writeSection(@NotNull Consumer<FileConfig> consumer) {
        FileConfig config = this.getConfig();
        consumer.accept(config);
        config.saveChanges();
    }

    protected void writeWorldInfo(@NotNull FileConfig config) {
        config.set("Data.World", this.worldName);
    }

    protected void writeSettings(@NotNull FileConfig config) {
        this.spawnLocation.write(config, "Settings.SpawnPos");
        config.set("Settings.DisplayName", this.displayName);
        config.set("Settings.Description", this.description);
        config.set("Settings.Priority", this.priority);
        config.set("Settings.Icon", this.icon);

        config.set("Settings.Advanced.MobSpawnList", this.mobSpawnList);
        config.set("Settings.Advanced.MobInteractList", this.mobInteractList);
        config.set("Settings.Advanced.BlockUsageList", this.blockUsageList);
        config.set("Settings.Advanced.AnimalDamageList", this.animalDamageList);
        config.set("Settings.Advanced.PlayerDamageList", this.playerDamageList);
        config.set("Settings.Advanced.CommandUsageList", this.commandUsageList);
    }

    protected void writeMembers(@NotNull FileConfig config) {
        this.owner.write(config, "Data.Owner");
        config.remove("Data.Members");
        this.members.values().forEach(member -> member.write(config, "Data.Members." + member.getPlayerId()));
    }

    protected void writeFlags(@NotNull FileConfig config) {
        config.remove("Flags");
        this.flags.forEach((flagName, value) -> {
            value.write(config, "Flags." + flagName);
        });
    }

    protected abstract boolean loadAdditional(@NotNull FileConfig config);

    protected abstract void saveAdditional(@NotNull FileConfig config);

    protected abstract boolean contains(@NotNull BlockPos blockPos);

    @Override
    public boolean isInside(@NotNull Entity entity) {
        return this.isInside(entity.getLocation());
    }

    @Override
    public boolean isInside(@NotNull Block block) {
        return this.isInside(block.getLocation());
    }

    @Override
    public boolean isInside(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        return this.isInside(world, BlockPos.from(location));
    }

    @Override
    public boolean isInside(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.isInside(world.getName(), blockPos);
    }

    @Override
    public boolean isInside(@NotNull String worldName, @NotNull BlockPos blockPos) {
        return worldName.equalsIgnoreCase(this.worldName) && this.isInside(blockPos);
    }

    @Override
    public boolean isInside(@NotNull BlockPos blockPos) {
        return this.contains(blockPos);
    }

    @Override
    public boolean hasPermission(@NotNull Player player, @NotNull ClaimPermission permission) {
        if (plugin.getMemberManager().isAdminMode(player)) return true;

        MemberRank rank = this.getMemberRank(player);
        return rank != null && rank.hasPermission(permission);
    }

    @Override
    public boolean teleport(@NotNull Player player) {
        return this.teleport(player, false);
    }

    @Override
    public boolean teleport(@NotNull Player player, boolean force) {
        World world = this.getWorld();
        if (world == null) return false;

        Location location = this.getSpawnLocation().toLocation(world);

        if (!force) {
            if (!this.isOwner(player) && !ClaimUtils.isSafeLocation(location)) {
                Lang.CLAIM_TELEPORT_ERROR_UNSAFE.getMessage().send(player, replacer -> replacer.replace(this.replacePlaceholders()));
                return false;
            }
        }

        location = LocationUtil.setCenter2D(location);

        if (!player.teleport(location)) {
            return false;
        }

        Lang.CLAIM_TELEPORT_SUCCESS.getMessage().send(player, replacer -> replacer.replace(this.replacePlaceholders()));
        return true;
    }

    @Override
    public boolean isActive() {
        return this.active && this.world != null;
    }

    @Override
    public boolean isInactive() {
        return !this.isActive();
    }

    @Override
    public boolean isWilderness() {
        return false;
    }

    @Override
    public boolean isSaveRequired() {
        return this.saveRequired;
    }

    @Override
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }

    @Override
    @Nullable
    public World getWorld() {
        return this.isActive() ? this.world : null;
    }

    @Override
    public void reactivate() {
        World world = this.plugin.getServer().getWorld(this.worldName);
        if (world == null) {
            this.deactivate();
        }
        else if (this.isInactive()) {
            this.activate(world);
        }
    }

    @Override
    public void activate(@NotNull World world) {
        if (this.worldName.equalsIgnoreCase(world.getName())) {
            this.world = world;
            this.active = true;
            //this.plugin.debug("Claim activated: " + this.getId() + " in " + this.worldName);
        }
    }

    @Override
    public void deactivate(@NotNull World world) {
        if (this.worldName.equalsIgnoreCase(world.getName())) {
            this.deactivate();
        }
    }

    @Override
    public void deactivate() {
        this.world = null;
        this.active = false;
        //this.plugin.debug("Claim deactivated: " + this.getId() + " in " + this.worldName);
    }

    @Override
    public boolean isOwner(@NotNull UUID playerId) {
        return this.getOwnerId().equals(playerId);
    }

    @NotNull
    @Override
    public UUID getOwnerId() {
        return this.owner.getPlayerId();
    }

    @NotNull
    @Override
    public String getOwnerName() {
        return this.owner.getPlayerName();
    }

    @Override
    public boolean isOwnerOrMember(@NotNull Player player) {
        return this.isOwnerOrMember(player.getUniqueId());
    }

    @Override
    public boolean isOwnerOrMember(@NotNull UUID playerId) {
        return this.isOwner(playerId) || this.isMember(playerId);
    }

    @Override
    public boolean isOwner(@NotNull Player player) {
        return this.isOwner(player.getUniqueId());
    }

    @Override
    public void setOwner(@NotNull UserInfo owner) {
        this.owner = owner;
    }

    @Override
    public void setOwner(@NotNull Player player) {
        this.setOwner(UserInfo.of(player));
    }

    @Override
    public boolean isMember(@NotNull Player player) {
        return this.isMember(player.getUniqueId());
    }

    @Override
    public boolean isMember(@NotNull UUID playerId) {
        return this.getMember(playerId) != null;
    }

    @Override
    public void addMember(@NotNull Player player) {
        MemberRank rank = this.plugin.getMemberManager().getLowestRank();
        this.addMember(new ClaimMember(player, rank));
    }

    @Override
    public void addMember(@NotNull Member member) {
        if (this.isMember(member.getPlayerId())) return;

        this.members.put(member.getPlayerId(), member);
    }

    @Override
    public void removeMember(@NotNull Player player) {
        this.removeMember(player.getUniqueId());
    }

    @Override
    public void removeMember(@NotNull Member member) {
        this.removeMember(member.getPlayerId());
    }

    @Override
    public void removeMember(@NotNull UUID playerId) {
        this.members.remove(playerId);
    }

    @Override
    @Nullable
    public Member getMember(@NotNull Player player) {
        return this.getMember(player.getUniqueId());
    }

    @Override
    @Nullable
    public Member getMember(@NotNull UUID playerId) {
        return this.members.get(playerId);
    }

    @Override
    @Nullable
    public MemberRank getMemberRank(@NotNull Player player) {
        return this.getMemberRank(player.getUniqueId());
    }

    @Override
    @Nullable
    public MemberRank getMemberRank(@NotNull UUID playerId) {
        if (this.isOwner(playerId)) {
            return this.plugin.getMemberManager().getHighestRank();
        }

        Member member = this.getMember(playerId);
        return member == null ? null : member.getRank();
    }

    @Override
    public <T> boolean hasFlag(@NotNull Flag<T> flag) {
        return !this.isWilderness() || this.flags.containsKey(flag.getId());
    }

    @Override
    public <T> void setFlag(@NotNull Flag<T> flag, @NotNull T value) {
        this.flags.put(flag.getId(), flag.boxed(value));
    }

    @Override
    @NotNull
    public <T> FlagValue getFlagValue(@NotNull Flag<T> flag) {
        return this.flags.getOrDefault(flag.getId(), flag.boxed(flag.getDefaultValue()));
    }

    @Override
    @NotNull
    public <T> T getFlag(@NotNull Flag<T> flag) {
        return this.getFlag(flag, flag.getDefaultValue());
    }

    @Override
    @NotNull
    public <T> T getFlag(@NotNull Flag<T> flag, @NotNull T defaultValue) {
        FlagValue value = this.flags.get(flag.getId());
        if (value == null) return defaultValue;

        return flag.unboxed(value).orElse(defaultValue);
    }

    @Override
    public boolean canMobSpawn(@NotNull EntityType type) {
        return this.mobSpawnList.isAllowed(type);
    }

    @Override
    public boolean canUseMob(@NotNull EntityType type) {
        return this.mobInteractList.isAllowed(type);
    }

    @Override
    public boolean canUseBlock(@NotNull Material blockType) {
        return this.blockUsageList.isAllowed(blockType);
    }

    @Override
    public boolean isAnimalDamageAllowed(@NotNull DamageType type) {
        return this.animalDamageList.isAllowed(type);
    }

    @Override
    public boolean isPlayerDamageAllowed(@NotNull DamageType type) {
        return this.playerDamageList.isAllowed(type);
    }

    @Override
    public boolean isCommandAllowed(@NotNull Command command) {
        return this.commandUsageList.isAllowed(command);
    }

    @NotNull
    @Override
    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(@NotNull String worldName) {
        this.worldName = worldName;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Override
    @NotNull
    public ClaimType getType() {
        return this.type;
    }

    @NotNull
    @Override
    public UserInfo getOwner() {
        return this.owner;
    }

    @Override
    @NotNull
    public Map<UUID, Member> getMemberMap() {
        return this.members;
    }

    @NotNull
    @Override
    public Set<Member> getMembers() {
        return new HashSet<>(this.members.values());
    }

    @Override
    @NotNull
    public Map<String, FlagValue> getFlags() {
        return this.flags;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = Math.max(0, priority);
    }

    @NotNull
    @Override
    public NightItem getIcon() {
        return this.icon.copy();
    }

    @Override
    public void setIcon(@NotNull NightItem icon) {
        this.icon = icon.copy();
    }

    @NotNull
    @Override
    public ExactPos getSpawnLocation() {
        return this.spawnLocation;
    }

    @Override
    public void setSpawnLocation(@NotNull ExactPos spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    @NotNull
    @Override
    public SmartList<EntityType> getMobSpawnList() {
        return this.mobSpawnList;
    }

    @Override
    @NotNull
    public SmartList<EntityType> getMobInteractList() {
        return this.mobInteractList;
    }

    @Override
    @NotNull
    public SmartList<Material> getBlockUsageList() {
        return this.blockUsageList;
    }

    @Override
    @NotNull
    public SmartList<DamageType> getAnimalDamageList() {
        return this.animalDamageList;
    }

    @Override
    @NotNull
    public SmartList<DamageType> getPlayerDamageList() {
        return this.playerDamageList;
    }

    @Override
    @NotNull
    public SmartList<Command> getCommandUsageList() {
        return this.commandUsageList;
    }
}
