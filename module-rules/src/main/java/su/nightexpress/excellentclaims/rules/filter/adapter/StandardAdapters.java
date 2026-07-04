package su.nightexpress.excellentclaims.rules.filter.adapter;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.filter.StringAdapter;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;

@NullMarked
public final class StandardAdapters {

    public static final StringAdapter<Material>   MATERIAL    = forKeyed(RegistryType.MATERIAL);
    public static final StringAdapter<EntityType> ENTITY_TYPE = forKeyed(RegistryType.ENTITY_TYPE);
    public static final StringAdapter<DamageType> DAMAGE_TYPE = forKeyed(RegistryType.DAMAGE_TYPE);

    private StandardAdapters() {
    }

    public static <T extends Keyed> StringAdapter<T> forKeyed(RegistryType<T> registry) {
        return new StringAdapter<>() {

            @Override
            public @Nullable T deserialize(String string) {
                return BukkitThing.getByString(registry, string);
            }

            @Override
            public String serialize(@NonNull T value) {
                return BukkitThing.getAsString(value);
            }
        };
    }
}
