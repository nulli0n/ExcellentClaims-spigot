package su.nightexpress.excellentclaims.claim.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.DirectionalPos;
import su.nightexpress.nightcore.config.FileConfig;

import java.io.File;
import java.util.UUID;

public class Wilderness extends AbstractClaim {

    public static final String ID = "_wilderness_";

    public Wilderness(@NotNull ClaimPlugin plugin, @NotNull File file) {
        super(plugin, ClaimType.REGION, file);
        this.setOwner(new UserInfo(UUID.randomUUID(), Lang.WILDERNESS_OWNER_NAME.getString()));
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.loadWorldInfo(config);
        this.loadFlags(config);
        this.setDisplayName(Lang.WILDERNESS_DISPLAY_NAME.getString());
        this.setDescription("");
        this.setPriority(0);
        this.setIcon(new ItemStack(Material.GRASS_BLOCK));
        this.setSpawnLocation(DirectionalPos.empty());

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
    public DirectionalPos getSpawnLocation() {
        return this.world == null ? DirectionalPos.empty() : DirectionalPos.from(this.world.getSpawnLocation());
    }
}
