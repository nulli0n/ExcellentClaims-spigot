package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.SourceBlockContext;
import su.nightexpress.excellentclaims.api.rule.context.TargetBlockContext;

@NullMarked
public record BlockFromToContext(Block sourceBlock,
                                 Block targetBlock) implements ActionContext, TargetBlockContext, SourceBlockContext {

}