package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class EndermanGriefRule extends BaseEntityBlockChangeRule {

    public EndermanGriefRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block) {
        return entity instanceof Enderman;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Enderman Grief")
            .description(
                "Controls whether endermans",
                "can pickup blocks here."
            )
            .icon(NightItem.asCustomHead("4f24767c8138b3dfec02f77bd151994d480d4e869664ce09a26b19289212162b"))
            .build();
    }
}
