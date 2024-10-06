package su.nightexpress.excellentclaims.api.claim;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.Cuboid;

public interface RegionClaim extends Claim {

    @NotNull Cuboid getCuboid();

    void setCuboid(@NotNull BlockPos min, @NotNull BlockPos max);

    void setCuboid(@NotNull Cuboid cuboid);
}
