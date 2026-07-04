package su.nightexpress.excellentclaims.rank;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RankRegistry;
import su.nightexpress.excellentclaims.rank.model.RankDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;

@NullMarked
public class RankIOService {

    private static final String RANKS_PATH = "Ranks";

    private final Path         file;
    private final RankRegistry registry;
    private final Logger       logger;

    public RankIOService(Path file, RankRegistry registry, Logger logger) {
        this.file = file;
        this.registry = registry;
        this.logger = logger;
    }

    public void loadRanks() {
        FileConfig config = FileConfig.load(this.file);

        if (config.getSection(RANKS_PATH).isEmpty()) {
            this.loadDefaults(config);
            return;
        }

        config.getSection(RANKS_PATH).forEach(id -> {
            try {
                Rank rank = this.loadRank(config, RANKS_PATH + "." + id, id);
                this.registry.register(rank);
            }
            catch (ModelLoadException exception) {
                this.logger.log(Level.SEVERE, "Rank '%s' can not be loaded.".formatted(id));
                this.logger.log(Level.SEVERE, "Reason: ", exception);
            }
        });

        config.saveChanges();

        this.logger.info("Loaded " + this.registry.size() + " member ranks.");
    }

    private DefaultRank loadRank(FileConfig config, String path, String rawId) throws ModelLoadException {
        Identifier id = Identifier.parse(rawId).orElse(null);
        if (id == null) throw new ModelLoadException("Invalid rank ID");

        RankDefinition definition = config.get(path, RankDefinition.class);
        if (definition == null) throw new ModelLoadException("Unable to read rank definition");

        return new DefaultRank(id, definition);
    }

    private void loadDefaults(FileConfig config) {
        RankDefaults.getDefaultRanks().forEach(defRank -> {
            config.set(RANKS_PATH + "." + defRank.id(), defRank);
            this.registry.register(defRank);
        });
    }

    public void recalculatePermissions() {
        this.registry.values()
            .stream()
            // Ascending Order: Lowest priority go FIRST.
            // This ensures that 'previous' rank permissions are already calculated.
            .sorted(Comparator.comparingInt(Rank::getPriority))
            .forEach(this::recalculatePermissions);
    }

    private void recalculatePermissions(Rank rank) {
        Set<ClaimPermission> current = new HashSet<>(rank.getBasePermissions());

        Rank previous = this.registry.getPreviousRank(rank);
        if (previous != null) {
            // Because of ascending sorting, 'previous' already contains the permissions 
            // of every rank below it.
            current.addAll(previous.getEffectivePermissions());
        }

        rank.recalculatePermissions(current);
    }
}
