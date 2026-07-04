package su.nightexpress.excellentclaims.core.claim.io;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMembers;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;

@NullMarked
public abstract class AbstractOwnableClaimIOSerivce<T extends OwnableClaim, B extends AbstractOwnableClaimBuilder<T, B>> extends AbstractClaimIOService<T, B> {

    public AbstractOwnableClaimIOSerivce(Logger logger, Path directory, Identifier domain) {
        super(logger, directory, domain);
    }

    @Override
    protected final void saveAdditionalData(@NonNull T claim, FileConfig config) {
        config.set("Members", claim.getMembersDomain());

        this.saveOwnableData(claim, config);
    }

    @Override
    protected final void loadAdditionalData(@NonNull B builder, FileConfig config) throws ModelLoadException {
        DefaultClaimMembers members = config.getOrSet("Members", DefaultClaimMembers.class, new DefaultClaimMembers());

        builder.members(members);

        // Pass to the child module
        this.loadOwnableData(builder, config);
    }

    @Override
    protected void createAdditionalData(@NonNull B builder) {
        builder.members(new DefaultClaimMembers());

        this.createOwnableData(builder);
    }

    protected abstract void saveOwnableData(@NonNull T claim, FileConfig config);

    protected abstract void loadOwnableData(@NonNull B builder, FileConfig config) throws ModelLoadException;

    protected abstract void createOwnableData(@NonNull B builder);
}