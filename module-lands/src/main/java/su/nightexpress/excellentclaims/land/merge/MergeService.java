package su.nightexpress.excellentclaims.land.merge;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.event.land.LandClaimMergeEvent;
import su.nightexpress.excellentclaims.api.event.land.LandClaimMergedEvent;
import su.nightexpress.excellentclaims.api.event.land.LandClaimSplitEvent;
import su.nightexpress.excellentclaims.api.event.land.LandClaimSplittedEvent;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.excellentclaims.land.LandIDUtil;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class MergeService {

    private final ClaimPlugin        plugin;
    private final LandSettings       settings;
    private final LandDataService    dataService;
    private final ClaimPermissionAPI permissions;

    public MergeService(ClaimPlugin plugin,
                        LandSettings settings,
                        LandDataService dataService,
                        ClaimPermissionAPI permissions) {
        this.plugin = plugin;
        this.settings = settings;
        this.dataService = dataService;
        this.permissions = permissions;
    }

    public ActionResult canStartMerge(Player player, LandClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.MERGE_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }
        return ActionResult.ok();
    }

    public ActionResult canStartSplit(Player player, LandClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.SPLIT_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }
        return ActionResult.ok();
    }

    public ActionResult separateChunk(Player player, LandClaim source, LandClaim target, ChunkPos chunkPos) {
        if (!this.permissions.hasPermission(player, source, ClaimPermission.SPLIT_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (target != source) {
            return ActionResult.fail(MergeLang.LAND_SPLIT_ERROR_DIFFERENT);
        }

        if (source.isSingle()) {
            return ActionResult.fail(MergeLang.LAND_SPLIT_ERROR_NOT_MERGED);
        }

        LandClaimSplitEvent separateEvent = new LandClaimSplitEvent(source, player, chunkPos);
        this.plugin.getPluginManager().callEvent(separateEvent);
        if (separateEvent.isCancelled()) {
            return ActionResult.fail(); // TODO Messsage
        }

        source.getChunkData().removeChunkPosition(chunkPos);

        this.dataService.markDirty(source);
        this.dataService.updateClaim(source);

        World world = player.getWorld();
        Identifier id = LandIDUtil.generateId(player, chunkPos);
        LandClaim land = this.dataService.createClaim(id, player, world, chunkPos);

        // Inheritance settings
        land.setName(source.getName() + " - Copy");
        land.setIcon(source.getIcon());
        land.setPriority(source.getPriority());

        source.getMembers().forEach(sourceMember -> {
            land.addMember(new DefaultClaimMember(sourceMember.getPlayerId(), sourceMember.getRank()));
        });
        // TODO Rules Inherit land.getFlags().putAll(source.getFlags());

        LandClaimSplittedEvent separatedEvent = new LandClaimSplittedEvent(source, player, land);
        this.plugin.getPluginManager().callEvent(separatedEvent);

        return ActionResult.ok(MergeLang.LAND_SPLIT_SUCCESS, ctx -> ctx.with(land.placeholders()));
    }

    public ActionResult mergeChunk(Player player, LandClaim source, LandClaim target) {
        if (!this.permissions.hasPermission(player, target, ClaimPermission.MERGE_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (!source.isOwner(player) || !target.isOwner(player)) {
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_FOREIGN);
        }

        if (source == target) {
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_SAME);
        }

        if (!source.isWorld(target.getWorldKey())) {
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_DIFFERENT_WORLD);
        }

        int maxChunks = this.settings.getMaxClaimSize(player);
        if (maxChunks >= 0 && target.getChunksAmount() >= maxChunks) {
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_MAX_CHUNKS, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> String.valueOf(maxChunks))
            );
        }

        LandClaimMergeEvent mergeEvent = new LandClaimMergeEvent(source, player, target);
        this.plugin.getPluginManager().callEvent(mergeEvent);
        if (mergeEvent.isCancelled()) {
            return ActionResult.fail(); // TODO
        }

        source.getMembers().forEach(target::addMember);
        target.getChunkData().getChunkPositions().addAll(source.getPositions());

        this.dataService.markDirty(target);
        this.dataService.deleteClaim(source);
        this.dataService.updateClaim(target);

        LandClaimMergedEvent mergedEvent = new LandClaimMergedEvent(source, player, target);
        this.plugin.getPluginManager().callEvent(mergedEvent);

        return ActionResult.ok(MergeLang.LAND_MERGE_SUCCESS, ctx -> ctx.with(target.placeholders()));
    }
}
