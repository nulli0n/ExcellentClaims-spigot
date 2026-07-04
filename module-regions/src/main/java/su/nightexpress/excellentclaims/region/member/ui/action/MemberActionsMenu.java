package su.nightexpress.excellentclaims.region.member.ui.action;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.MemberContext;
import su.nightexpress.excellentclaims.region.member.MemberService;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberActionUIContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class MemberActionsMenu extends AbstractObjectMenu<MemberActionUIContext> {

    private static final String DEFAULT_TITLE = "[%s] %s Actions"
        .formatted(RegionsPlaceholders.REGION_RAW_NAME, CommonPlaceholders.PLAYER_NAME);

    private final MemberService      service;
    private final MemberUIController controller;

    public MemberActionsMenu(ClaimPlugin plugin, MemberService service, MemberUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, DEFAULT_TITLE, MemberActionUIContext.class);
        this.service = service;
        this.controller = controller;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        MemberContext memberContext = uiContext.memberContext();

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(uiContext.claim().placeholders())
            .with(CommonPlaceholders.PLAYER_NAME, () -> memberContext.userData().getName())
            .build();

        return placeholders.apply(super.getRawTitle(context));
    }

    @Override
    public void defineDefaultLayout() {
        // TODO Add more states (no perm, not avail, etc)

        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 31);

        this.addDefaultButton("promote_member", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOWSTONE_DUST)
                    .setDisplayName(TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Promote"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Promote this member with a higher rank."),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to promote"))
                    ))
                )
                .action(this::handlePromote)
                .condition(context -> this.checkPromotion(context).success())
                .build()
            )
            .slots(11)
            .build()
        );

        this.addDefaultButton("demote_member", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.REDSTONE)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Demote"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Demote this member with a lower rank."),
                        "",
                        TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to demote"))
                    ))
                )
                .action(this::handleDemote)
                .condition(context -> this.checkDemotion(context).success())
                .build()
            )
            .slots(13)
            .build()
        );

        this.addDefaultButton("kick_member", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Kick"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Kick member from this claim."),
                        "",
                        TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to kick"))
                    ))
                )
                .action(this::handleKick)
                .condition(context -> this.checkKick(context).success())
                .build()
            )
            .slots(15)
            .build()
        );
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {

    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory,
                          List<MenuItem> items) {

    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private ActionResult checkPromotion(ViewerContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();
        RegionClaim claim = uiContext.claim();
        ClaimMember member = uiContext.memberContext().member();

        return this.service.canPromote(player, claim, member);
    }

    private ActionResult checkDemotion(ViewerContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();
        RegionClaim claim = uiContext.claim();
        ClaimMember member = uiContext.memberContext().member();

        return this.service.canDemote(player, claim, member);
    }

    private ActionResult checkKick(ViewerContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();
        ClaimMember member = uiContext.memberContext().member();

        return this.service.canKickMember(player, uiContext.claim(), member);
    }

    private void handleBack(ActionContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();

        this.controller.openMembersMenu(player, uiContext.claim());
    }

    private void handlePromote(ActionContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();
        Runnable callback = () -> context.getViewer().refresh();

        this.controller.onPromoteMemberClick(player, uiContext.claim(), uiContext, callback);
    }

    private void handleDemote(ActionContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();
        Runnable callback = () -> context.getViewer().refresh();

        this.controller.onDemoteMemberClick(player, uiContext.claim(), uiContext, callback);
    }

    private void handleKick(ActionContext context) {
        MemberActionUIContext uiContext = this.getObject(context);
        Player player = context.getPlayer();

        this.controller.onKickMemberClick(player, uiContext.claim(), uiContext, () -> context.getViewer().refresh());
    }
}
