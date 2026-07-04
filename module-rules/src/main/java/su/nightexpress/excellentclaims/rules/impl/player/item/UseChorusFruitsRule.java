package su.nightexpress.excellentclaims.rules.impl.player.item;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseItemRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseChorusFruitsRule extends BasePlayerUseItemRule {

    public UseChorusFruitsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, ItemStack itemStack) {
        return itemStack.getType() == Material.CHORUS_FRUIT;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Ender Pearles")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Item Usage") + " permission",
                "can use chorus fruits."
            )
            .icon(Material.CHORUS_FRUIT)
            .build();
    }
}
