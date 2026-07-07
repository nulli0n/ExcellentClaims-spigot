package su.nightexpress.excellentclaims.rules.impl.player.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemInteractContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseItemRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseSpawnEggsRule extends BasePlayerUseItemRule {

    public UseSpawnEggsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(ItemInteractContext context, ItemStack itemStack) {
        return itemStack.getItemMeta() instanceof SpawnEggMeta;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Spawn Eggs")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Item Usage") + " permission",
                "can use spawn eggs."
            )
            .icon(Material.ZOMBIE_SPAWN_EGG)
            .build();
    }
}
