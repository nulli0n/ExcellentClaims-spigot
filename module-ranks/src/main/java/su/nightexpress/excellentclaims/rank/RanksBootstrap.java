package su.nightexpress.excellentclaims.rank;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RankRegistry;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;

@NullMarked
public final class RanksBootstrap {

    private static final String FILE_CONFIG = "ranks.yml";

    private RanksBootstrap() {
    }

    public static RanksModule bootstrap(DependencyContainer container) {
        Logger logger = container.get(Logger.class);
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        Path moduleDir = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve("ranks");
        Path ranksFile = moduleDir.resolve(FILE_CONFIG);

        RanksCodecRegistrar.registerCodecs();

        RankRegistry registry = new RankRegistry();
        RankIOService rankIo = new RankIOService(ranksFile, registry, logger);
        RanksModule module = new RanksModule(registry, rankIo);

        container.register(RanksAPI.class, module);

        return module;
    }
}
