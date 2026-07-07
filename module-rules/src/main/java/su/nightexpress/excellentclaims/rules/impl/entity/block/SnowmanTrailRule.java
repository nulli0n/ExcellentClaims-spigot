package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowman;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class SnowmanTrailRule extends BaseEntityBlockChangeRule {

    public SnowmanTrailRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block) {
        return entity instanceof Snowman;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Snowman Trail")
            .description(
                "Controls whether snowmans",
                "can form snow here."
            )
            .icon(NightItem.asCustomHead("126ab3ed98ff470e4aa03fc69c745f61c0b614f3e1ecb42bac1c929223364789"))
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
