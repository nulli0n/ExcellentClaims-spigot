package su.nightexpress.excellentclaims.rules.filter.codec;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;

import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.nightcore.bridge.reflect.TypeReference;

public final class StandardFilterTypes {

    public static final TypeReference<FilteredSet<Material>> MATERIAL = new TypeReference<FilteredSet<Material>>() {

    };

    public static final TypeReference<FilteredSet<EntityType>> ENTITY_TYPE = new TypeReference<FilteredSet<EntityType>>() {

    };

    public static final TypeReference<FilteredSet<DamageType>> DAMAGE_TYPE = new TypeReference<FilteredSet<DamageType>>() {

    };

    public static final TypeReference<FilteredSet<Command>> COMMAND = new TypeReference<FilteredSet<Command>>() {

    };

    private StandardFilterTypes() {
    }
}
