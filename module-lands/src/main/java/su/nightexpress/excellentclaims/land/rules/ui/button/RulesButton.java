package su.nightexpress.excellentclaims.land.rules.ui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.rules.ui.LandRuleUIController;
import su.nightexpress.excellentclaims.land.rules.ui.LandRuleUIService;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RulesButton implements DefaultButtonExtension {

    private final LandRuleUIService    service;
    private final LandRuleUIController controller;

    public RulesButton(LandRuleUIService service, LandRuleUIController controller) {
        this.service = service;
        this.controller = controller;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("land_rules", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.RED_BANNER)
                    .setDisplayName(TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Rules")))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("View and manage claim rules."),
                        "",
                        TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(30)
            .build()
        );
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        return this.service.canOpenRules(player, claim).success();
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        this.controller.onRulesClick(player, claim);
    }
}
