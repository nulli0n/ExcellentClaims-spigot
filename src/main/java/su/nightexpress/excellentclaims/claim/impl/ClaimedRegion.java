package su.nightexpress.excellentclaims.claim.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.Cuboid;
import su.nightexpress.nightcore.config.FileConfig;

import java.io.File;

public class ClaimedRegion extends AbstractClaim implements RegionClaim {

    private Cuboid cuboid;

    public ClaimedRegion(@NotNull ClaimPlugin plugin, @NotNull File file) {
        super(plugin, ClaimType.REGION, file);
    }

    @Override
    protected boolean loadAdditional(@NotNull FileConfig config) {
        BlockPos minPos = BlockPos.read(config, "Data.MinPos");
        BlockPos maxPos = BlockPos.read(config, "Data.MaxPos");
        this.setCuboid(minPos, maxPos);

        return true;
    }

    @Override
    protected void saveAdditional(@NotNull FileConfig config) {
        this.cuboid.getMin().write(config, "Data.MinPos");
        this.cuboid.getMax().write(config, "Data.MaxPos");
    }

    @Override
    protected void writeSettings(@NotNull FileConfig config) {
        super.writeSettings(config);

    }

    @Override
    public boolean isEmpty() {
        return this.cuboid.isEmpty();
    }

    @Override
    protected boolean contains(@NotNull BlockPos blockPos) {
        return !this.isEmpty() && this.cuboid.contains(blockPos);
    }

    @NotNull
    @Override
    public Cuboid getCuboid() {
        return cuboid;
    }

    @Override
    public void setCuboid(@NotNull BlockPos min, @NotNull BlockPos max) {
        this.setCuboid(new Cuboid(min, max));
    }

    @Override
    public void setCuboid(@NotNull Cuboid cuboid) {
        this.cuboid = cuboid;
    }
}
