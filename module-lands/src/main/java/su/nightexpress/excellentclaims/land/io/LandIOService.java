package su.nightexpress.excellentclaims.land.io;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractOwnableClaimIOSerivce;
import su.nightexpress.excellentclaims.land.data.model.LandChunkData;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.data.model.LandClaimBuilder;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;

@NullMarked
public class LandIOService extends AbstractOwnableClaimIOSerivce<LandClaim, LandClaimBuilder> {

    public LandIOService(Logger logger, Path directory, Identifier domain) {
        super(logger, directory, domain);
    }

    @Override
    protected void createOwnableData(LandClaimBuilder builder) {
        builder.chunkData(new LandChunkData());
    }

    @Override
    protected LandClaimBuilder createBuilder(DefaultClaimIdentity identity) {
        return new LandClaimBuilder(identity);
    }

    @Override
    protected void loadOwnableData(LandClaimBuilder builder, FileConfig config) throws ModelLoadException {
        LandChunkData chunkData = config.get("Boundaries", LandChunkData.class);
        if (chunkData == null) throw new ModelLoadException("Invalid or empty chunks data");

        builder.chunkData(chunkData);
    }

    @Override
    protected void saveOwnableData(LandClaim claim, FileConfig config) {
        config.set("Boundaries", claim.getChunkData());
    }
}
