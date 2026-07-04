package su.nightexpress.excellentclaims.land.merge.tool;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.merge.session.SessionOrchestrator;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class MergeToolController extends AbstractController {

    private final MergeToolService    toolService;
    private final SessionOrchestrator sessionOrchestrator;
    private final MessageDispatcher   dispatcher;

    // TODO Listen for interaction event instead of relying on global tool controller

    public MergeToolController(ClaimPlugin plugin,
                               MergeToolService toolService,
                               SessionOrchestrator sessionOrchestrator,
                               MessageDispatcher dispatcher) {
        super(plugin);
        this.toolService = toolService;
        this.sessionOrchestrator = sessionOrchestrator;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }

    /* public boolean startMerge(Player player, MergeType type) {
        LandClaim claim = this.repository.getPrioritizedClaim(player.getLocation());
    
        if (claim == null) {
            this.messages.sendPrefixed(LandLang.ERROR_NO_CHUNK, player);
            return false;
        }
    
        return this.startMerge(player, type, claim);
    } */

    public void handleToolUse(Player player, Location location, Action action) {
        // TODO Validate tool
        BlockPos blockPos = BlockPos.from(location);
        ActionResult result = this.sessionOrchestrator.processToolInteract(player, blockPos);

        result.handleFeedback(locale -> this.dispatcher.send(locale, player));
    }
}
