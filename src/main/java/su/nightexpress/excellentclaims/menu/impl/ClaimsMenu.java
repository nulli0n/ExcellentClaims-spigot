package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.*;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ClaimsMenu extends ConfigMenu<ClaimPlugin> implements AutoFilled<Claim>, Linked<ClaimsMenu.Data> {

    public static final String FILE_NAME = "claim_list.yml";

    private static final String INFO_TELEPORT = "%teleport%";
    private static final String INFO_MANAGE   = "%manage%";
    private static final String INFO_GLOBAL   = "%global_info%";
    private static final String INFO_OWN      = "%own_info%";
    private static final String INFO_MEMBER   = "%member_info%";

    private final ViewLink<Data> link;

    private String titleGlobal;
    private String titleWorld;
    private String titleUser;

    private String typeRegion;
    private String typeChunk;

    private String       chunkName;
    private String       regionName;
    private List<String> claimLore;
    private List<String> globalInfo;
    private List<String> ownInfo;
    private List<String> memberInfo;
    private List<String> teleportInfo;
    private List<String> manageInfo;
    private int[]        claimSlots;

    public ClaimsMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.load();
    }

    @Override
    @NotNull
    public ViewLink<Data> getLink() {
        return this.link;
    }

    public void open(@NotNull Player player, @NotNull ClaimType type) {
        this.open(player, new Data(type, null, null));
    }

    public void open(@NotNull Player player, @NotNull ClaimType type, @NotNull UserInfo target) {
        this.open(player, new Data(type, target, null));
    }

    public void open(@NotNull Player player, @NotNull ClaimType type, @NotNull String worldName) {
        this.open(player, new Data(type, null, worldName));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        ClaimType type = data.type;
        UserInfo userInfo = data.userInfo;
        String worldName = data.worldName;

        String title;
        if (userInfo != null) title = this.titleUser;
        else if (worldName != null) title = this.titleWorld;
        else title = this.titleGlobal;

        options.editTitle(str -> title
            .replace(GENERIC_TYPE, type == ClaimType.CHUNK ? this.typeChunk : this.typeRegion)
            .replace(GENERIC_VALUE, String.valueOf(userInfo == null ? worldName : userInfo.getPlayerName())));
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Claim> autoFill) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        ClaimType type = data.type;
        UserInfo userInfo = data.userInfo;
        String worldName = data.worldName;

        Collection<Claim> claims;

        if (userInfo != null) {
            // Display all player's claims.
            UUID playerId = userInfo.getPlayerId();
            claims = this.plugin.getClaimManager().getClaims(type, claim -> claim.isActive() && claim.isOwnerOrMember(playerId))
                .stream().sorted(Comparator.comparing((Claim claim) -> claim.isOwner(playerId)).reversed().thenComparing(Claim::getDisplayName)).toList();
        }
        else if (worldName != null) {
            // Display all world claims.
            claims = this.plugin.getClaimManager().getClaims(worldName, type).stream().sorted(Comparator.comparing(Claim::getId)).toList();
        }
        else {
            // Display all claims.
            claims = this.plugin.getClaimManager().getClaims(type).stream().sorted(Comparator.comparing(Claim::getId)).toList();
        }

        autoFill.setSlots(this.claimSlots);
        autoFill.setItems(claims);
        autoFill.setItemCreator(claim -> {
            boolean isOwner = claim.isOwner(player);
            boolean isMember = claim.isMember(player);

            ItemStack itemStack = claim.getIcon();
            ItemReplacer.create(itemStack).hideFlags().trimmed()
                .setDisplayName(type == ClaimType.CHUNK ? this.chunkName : this.regionName)
                .setLore(this.claimLore)
                .replace(INFO_GLOBAL, !isOwner && !isMember ? this.globalInfo : Collections.emptyList())
                .replace(INFO_OWN, isOwner ? this.ownInfo : Collections.emptyList())
                .replace(INFO_MEMBER, isMember ? this.memberInfo : Collections.emptyList())
                .replace(INFO_TELEPORT, claim.hasPermission(player, ClaimPermission.TELEPORT) ? this.teleportInfo : Collections.emptyList())
                .replace(INFO_MANAGE, claim.hasPermission(player, ClaimPermission.MANAGE_CLAIM) ? this.manageInfo : Collections.emptyList())
                .replace(claim.getPlaceholders())
                .writeMeta();

            return itemStack;
        });
        autoFill.setClickAction(claim -> (viewer1, event) -> {
            if (event.isLeftClick()) {
                if (!claim.hasPermission(player, ClaimPermission.TELEPORT)) {
                    return;
                }
                this.runNextTick(() -> claim.teleport(player));
            }
            if (event.isRightClick()) {
                if (!claim.hasPermission(player, ClaimPermission.MANAGE_CLAIM)) {
                    return;
                }
                this.runNextTick(() -> this.plugin.getMenuManager().openClaimMenu(player, claim));
            }
        });
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Claims [" + GENERIC_VALUE + "]"), MenuSize.CHEST_45);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack nextPage = ItemUtil.getSkinHead(SKIN_ARROW_RIGHT);
        ItemUtil.editMeta(nextPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_NEXT_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(nextPage).setPriority(10).setSlots(44).setHandler(ItemHandler.forNextPage(this)));

        ItemStack backPage = ItemUtil.getSkinHead(SKIN_ARROW_LEFT);
        ItemUtil.editMeta(backPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_PREVIOUS_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(backPage).setPriority(10).setSlots(36).setHandler(ItemHandler.forPreviousPage(this)));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.titleGlobal = ConfigValue.create("Title.Global",
            BLACK.enclose("All " + GENERIC_TYPE)
        ).read(cfg);

        this.titleUser = ConfigValue.create("Title.User",
            BLACK.enclose(GENERIC_TYPE + " of " + GENERIC_VALUE)
        ).read(cfg);

        this.titleWorld = ConfigValue.create("Title.World",
            BLACK.enclose(GENERIC_TYPE + " in " + GENERIC_VALUE)
        ).read(cfg);

        this.typeChunk = ConfigValue.create("Type.Chunk",
            "Claims"
        ).read(cfg);

        this.typeRegion = ConfigValue.create("Type.Region",
            "Regions"
        ).read(cfg);

        this.chunkName = ConfigValue.create("Claim.ChunkName",
            LIGHT_YELLOW.enclose(BOLD.enclose(CLAIM_NAME))
        ).read(cfg);

        this.regionName = ConfigValue.create("Claim.RegionName",
            LIGHT_YELLOW.enclose(BOLD.enclose(CLAIM_NAME)) + " " + GRAY.enclose("(ID: " + WHITE.enclose(CLAIM_ID) + ")")
        ).read(cfg);

        this.claimLore = ConfigValue.create("Claim.Lore", Lists.newList(
            LIGHT_YELLOW.enclose(LIGHT_GRAY.enclose("World: ") + CLAIM_WORLD),
            LIGHT_YELLOW.enclose(LIGHT_GRAY.enclose("Owner: ") + CLAIM_OWNER_NAME),
            "",
            INFO_GLOBAL,
            INFO_OWN,
            INFO_MEMBER,
            "",
            INFO_TELEPORT,
            INFO_MANAGE
        )).read(cfg);

        this.globalInfo = ConfigValue.create("Claim.Info.Global", Lists.newList(
            LIGHT_RED.enclose("✘ You're not a member of this claim.")
        )).read(cfg);

        this.ownInfo = ConfigValue.create("Claim.Info.Own", Lists.newList(
            LIGHT_GREEN.enclose("✔ You're the owner of this claim.")
        )).read(cfg);

        this.memberInfo = ConfigValue.create("Claim.Info.Member", Lists.newList(
            LIGHT_YELLOW.enclose("✔ You're member of this claim.")
        )).read(cfg);

        this.teleportInfo = ConfigValue.create("Claim.Action.Teleport", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Left-Click to " + LIGHT_YELLOW.enclose("teleport") + ".")
        )).read(cfg);

        this.manageInfo = ConfigValue.create("Claim.Action.Manage", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Right-Click to " + LIGHT_YELLOW.enclose("manage") + ".")
        )).read(cfg);

        this.claimSlots = ConfigValue.create("Claim.Slots", IntStream.range(0, 36).toArray()).read(cfg);
    }

    public record Data(@NotNull ClaimType type, @Nullable UserInfo userInfo, @Nullable String worldName) {}
}
