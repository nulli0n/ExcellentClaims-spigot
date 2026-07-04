package su.nightexpress.excellentclaims.land.data.codec;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.land.data.model.LandChunkData;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class LandChunkDataCodec implements ConfigCodec<LandChunkData> {

    @Override
    public LandChunkData read(FileConfig config, String path) throws CodecReadException {
        Set<String> rawPositions = config.getStringSet(path + ".Chunks");
        Set<ChunkPos> chunkPositions = rawPositions.stream()
            .map(raw -> ChunkPos.deserialize(raw))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        return new LandChunkData(chunkPositions);
    }

    @Override
    public void write(FileConfig config, String path, LandChunkData value) {
        Set<String> rawPositions = value.getChunkPositions().stream()
            .map(ChunkPos::serialize)
            .collect(Collectors.toSet());

        config.set(path + ".Chunks", rawPositions);
    }
}
