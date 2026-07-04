package su.nightexpress.excellentclaims.wilderness.io;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractClaimIOService;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessClaimBuilder;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;

@NullMarked
public class WildernessIOService extends AbstractClaimIOService<WildernessRegion, WildernessClaimBuilder> {

    public WildernessIOService(Logger logger, Path directory, Identifier domain) {
        super(logger, directory, domain);
    }

    @Override
    protected WildernessClaimBuilder createBuilder(DefaultClaimIdentity identity) {
        return new WildernessClaimBuilder(identity);
    }

    @Override
    protected void createAdditionalData(WildernessClaimBuilder builder) {

    }

    @Override
    protected void loadAdditionalData(WildernessClaimBuilder builder, FileConfig config) throws ModelLoadException {

    }

    @Override
    protected void saveAdditionalData(WildernessRegion claim, FileConfig config) {

    }
}
