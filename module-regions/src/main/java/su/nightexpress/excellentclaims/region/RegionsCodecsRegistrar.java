package su.nightexpress.excellentclaims.region;

import su.nightexpress.excellentclaims.region.data.codec.RegionCuboidCodec;
import su.nightexpress.excellentclaims.region.data.model.RegionCuboid;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public final class RegionsCodecsRegistrar {

    private RegionsCodecsRegistrar() {
    }

    public static void register() {
        ConfigCodecs.register(RegionCuboid.class, new RegionCuboidCodec());
    }
}
