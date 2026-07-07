package su.nightexpress.excellentclaims.rules.evaluation.context.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.ItemStackContext;

@NullMarked
public record ItemDropContext(Player actor,
                              ItemStack itemStack) implements ActionContext, ActorContext, ItemStackContext {

}
