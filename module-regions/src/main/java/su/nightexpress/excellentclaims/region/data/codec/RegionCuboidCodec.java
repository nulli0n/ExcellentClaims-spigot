package su.nightexpress.excellentclaims.region.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.data.model.RegionCuboid;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class RegionCuboidCodec implements ConfigCodec<RegionCuboid> {

    @Override
    public RegionCuboid read(FileConfig config, String path) throws CodecReadException {
        BlockPos minPos = BlockPos.read(config, path + ".lowest");
        BlockPos maxPos = BlockPos.read(config, path + ".highest");

        Cuboid cuboid = new Cuboid(minPos, maxPos);

        return new RegionCuboid(cuboid);
    }

    @Override
    public void write(FileConfig config, String path, RegionCuboid value) {
        Cuboid cuboid = value.getCuboid();

        config.set(path + ".lowest", cuboid.getMin());
        config.set(path + ".highest", cuboid.getMax());
    }
}
