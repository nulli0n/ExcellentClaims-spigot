package su.nightexpress.excellentclaims.region.member.ui.button;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.MemberService;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class MembersMenuButton implements DefaultButtonExtension {

    private final MemberService      service;
    private final MemberUIController controller;

    public MembersMenuButton(MemberService service, MemberUIController controller) {
        this.service = service;
        this.controller = controller;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("region_members", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.asCustomHead("e0130271c816e36ab17f8ef5ef06304c8cbda95230788285412bf53466b07034")
                    .setDisplayName(TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Members"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("View and manage region members."),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(29)
            .build());
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        RegionClaim claim = context.getObject(RegionClaim.class);

        this.controller.openMembersMenu(player, claim);
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        RegionClaim claim = context.getObject(RegionClaim.class);

        return this.service.canManageMembers(player, claim).success();
    }
}
