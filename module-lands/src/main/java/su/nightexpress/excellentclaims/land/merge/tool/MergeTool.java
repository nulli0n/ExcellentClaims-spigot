package su.nightexpress.excellentclaims.land.merge.tool;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.tool.ClaimTool;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.land.merge.settings.MergeSettings;

@NullMarked
public class MergeTool implements ClaimTool {

    private final Identifier    id;
    private final MergeSettings settings;

    public MergeTool(Identifier id, MergeSettings settings) {
        this.id = id;
        this.settings = settings;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public ItemStack getItemStack() {
        return this.settings.getMergeTool().getItemStack();
    }
}
