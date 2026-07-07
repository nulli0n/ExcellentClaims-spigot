package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ItemStackContext {

    ItemStack itemStack();
}
