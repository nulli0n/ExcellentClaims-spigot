package su.nightexpress.excellentclaims.land;

import java.util.Comparator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.core.claim.StandardClaimRepository;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

public class LandsRepository extends StandardClaimRepository<LandClaim> {

    public @Nullable LandClaim getPrioritizedClaim(Chunk chunk) {
        return this.getPrioritizedClaim(chunk.getWorld(), ChunkPos.from(chunk));
    }

    public @Nullable LandClaim getPrioritizedClaim(World world, ChunkPos chunkPos) {
        return this.getInChunk(world, chunkPos)
            .stream()
            .max(Comparator.comparingInt(LandClaim::getPriority))
            .orElse(null);
    }
}
