package su.nightexpress.excellentclaims.rank;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.rank.codec.RankDefinitionCodec;
import su.nightexpress.excellentclaims.rank.model.RankDefinition;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class RanksCodecRegistrar {

    private RanksCodecRegistrar() {
    }

    public static void registerCodecs() {
        ConfigCodecs.register(RankDefinition.class, new RankDefinitionCodec());
    }
}
