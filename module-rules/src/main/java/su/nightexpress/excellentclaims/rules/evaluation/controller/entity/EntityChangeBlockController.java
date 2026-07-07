package su.nightexpress.excellentclaims.rules.evaluation.controller.entity;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class EntityChangeBlockController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public EntityChangeBlockController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        Block block = event.getBlock();

        if (this.handleBlockChange(entity, block) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        Entity entity = event.getEntity();
        Block block = event.getBlock();

        if (this.handleBlockChange(entity, block) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        Block block = entity.getLocation().getBlock();

        if (this.handleBlockChange(entity, block) == EventState.DENY) {
            event.setCancelled(true);
            return;
        }

        List<Block> blocks = event.blockList();
        blocks.removeIf(exploded -> {
            return this.handleBlockChange(entity, exploded) == EventState.DENY;
        });

        if (blocks.isEmpty()) {
            event.setCancelled(true);
        }
    }

    private EventState handleBlockChange(Entity entity, Block block) {
        Player actor = null;
        if (entity instanceof Player player) {
            actor = player;
        }

        EntityChangeBlockContext context = new EntityChangeBlockContext(actor, entity, block);
        EventState state = this.engine.evaluate(context).state();
        if (state == EventState.DENY) {
            if (actor != null) {
                this.dispatcher.send(RulesLang.PROTECTION_BLOCK_CHANGE, actor, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
                );
            }
        }

        return state;
    }
}
