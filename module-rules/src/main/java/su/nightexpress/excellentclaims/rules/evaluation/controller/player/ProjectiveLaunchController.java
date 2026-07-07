package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.ProjectileLaunchContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class ProjectiveLaunchController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public ProjectiveLaunchController(@NonNull EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Projectile projectile)) return;

        if (this.handleProjectileLaunch(player, projectile) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile.getShooter() instanceof Player player)) return;

        if (this.handleProjectileLaunch(player, projectile) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    private EventState handleProjectileLaunch(Player actor, Projectile projectile) {
        ProjectileLaunchContext context = new ProjectileLaunchContext(actor, projectile);
        EventState state = this.engine.evaluate(context).state();

        if (state == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_PROJECTILE_SHOOT, actor, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(projectile.getType()))
            );
        }

        return state;
    }
}
