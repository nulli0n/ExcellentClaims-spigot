package su.nightexpress.excellentclaims.api.claim.tool;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifiable;

@NullMarked
@Deprecated
public interface ClaimTool extends Identifiable {

    ItemStack getItemStack();

}
