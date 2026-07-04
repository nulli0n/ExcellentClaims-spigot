package su.nightexpress.excellentclaims.core.claim.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimDefinition;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimRules;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public abstract class AbstractClaimIOService<T extends Claim, B extends AbstractClaimBuilder<T, B>> {

    protected final Logger     logger;
    protected final Path       directory;
    protected final Identifier domain;

    public AbstractClaimIOService(Logger logger, Path directory, Identifier domain) {
        this.logger = logger;
        this.directory = directory;
        this.domain = domain;
    }

    protected abstract B createBuilder(DefaultClaimIdentity identity);

    public Path getClaimsDirectory(AdaptedKey worldKey) {
        return this.directory.resolve(worldKey.namespace()).resolve(worldKey.value());
    }

    public Path getClaimFile(@NonNull T claim) {
        String id = claim.id().value();
        AdaptedKey worldKey = claim.getWorldKey();

        return this.getClaimFile(id, worldKey);
    }

    public Path getClaimFile(String id, AdaptedKey worldKey) {
        return this.getClaimsDirectory(worldKey).resolve(FileConfig.withExtension(id));
    }

    public Collection<T> readClaims(AdaptedKey worldKey) {
        Path worldDir = this.getClaimsDirectory(worldKey);
        if (!Files.exists(worldDir)) return List.of();

        List<T> claims = new ArrayList<>();

        FileUtil.findYamlFiles(worldDir).forEach(file -> {
            try {
                T claim = this.loadClaim(worldKey, file);
                claims.add(claim);
            }
            catch (ModelLoadException exception) {
                this.logger.log(Level.SEVERE, "Claim '%s' can not be loaded.".formatted(file));
                this.logger.log(Level.SEVERE, "Reason: ", exception);
            }
        });

        return claims;
    }

    public T loadClaim(AdaptedKey worldKey, Path file) throws ModelLoadException {
        String fileName = FileUtil.getNameWithoutExtension(file);

        Identifier claimKey = Identifier.parse(fileName).orElseThrow(() -> {
            return new ModelLoadException("Invalid claim file name");
        });

        FileConfig config = FileConfig.load(file);

        DefaultClaimDefinition definition = config.getOrSet("Settings", DefaultClaimDefinition.class,
            new DefaultClaimDefinition());
        DefaultClaimRules properties = config.getOrSet("Properties", DefaultClaimRules.class, new DefaultClaimRules());

        B builder = this.createBuilder(new DefaultClaimIdentity(claimKey, worldKey, this.domain));
        builder.definition(definition);
        builder.properties(properties);
        this.loadAdditionalData(builder, config);

        return builder.build();
    }

    protected abstract void loadAdditionalData(@NonNull B builder, FileConfig config) throws ModelLoadException;

    public T createClaim(Identifier id, World world) {
        AdaptedKey worldKey = BukkitKeys.getKey(world);

        B builder = this.createBuilder(new DefaultClaimIdentity(id, worldKey, this.domain));
        builder.definition(new DefaultClaimDefinition());
        builder.properties(new DefaultClaimRules());
        this.createAdditionalData(builder);

        return builder.build();
    }

    protected abstract void createAdditionalData(@NonNull B builder);

    public boolean deleteClaimFile(@NonNull T claim) {
        Path file = this.getClaimFile(claim);

        try {
            Files.deleteIfExists(file);
            return true;
        }
        catch (IOException exception) {
            this.logger.log(Level.WARNING, "Could not delete '%s' claim".formatted(claim.id()), exception);
            return false;
        }
    }

    public void writeClaim(@NonNull T claim) {
        Path file = this.getClaimFile(claim);

        FileConfig.load(file).edit(config -> {
            this.saveCommonData(claim, config);
            this.saveAdditionalData(claim, config);
        });
    }

    protected void saveCommonData(@NonNull T claim, FileConfig config) {
        config.set("Settings", claim.definition());
        config.set("Properties", claim.getRules());
    }

    protected abstract void saveAdditionalData(@NonNull T claim, FileConfig config);
}
