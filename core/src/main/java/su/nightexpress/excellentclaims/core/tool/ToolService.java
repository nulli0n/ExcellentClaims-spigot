package su.nightexpress.excellentclaims.core.tool;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.tool.ClaimTool;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public abstract class ToolService {

    //private final ToolRegistry tools;
    private final AdaptedKey domain;

    public ToolService(AdaptedKey domain) {
        //this.tools = tools;
        this.domain = domain;
    }

    /* public @Nullable ItemStack createToolStack(AdaptedKey key) {
        RegisteredTool tool = this.tools.get(key);
        return tool == null ? null : this.createToolStack(tool.tool());
    } */

    public void giveTool(Player player, ClaimTool tool) {
        ItemStack itemStack = this.createToolStack(tool);
        Players.addItem(player, itemStack);
    }

    public void takeTool(Player player, ClaimTool tool) {
        Players.takeItem(player, stack -> this.isTool(stack, tool));
    }

    public void takeAllTools(Player player) {
        Players.takeItem(player, this::isTool);
    }

    public ItemStack createToolStack(ClaimTool tool) {
        ItemStack itemStack = tool.getItemStack();
        PDCUtil.set(itemStack, this.domain.bukkit(), tool.id().value());
        return itemStack;
    }

    /* public @Nullable RegisteredTool getToolByItem(ItemStack itemStack) {
        AdaptedKey toolKey = this.getToolId(itemStack);
        return toolKey == null ? null : this.tools.get(toolKey);
    } */

    public @Nullable Identifier getToolId(ItemStack itemStack) {
        String rawkey = PDCUtil.getString(itemStack, this.domain.bukkit()).orElse(null);
        return rawkey == null ? null : Identifier.parse(rawkey).orElse(null);
    }

    public boolean isTool(ItemStack itemStack) {
        return this.getToolId(itemStack) != null;
    }

    public boolean isTool(ItemStack itemStack, Identifier toolId) {
        return toolId.equals(this.getToolId(itemStack));
    }

    public boolean isTool(ItemStack itemStack, ClaimTool tool) {
        Identifier id = this.getToolId(itemStack);
        return id != null && id.equals(tool.getId());
        /* RegisteredTool registered = this.getToolByItem(itemStack);
        return registered != null && registered.key().equals(tool.key()); */
    }
}
