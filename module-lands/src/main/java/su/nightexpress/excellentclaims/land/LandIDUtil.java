package su.nightexpress.excellentclaims.land;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

public final class LandIDUtil {

    private static final String LAND_ID_PATTERN = "%s_%s_%s_%s";

    private LandIDUtil() {
    }

    public static Identifier generateId(Player player, ChunkPos chunkPos) {
        // Timestamp to make sure no duplicated IDs were produced (valid for chunk separation of the original claim chunk).

        String name = LowerCase.internal(player.getName());
        int x = chunkPos.getX();
        int z = chunkPos.getZ();
        long millis = System.currentTimeMillis();

        return Identifier.of(LAND_ID_PATTERN.formatted(name, x, z, millis));
    }
}
