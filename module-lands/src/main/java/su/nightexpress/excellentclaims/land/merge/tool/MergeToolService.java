package su.nightexpress.excellentclaims.land.merge.tool;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.core.tool.ToolService;
import su.nightexpress.excellentclaims.land.merge.MergeToolbox;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

public class MergeToolService extends ToolService {

    private final MergeToolbox toolbox;

    public MergeToolService(AdaptedKey domain, MergeToolbox toolbox) {
        super(domain);
        this.toolbox = toolbox;
    }

    public void giveMergeTool(Player player) {
        this.giveTool(player, this.toolbox.mergeTool());
    }

    public void giveSplitTool(Player player) {
        this.giveTool(player, this.toolbox.splitTool());
    }

    public void takeMergeTool(Player player) {
        this.takeTool(player, this.toolbox.mergeTool());
    }

    public void takeSplitTool(Player player) {
        this.takeTool(player, this.toolbox.splitTool());
    }
}
