package su.nightexpress.excellentclaims.api.claim;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;

import java.util.Set;

public interface LandClaim extends Claim {

    boolean contains(@NotNull ChunkPos chunkPos);

    boolean isMerged();

    boolean isSingle();

    int getChunksAmount();

    @NotNull Set<ChunkPos> getPositions();

    @NotNull Set<Chunk> getChunks();
}
