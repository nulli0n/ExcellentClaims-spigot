package su.nightexpress.excellentclaims.rules.evaluation.context.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public record CreatureSpawnContext(LivingEntity entity, Location location) implements ActionContext {

    @Override
    public @Nullable Player actor() {
        return null;
    }
}
