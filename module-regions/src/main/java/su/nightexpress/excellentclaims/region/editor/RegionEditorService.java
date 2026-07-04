package su.nightexpress.excellentclaims.region.editor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.api.service.CommonReason;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class RegionEditorService {

    private final RegionSettings     settings;
    private final RegionDataService  dataService;
    private final ClaimPermissionAPI permissions;

    public RegionEditorService(RegionSettings settings,
                               RegionDataService dataService,
                               ClaimPermissionAPI permissions) {
        this.settings = settings;
        this.dataService = dataService;
        this.permissions = permissions;
    }

    public ActionResult canRename(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.RENAME_CLAIM)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canChangeDescription(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_DESCRIPTION)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canChangeIcon(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_ICON)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canChangePriority(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_PRIORITY)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canChangeSpawnLocation(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_SPAWN)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canUseHomeTeleport(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.TELEPORT)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult setSpawnLocation(Player player, RegionClaim claim, Location location) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_SPAWN)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (!claim.contains(location)) {
            return ActionResult.fail(RegionLang.EDITOR_SET_SPAWN_OUTSIDE);
        }

        claim.setSpawnLocation(ExactPos.from(location));
        this.dataService.markDirty(claim);

        return ActionResult.ok(RegionLang.EDITOR_SET_SPAWN_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }

    public ActionResult setName(Player player, RegionClaim claim, String name) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.RENAME_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (!player.hasPermission(RegionPerms.BYPASS_EDITOR_NAME_LENGTH)) {
            String raw = NightMessage.stripTags(name);
            int maxLength = this.settings.getRegionNameMaxLength();
            if (maxLength > 0 && raw.length() > maxLength) {
                return ActionResult.fail(RegionLang.EDITOR_SET_NAME_TOO_LONG, ctx -> ctx
                    .with(ClaimsPlaceholders.GENERIC_MAX, () -> String.valueOf(maxLength))
                );
            }
        }

        claim.setName(name);
        this.dataService.markDirty(claim);

        return ActionResult.ok(RegionLang.EDITOR_SET_NAME_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }

    public ActionResult setDescription(Player player, RegionClaim claim, String raw) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_DESCRIPTION)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (!player.hasPermission(RegionPerms.BYPASS_EDITOR_DESCRIPTION_LENGTH)) {
            int maxLength = this.settings.getRegionDescriptionMaxLength();
            if (maxLength > 0 && raw.length() > maxLength) {
                return ActionResult.fail(RegionLang.EDITOR_SET_DESCRIPTION_TOO_LONG, ctx -> ctx
                    .with(ClaimsPlaceholders.GENERIC_MAX, () -> String.valueOf(maxLength))
                );
            }
        }

        List<String> description = new ArrayList<>();
        StringUtil.splitDelimiters(raw, description::add);

        claim.setDescription(description);
        this.dataService.markDirty(claim);

        return ActionResult.ok(RegionLang.EDITOR_SET_DESCRIPTION_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }

    public ActionResult setIcon(Player player, RegionClaim claim, NightItem icon) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_ICON)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        claim.setIcon(icon);
        this.dataService.markDirty(claim);

        return ActionResult.ok(RegionLang.EDITOR_SET_ICON_SUCCESS, ctx -> ctx
            .with(claim.placeholders())
        );
    }

    public ActionResult setPriority(Player player, RegionClaim claim, int priority) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SET_CLAIM_PRIORITY)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        int minValue = this.settings.getRegionPriorityMinValue();
        int maxValue = this.settings.getRegionPriorityMaxValue();
        if (priority < minValue || priority > maxValue) {
            return ActionResult.fail(RegionLang.EDITOR_PRIORITY_NOT_IN_BOUNDS, ctx -> ctx
                .with(ClaimsPlaceholders.GENERIC_MIN, () -> String.valueOf(minValue))
                .with(ClaimsPlaceholders.GENERIC_MAX, () -> String.valueOf(maxValue))
            );
        }

        claim.setPriority(priority);
        this.dataService.markDirty(claim);

        return ActionResult.ok(RegionLang.EDITOR_SET_PRIORITY_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }

    public ActionResult teleport(Player player, RegionClaim claim, boolean force) {
        ActionResult check = this.canUseHomeTeleport(player, claim);
        if (!check.success()) return check;

        if (!claim.isEnabled()) {
            return ActionResult.fail(RegionLang.ERROR_REGION_DISABLED);
        }

        World world = claim.getWorld();
        Location location = LocationUtil.setCenter2D(claim.getSpawnLocation().toLocation(world));

        if (!player.teleport(location)) {
            return ActionResult.fail(RegionLang.REGION_TELEPORT_CANCELLED);
        }

        return ActionResult.ok(RegionLang.REGION_TELEPORT_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }
}
