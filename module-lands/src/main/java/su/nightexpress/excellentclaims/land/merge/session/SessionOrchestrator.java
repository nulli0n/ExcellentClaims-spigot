package su.nightexpress.excellentclaims.land.merge.session;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.land.MergeType;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.merge.MergeLang;
import su.nightexpress.excellentclaims.land.merge.MergeService;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class SessionOrchestrator {

    private final ClaimRepository<LandClaim> repository;
    private final SessionService             sessionService;
    private final MergeService               mergeService;

    public SessionOrchestrator(ClaimRepository<LandClaim> repository,
                               SessionService sessionService,
                               MergeService mergeService) {
        this.repository = repository;
        this.sessionService = sessionService;
        this.mergeService = mergeService;
    }

    public ActionResult startMerging(Player player, LandClaim claim) {
        ActionResult validation = this.mergeService.canStartMerge(player, claim);
        if (!validation.success()) return validation;

        // Give tools and start session
        this.sessionService.startSession(player, claim, MergeType.MERGE);

        return ActionResult.ok(MergeLang.LAND_MERGE_INFO);
    }

    public ActionResult startSplitting(Player player, LandClaim claim) {
        ActionResult validation = this.mergeService.canStartSplit(player, claim);
        if (!validation.success()) return validation;

        if (claim.isSingle()) {
            return ActionResult.fail(MergeLang.LAND_SPLIT_ERROR_NOT_MERGED);
        }

        // Give tools and start session
        this.sessionService.startSession(player, claim, MergeType.SPLIT);

        return ActionResult.ok(MergeLang.LAND_SPLIT_INFO);
    }

    public ActionResult processToolInteract(Player player, BlockPos blockPos) {
        Session session = this.sessionService.getActiveSession(player);
        if (session == null) return ActionResult.fail();

        LandClaim source = session.getClaim();
        MergeType type = session.getType();

        // Validate Session Claim State
        if (source.isDisabled() || source.isEmpty()) {
            this.sessionService.endSession(player);
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_INACTIVE);
        }

        // Delegate to Domain Service
        ActionResult result;
        if (type == MergeType.MERGE) {
            result = this.mergeChunk(player, source, blockPos);
        }
        else {
            result = this.separateChunk(player, source, blockPos);
        }

        // Workflow State Management
        if (result.success()) {
            this.sessionService.endSession(player);
        }

        return result;
    }

    private ActionResult mergeChunk(Player player, LandClaim source, BlockPos blockPos) {
        World world = player.getWorld();
        LandClaim target = this.repository.getPrioritizedClaim(world, blockPos);

        if (target == null) {
            return ActionResult.fail(MergeLang.LAND_MERGE_ERROR_NOTHING);
        }

        return this.mergeService.mergeChunk(player, source, target);
    }

    private ActionResult separateChunk(Player player, LandClaim source, BlockPos blockPos) {
        World world = player.getWorld();
        ChunkPos chunkPos = blockPos.toChunkPos();
        LandClaim target = this.repository.getPrioritizedClaim(world, blockPos);

        if (target == null) {
            return ActionResult.fail(MergeLang.LAND_SPLIT_ERROR_NOTHING);
        }

        return this.mergeService.separateChunk(player, source, target, chunkPos);
    }
}