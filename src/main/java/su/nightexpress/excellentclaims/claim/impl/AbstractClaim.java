package su.nightexpress.excellentclaims.claim.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.flag.ClaimFlag;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.FlagRegistry;
import su.nightexpress.excellentclaims.member.ClaimMember;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.DirectionalPos;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

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
    protected DirectionalPos spawnLocation;

    public AbstractClaim(@NotNull ClaimPlugin plugin, @NotNull ClaimType type, @NotNull File file) {
        super(plugin, file);
        this.type = type;
        this.members = new HashMap<>();
        this.flags = new HashMap<>();
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

        this.setSpawnLocation(DirectionalPos.read(config, "Settings.SpawnPos"));
        this.setDisplayName(ConfigValue.create("Settings.DisplayName", StringUtil.capitalizeUnderscored(this.getId())).read(config));
        this.setDescription(config.getString("Settings.Description"));
        this.setPriority(config.getInt("Settings.Priority", 0));
        this.setIcon(config.getCosmeticItem("Settings.Icon"));

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
    }

    protected void writeMembers(@NotNull FileConfig config) {
        this.owner.write(config, "Data.Owner");
        config.remove("Data.Members");
        this.members.values().forEach(member -> member.write(config, "Data.Members." + member.getPlayerId()));
    }

    protected void writeFlags(@NotNull FileConfig config) {
        config.remove("Flags");
        this.flags.forEach((flagName, value) -> {
            Flag flag = value.getFlag();
            value.write(config, "Flags." + flag.getId());
        });
    }

    protected abstract boolean loadAdditional(@NotNull FileConfig config);

    protected abstract void saveAdditional(@NotNull FileConfig config);

    protected abstract boolean contains(@NotNull BlockPos blockPos);

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
        //if (player.hasPermission(Perms.BYPASS_RANK_PERMISSIONS)) return true;
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
            this.plugin.debug("Claim activated: " + this.getId() + " in " + this.worldName);
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
        this.plugin.debug("Claim deactivated: " + this.getId() + " in " + this.worldName);
    }

    @Override
    public boolean isOwner(@NotNull UUID playerId) {
        return this.getOwnerId().equals(playerId);
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
    public Member getMember(@NotNull UUID playerId) {
        return this.members.get(playerId);
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
    public boolean hasFlag(@NotNull Flag flag) {
        return !this.isWilderness() || this.flags.containsKey(flag.getId());
    }

    @Override
    public <T> void setFlag(@NotNull ClaimFlag<T> flag, @NotNull T value) {
        this.flags.put(flag.getId(), flag.asValue(value));
    }

    @Override
    @NotNull
    public <T> FlagValue getFlagValue(@NotNull ClaimFlag<T> flag) {
        return this.flags.getOrDefault(flag.getId(), flag.asDefaultValue());
    }

    @Override
    @NotNull
    public <T> T getFlag(@NotNull ClaimFlag<T> flag) {
        return this.getFlag(flag.getId(), flag.getValueType(), flag.getDefaultValue());
    }

    @Override
    @NotNull
    public <T> T getFlag(@NotNull ClaimFlag<T> flag, @NotNull T defaultValue) {
        return this.getFlag(flag.getId(), flag.getValueType(), defaultValue);
    }

    @Override
    @NotNull
    public <T> T getFlag(@NotNull String name, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        name = name.toLowerCase();

        FlagValue value = this.flags.get(name.toLowerCase());
        if (value == null) return defaultValue;

        Object result = value.getValue();// value.getValue();
        if (clazz.isAssignableFrom(result.getClass())) {
            return clazz.cast(result);
        }
        else {
            throw new IllegalArgumentException("Flag '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz.getSimpleName());
        }
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
    public DirectionalPos getSpawnLocation() {
        return this.spawnLocation;
    }

    @Override
    public void setSpawnLocation(@NotNull DirectionalPos spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
