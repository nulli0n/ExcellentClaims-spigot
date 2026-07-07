package su.nightexpress.excellentclaims.rules.evaluation.controller.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.CreatureSpawnContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

@NullMarked
public class EntitySpawnController extends AbstractEventController {

    public EntitySpawnController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        Location location = event.getLocation();

        CreatureSpawnContext context = new CreatureSpawnContext(entity, location);
        EventState state = this.engine.evaluate(context).state();
        if (state == EventState.DENY) {
            event.setCancelled(true);
        }
    }
}
