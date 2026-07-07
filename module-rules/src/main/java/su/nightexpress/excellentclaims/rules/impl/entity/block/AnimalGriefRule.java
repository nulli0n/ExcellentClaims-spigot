package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class AnimalGriefRule extends BaseEntityBlockChangeRule {

    public AnimalGriefRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block) {
        return entity instanceof Animals;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Animal Grief")
            .description(
                "Controls whether animals",
                "can change blocks here."
            )
            .icon(NightItem.asCustomHead("84e5cdb0edb362cb454586d1fd0ebe971423f015b0b1bfc95f8d5af8afe7e810"))
            .build();
    }
}
