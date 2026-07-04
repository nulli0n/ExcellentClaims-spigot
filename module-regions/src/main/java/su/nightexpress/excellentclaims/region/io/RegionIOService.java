package su.nightexpress.excellentclaims.region.io;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractOwnableClaimIOSerivce;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.data.model.RegionClaimBuilder;
import su.nightexpress.excellentclaims.region.data.model.RegionCuboid;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;

@NullMarked
public class RegionIOService extends AbstractOwnableClaimIOSerivce<RegionClaim, RegionClaimBuilder> {

    public RegionIOService(Logger logger, Path directory, Identifier domain) {
        super(logger, directory, domain);
    }

    @Override
    protected void createOwnableData(RegionClaimBuilder builder) {
        builder.boundingBox(new RegionCuboid());
    }

    @Override
    protected RegionClaimBuilder createBuilder(DefaultClaimIdentity identity) {
        return new RegionClaimBuilder(identity);
    }

    @Override
    protected void loadOwnableData(RegionClaimBuilder builder, FileConfig config) throws ModelLoadException {
        RegionCuboid boundingBox = config.get("BoundingBox", RegionCuboid.class);
        if (boundingBox == null) throw new ModelLoadException("Invalid or empty bounding box data");

        builder.boundingBox(boundingBox);
    }

    @Override
    protected void saveOwnableData(RegionClaim claim, FileConfig config) {
        config.set("BoundingBox", claim.getBoundingBox());
    }
}
