package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Silverfish;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;

@NullMarked
public class SilverfishInfestRule extends BaseEntityBlockChangeRule {

    public SilverfishInfestRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block) {
        return entity instanceof Silverfish;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Silverfish Infest")
            .description(
                "Controls whether silverfishes",
                "can infest blocks here."
            )
            .icon(Material.STONE_BRICKS)
            .build();
    }
}
