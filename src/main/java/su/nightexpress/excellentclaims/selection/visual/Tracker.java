package su.nightexpress.excellentclaims.selection.visual;

import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

public class Tracker {

    private BlockPos previousPos;
    private boolean  chunkBounds;
    private boolean  selection;

    public Tracker() {
        this.previousPos = null;
        this.chunkBounds = false;
        this.selection = false;
    }

    @Nullable
    public BlockPos getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(@Nullable BlockPos previousPos) {
        this.previousPos = previousPos;
    }

    public boolean isChunkBounds() {
        return chunkBounds;
    }

    public void setChunkBounds(boolean chunkBounds) {
        this.chunkBounds = chunkBounds;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
