package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ravager;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RavagerGriefRule extends BaseEntityBlockChangeRule {

    public RavagerGriefRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block) {
        return entity instanceof Ravager;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Ravager Grief")
            .description(
                "Controls whether Ravagers",
                "can destroy blocks here."
            )
            .icon(NightItem.asCustomHead("cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8"))
            .build();
    }
}
