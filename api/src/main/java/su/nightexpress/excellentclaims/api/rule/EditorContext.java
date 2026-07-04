package su.nightexpress.excellentclaims.api.rule;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class EditorContext {

    private final InventoryClickEvent event;
    private final Runnable            dirtyFlag;

    @Nullable
    private final Runnable callback;

    public EditorContext(InventoryClickEvent event, Runnable dirtyFlag, @Nullable Runnable callback) {
        this.event = event;
        this.dirtyFlag = dirtyFlag;
        this.callback = callback;
    }

    public InventoryClickEvent getEvent() {
        return this.event;
    }

    public void markDirty() {
        this.dirtyFlag.run();
    }

    public void callback() {
        if (this.callback != null) {
            this.callback.run();
        }
    }
}
