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
public class UseHornsRule extends BasePlayerUseItemRule {

    public UseHornsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, ItemStack itemStack) {
        return itemStack.getType() == Material.GOAT_HORN;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Horns")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Item Usage") + " permission",
                "can use horns."
            )
            .icon(Material.GOAT_HORN)
            .build();
    }
}
