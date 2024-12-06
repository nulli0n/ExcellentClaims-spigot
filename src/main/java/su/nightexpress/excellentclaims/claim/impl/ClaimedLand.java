package su.nightexpress.excellentclaims.claim.impl;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;
import su.nightexpress.nightcore.config.FileConfig;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ClaimedLand extends AbstractClaim implements LandClaim {

    private final Set<ChunkPos> positions;

    public ClaimedLand(@NotNull ClaimPlugin plugin, @NotNull File file) {
        super(plugin, ClaimType.CHUNK, file);
        this.positions = new HashSet<>();
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.LAND_CLAIM.replacer(this);
    }

    @Override
    protected boolean loadAdditional(@NotNull FileConfig config) {
        config.getSection("Data.ChunkPos").forEach(sId -> {
            this.positions.add(ChunkPos.read(config, "Data.ChunkPos." + sId));
        });
        if (this.positions.isEmpty()) {
            this.plugin.error("No chunks present in the '" + this.getFile().getPath() + "' claim!");
            return false;
        }

        return true;
    }

    @Override
    protected void saveAdditional(@NotNull FileConfig config) {
        config.remove("Data.ChunkPos");
        int i = 0;
        for (ChunkPos chunkPos : this.positions) {
            chunkPos.write(config, "Data.ChunkPos." + (i++));
        }
    }

    @Override
    protected boolean contains(@NotNull BlockPos blockPos) {
        return this.positions.contains(ChunkPos.from(blockPos));
    }

    @Override
    public boolean contains(@NotNull ChunkPos chunkPos) {
        return this.positions.contains(chunkPos);
    }

    @Override
    public boolean isEmpty() {
        return this.positions.isEmpty();
    }

    @Override
    public boolean isMerged() {
        return this.getChunksAmount() > 1;
    }

    @Override
    public boolean isSingle() {
        return this.getChunksAmount() == 1;
    }

    @Override
    public int getChunksAmount() {
        return this.positions.size();
    }

    @Override
    @NotNull
    public Set<ChunkPos> getPositions() {
        return this.positions;
    }

    @Override
    @NotNull
    public Set<Chunk> getChunks() {
        return this.world == null ? Collections.emptySet() : this.positions.stream().map(pos -> pos.getChunk(this.world)).collect(Collectors.toSet());
    }
}
