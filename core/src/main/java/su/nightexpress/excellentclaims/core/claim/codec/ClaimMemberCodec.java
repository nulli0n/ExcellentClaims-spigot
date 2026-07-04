package su.nightexpress.excellentclaims.core.claim.codec;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ClaimMemberCodec implements ConfigCodec<DefaultClaimMember> {

    @Override
    public @Nullable DefaultClaimMember read(FileConfig config, String path) throws CodecReadException {
        UUID playerId = config.get(path + ".UUID", ConfigCodecs.UUID);
        if (playerId == null) {
            throw new CodecReadException("Invalid UUID");
        }

        String rankId = config.getOrSet(path + ".Rank", ConfigCodecs.STRING, "");
        Identifier key = Identifier.parse(rankId).orElse(null);
        if (key == null) {
            throw new CodecReadException("Invalid rank identifier");
        }

        return new DefaultClaimMember(playerId, key);
    }

    @Override
    public void write(FileConfig config, String path, DefaultClaimMember value) {
        config.set(path + ".UUID", value.getPlayerId().toString());
        config.set(path + ".Rank", value.getRank().value());
    }
}
