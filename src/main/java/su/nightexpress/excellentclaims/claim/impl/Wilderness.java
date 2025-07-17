package su.nightexpress.excellentclaims.claim.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class Wilderness extends AbstractClaim {

    public static final String ID = "_wilderness_";

    public Wilderness(@NotNull ClaimPlugin plugin, @NotNull File file) {
        super(plugin, ClaimType.REGION, file);
        this.setOwner(new UserInfo(UUID.randomUUID(), Lang.WILDERNESS_OWNER_NAME.getString()));
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.CLAIM.replacer(this);
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.loadWorldInfo(config);
        this.loadFlags(config);
        this.setDisplayName(Lang.WILDERNESS_DISPLAY_NAME.getString());
        this.setDescription("");
        this.setPriority(0);
        this.setIcon(new NightItem(Material.GRASS_BLOCK));
        this.setSpawnLocation(ExactPos.empty());

        return this.loadAdditional(config);
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        this.writeWorldInfo(config);
        this.writeFlags(config);
        this.saveAdditional(config);
    }

    @Override
    protected boolean loadAdditional(@NotNull FileConfig config) {
        return true;
    }

    @Override
    protected void saveAdditional(@NotNull FileConfig config) {

    }

    @Override
    protected boolean contains(@NotNull BlockPos blockPos) {
        return true;
    }

    @Override
    @NotNull
    public Set<ChunkPos> getEffectiveChunkPositions() {
        return Collections.emptySet();
    }

    @Override
    public boolean isIntersecting(@NotNull Cuboid cuboid) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isWilderness() {
        return true;
    }

    @Override
    public boolean isOwner(@NotNull UUID playerId) {
        return false;
    }

    @Override
    public boolean isMember(@NotNull UUID playerId) {
        return false;
    }

    @Override
    @NotNull
    public ExactPos getSpawnLocation() {
        return this.world == null ? ExactPos.empty() : ExactPos.from(this.world.getSpawnLocation());
    }
}
