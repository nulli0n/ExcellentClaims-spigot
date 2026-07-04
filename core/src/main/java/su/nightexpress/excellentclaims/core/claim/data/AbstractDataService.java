package su.nightexpress.excellentclaims.core.claim.data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.claim.data.DataService;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.io.AbstractClaimIOService;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public abstract class AbstractDataService<T extends Claim> implements DataService<T> {

    protected final AbstractClaimIOService<T, ?> ioService;
    protected final ClaimRepository<T>           repository;

    protected final Set<T> pendingSaves;

    public AbstractDataService(AbstractClaimIOService<T, ?> ioService, ClaimRepository<T> repository) {
        this.ioService = ioService;
        this.repository = repository;

        this.pendingSaves = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void loadClaims(AdaptedKey worldKey) {
        this.ioService.readClaims(worldKey).forEach(claim -> {
            this.repository.register(claim);
            claim.setEnabled();
        });

        // TODO World size & claim name
        //this.plugin.info("Loaded %s %s claims for %s.".formatted(this.repository.size(), this.definition
        //    .getClaimName(), worldName));
    }

    @Override
    public void unloadClaims(AdaptedKey worldKey) {
        this.saveDirty();

        this.repository.values(worldKey).forEach(claim -> {
            this.repository.remove(claim);
            claim.setDisabled();
        });
    }

    @Override
    public void saveClaim(T claim) {
        this.pendingSaves.remove(claim);
        this.ioService.writeClaim(claim);
    }

    @Override
    public @Nullable T getClaim(Identifier id) {
        return this.repository.get(id);
    }

    @Override
    public void updateClaim(T claim) {
        this.repository.reindex(claim);
    }

    @Override
    public void deleteClaim(T claim) {
        if (this.ioService.deleteClaimFile(claim)) {
            this.repository.remove(claim);
            claim.setDisabled();
        }
    }

    @Override
    public void markDirty(@NonNull T claim) {
        this.pendingSaves.add(claim);
    }

    @Override
    public void saveDirty() {
        if (this.pendingSaves.isEmpty()) return;

        // Drain the queue safely
        Set<T> toSave = new HashSet<>(this.pendingSaves);
        this.pendingSaves.removeAll(toSave);

        for (T claim : toSave) {
            this.ioService.writeClaim(claim);
        }
    }

    @Override
    public void saveAll() {
        for (T claim : this.repository.values()) {
            this.ioService.writeClaim(claim);
        }
    }
}
