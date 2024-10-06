package su.nightexpress.excellentclaims.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.menu.impl.*;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.function.Function;

public class MenuManager extends AbstractManager<ClaimPlugin> {

    private ClaimsMenu claimsMenu;
    private ClaimMenu claimMenu;
    private MemberMenu memberMenu;
    private MembersMenu membersMenu;
    private FlagsTypeMenu flagsTypeMenu;
    private FlagsMenu flagsMenu;
    private TransferMenu transferMenu;
    private ConfirmMenu confirmMenu;

    public MenuManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.loadUI();
    }

    @Override
    protected void onShutdown() {
        if (this.confirmMenu != null) this.confirmMenu.clear();
        if (this.transferMenu != null) this.transferMenu.clear();
        if (this.memberMenu != null) {
            this.memberMenu.clear();
            this.memberMenu = null;
        }
        if (this.membersMenu != null) {
            this.membersMenu.clear();
            this.membersMenu = null;
        }
        if (this.flagsTypeMenu != null) this.flagsTypeMenu.clear();
        if (this.flagsMenu != null) {
            this.flagsMenu.clear();
            this.flagsMenu = null;
        }
        if (this.claimMenu != null) {
            this.claimMenu.clear();
            this.claimMenu = null;
        }
        if (this.claimsMenu != null) this.claimsMenu.clear();
    }

    private void loadUI() {
        this.claimsMenu = new ClaimsMenu(this.plugin);
        this.claimMenu = new ClaimMenu(this.plugin);
        this.memberMenu = new MemberMenu(this.plugin);
        this.membersMenu = new MembersMenu(this.plugin);
        this.flagsTypeMenu = new FlagsTypeMenu(this.plugin);
        this.flagsMenu = new FlagsMenu(this.plugin);
        this.transferMenu = new TransferMenu(this.plugin);
        this.confirmMenu = new ConfirmMenu(this.plugin);
    }

    public void openConfirm(@NotNull Player player, @NotNull Confirmation confirmation) {
        this.confirmMenu.open(player, confirmation);
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
        if (!force) {
            if (!claim.hasPermission(player, ClaimPermission.MANAGE_CLAIM)) {
                Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
                return false;
            }
        }

        this.claimMenu.open(player, claim);
        return true;
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openFlagsMenu(player, claim, false);
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        return openFlagsMenu(player, claim, null, force);
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim, @Nullable FlagCategory category) {
        return this.openFlagsMenu(player, claim, category, false);
    }

    public boolean openFlagsMenu(@NotNull Player player, @NotNull Claim claim, @Nullable FlagCategory category, boolean force) {
        if (!force) {
            if (!claim.hasPermission(player, ClaimPermission.MANAGE_FLAGS) || (category != null && !player.hasPermission(Perms.FLAG_TYPE.apply(category)))) {
                Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
                return false;
            }
        }

        if (category == null) this.flagsTypeMenu.open(player, claim);
        else this.flagsMenu.open(player, claim, category);
        return true;
    }

    public boolean openMembersMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openMembersMenu(player, claim, false);
    }

    public boolean openMembersMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        if (!force) {
            if (!claim.hasPermission(player, ClaimPermission.VIEW_MEMBERS)) {
                Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
                return false;
            }
        }

        this.membersMenu.open(player, claim);
        return true;
    }

    public boolean openMemberMenu(@NotNull Player player, @NotNull Claim claim, @NotNull Member member) {
        return this.openMemberMenu(player, claim, member, false);
    }

    public boolean openMemberMenu(@NotNull Player player, @NotNull Claim claim, @NotNull Member member, boolean force) {
        if (!force) {
            if (!claim.hasPermission(player, ClaimPermission.MANAGE_MEMBERS)) {
                Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
                return false;
            }
        }

        this.memberMenu.open(player, claim, member);
        return true;
    }

    public boolean openTransferMenu(@NotNull Player player, @NotNull Claim claim) {
        return this.openTransferMenu(player, claim, false);
    }

    public boolean openTransferMenu(@NotNull Player player, @NotNull Claim claim, boolean force) {
        if (!force) {
            if (!claim.hasPermission(player, ClaimPermission.TRANSFER_CLAIM)) {
                Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
                return false;
            }
        }

        this.transferMenu.open(player, claim);
        return true;
    }
}
