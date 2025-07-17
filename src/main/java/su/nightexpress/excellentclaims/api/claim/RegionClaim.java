package su.nightexpress.excellentclaims.api.claim;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

public interface RegionClaim extends Claim {

    @NotNull
    Cuboid getCuboid();

    void setCuboid(@NotNull BlockPos min, @NotNull BlockPos max);

    void setCuboid(@NotNull Cuboid cuboid);
}
