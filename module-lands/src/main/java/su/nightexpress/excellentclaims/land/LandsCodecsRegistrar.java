package su.nightexpress.excellentclaims.land;

import su.nightexpress.excellentclaims.land.data.codec.LandChunkDataCodec;
import su.nightexpress.excellentclaims.land.data.model.LandChunkData;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public final class LandsCodecsRegistrar {

    private LandsCodecsRegistrar() {
    }

    public static void register() {
        ConfigCodecs.register(LandChunkData.class, new LandChunkDataCodec());
    }
}
