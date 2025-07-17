package su.nightexpress.excellentclaims.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.menu.impl.*;
import su.nightexpress.excellentclaims.menu.type.ClaimMenu;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.excellentclaims.util.list.SmartList;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.function.Consumer;
import java.util.function.Function;

public class MenuManager extends AbstractManager<ClaimPlugin> {

    private ClaimsMenu         claimsMenu;
    private SettingsMenu       settingsMenu;
    private MemberMenu         memberMenu;
    private MembersMenu        membersMenu;
    private FlagsMenu          flagsMenu;
    private EntrySelectionMenu selectionMenu;
    private SelectPlayerMenu   addMemberMenu;
    private SelectPlayerMenu   transferMenu;

    public MenuManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.loadUI();
    }

    @Override
    protected void onShutdown() {
        if (this.memberMenu != null) this.memberMenu.clear();
        if (this.claimsMenu != null) this.claimsMenu.clear();
    }

    private void loadUI() {
        this.claimsMenu = new ClaimsMenu(this.plugin);
        this.memberMenu = new MemberMenu(this.plugin);

        this.settingsMenu = this.addMenu(new SettingsMenu(this.plugin), Config.DIR_UI, "claim.yml");
        this.membersMenu = this.addMenu(new MembersMenu(this.plugin), Config.DIR_UI, "claim_members.yml");
        this.flagsMenu = this.addMenu(new FlagsMenu(this.plugin), Config.DIR_UI, "flags.yml");
        this.transferMenu = this.addMenu(new SelectPlayerMenu(this.plugin, ClaimPermission.TRANSFER_CLAIM), Config.DIR_UI, "claim_transfer.yml");
        this.addMemberMenu = this.addMenu(new SelectPlayerMenu(this.plugin, ClaimPermission.ADD_MEMBERS), Config.DIR_UI, "add_member.yml");

        this.selectionMenu = this.addMenu(new EntrySelectionMenu(this.plugin), Config.DIR_UI, "entry_selection.yml");
    }

    @NotNull
    public SettingsMenu getSettingsMenu() {
        return this.settingsMenu;
    }

    @NotNull
    public MembersMenu getMembersMenu() {
        return this.membersMenu;
    }

    public void openClaimsMenu(@NotNull Player player, @NotNull ClaimType type) {
        this.claimsMenu.open(player, type);
    }

    public void openClaimsMenu(@NotNull Player player, @NotNull ClaimType type, @NotNull UserInfo target) {
        this.claimsMenu.open(player, type, target);
    }

    public void openClaimsMenu(@NotNull Player player, @NotNull ClaimType type, @NotNull String worldName) {
        this.claimsMenu.open(player, type, worldName);
    }

    public boolean openMenuForCurrentChunk(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.CHUNK, claim -> this.openClaimMenu(player, claim));
    }

    public boolean openMenurForCurrentRegion(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.REGION, claim -> this.openClaimMenu(player, claim));
    }

    public boolean openMembersForCurrentChunk(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.CHUNK, claim -> this.openMembersMenu(player, claim));
    }

    public boolean openMemberForCurrentRegion(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.REGION, claim -> this.openMembersMenu(player, claim));
    }

    public boolean openFlagsForCurrentChunk(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.CHUNK, claim -> this.openFlagsMenu(player, claim));
    }

    public boolean openFlagsForCurrentRegion(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.REGION, claim -> this.openFlagsMenu(player, claim));
    }

    public boolean openTransferForCurrentChunk(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.CHUNK, claim -> this.openTransferMenu(player, claim));
    }

    public boolean openTransferForCurrentRegion(@NotNull Player player) {
        return this.openMenuForCurrentClaim(player, ClaimType.REGION, claim -> this.openTransferMenu(player, claim));
    }

    private boolean openMenuForCurrentClaim(@NotNull Player player, @NotNull ClaimType type, @NotNull Function<Claim, Boolean> function) {
        Claim claim = this.plugin.getClaimManager().getPrioritizedClaim(player.getLocation());
        if (claim == null) {
            Lang.ERROR_NO_CLAIM.getMessage().send(player);
            return false;
        }

        if (claim.getType() != type) {
            LangText message = type == ClaimType.CHUNK ? Lang.ERROR_NO_CHUNK : Lang.ERROR_NO_REGION;
            message.getMessage().send(player);
            return false;
        }

        return function.apply(claim);
    }

    public boolean openClaimMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openClaimMenu(player, claim, false);
    }

    public boolean openClaimMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return this.openMenu(player, claim, this.settingsMenu, menu -> menu.open(player, claim), force);
    }

    public <T> void openEntrySelection(@NotNull Player player, @NotNull Claim claim, @NotNull SmartList<T> list) {
        this.selectionMenu.open(player, claim, list);
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openFlagsMenu(player, claim, false);
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return this.openMenu(player, claim, this.flagsMenu, menu -> menu.open(player, claim), force);
    }

    public boolean openMembersMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openMembersMenu(player, claim, false);
    }

    public boolean openMembersMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return this.openMenu(player, claim, this.membersMenu, menu -> menu.open(player, claim), force);
    }

    public boolean openMemberMenu(@NotNull Player player, @NotNull Claim claim, @NotNull Member member) {
        return this.openMemberMenu(player, claim, member, false);
    }

    public boolean openMemberMenu(@NotNull Player player, @NotNull Claim claim, @NotNull Member member, boolean force) {
        return this.openMenu(player, claim, this.memberMenu, menu -> menu.open(player, claim, member), force);
    }

    public boolean openTransferMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openTransferMenu(player, claim, false);
    }

    public boolean openTransferMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return this.openMenu(player, claim, this.transferMenu, menu -> menu.open(player, claim), force);
    }

    public boolean openAddMemberMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openAddMemberMenu(player, claim, false);
    }

    public boolean openAddMemberMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return this.openMenu(player, claim, this.addMemberMenu, menu -> menu.open(player, claim), force);
    }

    public <T extends ClaimMenu> boolean openMenu(@NotNull Player player, @NotNull Claim claim, @NotNull T menu, @NotNull Consumer<T> consumer, boolean force) {
        if (!force && !menu.hasPermission(player, claim)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        consumer.accept(menu);
        return true;
    }
}
