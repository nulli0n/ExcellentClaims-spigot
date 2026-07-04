package su.nightexpress.excellentclaims.region.member.ui.list;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.MemberService;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberListUIContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class MemberListMenu extends AbstractObjectMenu<MemberListUIContext> {

    private static final String DEFAULT_TITLE = "[%s] Members"
        .formatted(RegionsPlaceholders.REGION_RAW_NAME);

    private final MemberUIController controller;
    private final MemberService      service;

    @Nullable
    private ItemPopulator<MemberDisplayData> memberPopulator;

    public MemberListMenu(ClaimPlugin plugin, MemberUIController controller, MemberService service) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, MemberListUIContext.class);
        this.controller = controller;
        this.service = service;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        MemberListUIContext uiContext = this.getObject(context);

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(uiContext.claim().placeholders())
            .build();

        return placeholders.apply(super.getRawTitle(context));
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addNextPageButton(26);
        this.addPreviousPageButton(18);

        this.addBackButton(this::handleBack, 49);

        this.addDefaultButton("add_member_by_online", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.asCustomHead("123fa5a163d4295d3db14ac429f78842ce1b0178f7d27a77e3fdad65c8d74ed6")
                    .setDisplayName(TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Add Online Member"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Add new member to the region."),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to select"))
                    ))
                )
                .action(this::handleAddOnlineMember)
                .condition(context -> this.checkResult(context, this.service::canAddMembers).success())
                .build()
            )
            .slots(52)
            .build()
        );

        // TODO Add By Name

        this.addDefaultButton("purge_members", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LAVA_BUCKET)
                    .setDisplayName(TagWrappers.ORANGE.and(TagWrappers.BOLD).wrap("Purge Members"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Remove all members from the region."),
                        "",
                        TagWrappers.ORANGE.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to purge"))
                    ))
                )
                .action(this::handlePurge)
                .condition(context -> this.checkResult(context, this.service::canPurgeMembers).success())
                .build()
            )
            .slots(46)
            .build()
        );
    }

    @Override
    protected void onLoad(FileConfig config) {
        String memberName = config.getOrSet("Member.Name", ConfigCodecs.STRING, TagWrappers.GOLD.wrap(
            CommonPlaceholders.GENERIC_NAME));

        List<String> memberLore = config.getOrSet("Member.Lore", ConfigCodecs.STRING_LIST, Lists.newList(
            TagWrappers.GRAY.wrap("Rank: " + TagWrappers.GOLD.wrap(ClaimsPlaceholders.RANK_NAME)),
            CommonPlaceholders.EMPTY_IF_BELOW,
            ClaimsPlaceholders.RANK_PERMISSIONS,
            CommonPlaceholders.EMPTY_IF_BELOW,
            TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click for actions"))
        ));

        int[] memberSlots = config.getOrSet("Member.Slots", ConfigCodecs.INT_ARRAY,
            new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34}
        );

        this.memberPopulator = ItemPopulator.builder(MemberDisplayData.class)
            .slots(memberSlots)
            .itemProvider((context, data) -> {
                return new NightItem(Material.PLAYER_HEAD)
                    .setPlayerProfile(data.userData().getEffectiveProfile())
                    .setDisplayName(memberName)
                    .setLore(memberLore)
                    .replace(replacer -> replacer
                        .with(CommonPlaceholders.GENERIC_NAME, () -> data.userData().getName())
                        .with(data.rank().placeholders())
                    );
            })
            .actionProvider(data -> context -> this.handleMember(data.member(), data.userData(), context))
            .build();
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        MemberListUIContext uiContext = this.getObject(context);
        List<MemberDisplayData> members = uiContext.members();

        if (this.memberPopulator != null) {
            this.memberPopulator.populateTo(context, members, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private ActionResult checkResult(ViewerContext context,
                                     BiFunction<Player, RegionClaim, ActionResult> consumer) {
        Player player = context.getPlayer();
        RegionClaim claim = this.getObject(context).claim();
        return consumer.apply(player, claim);
    }

    private void handleUIContext(ActionContext context, BiConsumer<Player, MemberListUIContext> consumer) {
        Player player = context.getPlayer();
        MemberListUIContext uiContext = this.getObject(context);
        consumer.accept(player, uiContext);
    }

    private void handleBack(ActionContext context) {
        this.handleUIContext(context, (player, uiContext) -> {
            this.controller.onBackClick(player, uiContext.claim());
        });
    }

    private void handleMember(ClaimMember member, UserData userData, ActionContext context) {
        this.handleUIContext(context, (player, uiContext) -> {
            this.controller.onMemberClick(player, uiContext.claim(), member, userData);
        });
    }

    private void handleAddOnlineMember(ActionContext context) {
        this.handleUIContext(context, (player, uiContext) -> {
            Runnable callback = () -> this.controller.openMembersMenu(player, uiContext.claim()); // Re-open fully to fetch new members in context

            this.controller.onAddOnlineMemberClick(player, uiContext.claim(), callback);
        });
    }

    private void handlePurge(ActionContext context) {
        this.handleUIContext(context, (player, uiContext) -> {
            Runnable callback = () -> this.controller.openMembersMenu(player, uiContext.claim()); // Re-open fully to fetch new members in context

            this.controller.onPurgeMembersClick(player, uiContext.claim(), callback);
        });
    }
}
