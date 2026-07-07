package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class PlayerDamageController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public PlayerDamageController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        DamageSource source = event.getDamageSource();
        Entity entity = event.getEntity();
        Player actor = null;

        if (source.getCausingEntity() instanceof Player player) {
            actor = player;
        }

        EntityDamageContext context = new EntityDamageContext(actor, entity, source);
        RuleResult result = this.engine.evaluate(context);

        if (result.state() == EventState.DENY) {
            event.setCancelled(true);

            if (actor != null) {
                this.dispatcher.send(RulesLang.PROTECTION_DAMAGE_ENTITY, actor, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(entity.getType()))
                );
            }
        }
    }
}
