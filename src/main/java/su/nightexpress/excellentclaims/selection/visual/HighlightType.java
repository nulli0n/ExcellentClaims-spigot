package su.nightexpress.excellentclaims.selection.visual;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum HighlightType {

    CHUNK_INTERSECT(true),
    SELECTION_INTERSECT(true),
    CHUNK(CHUNK_INTERSECT),
    SELECTION(SELECTION_INTERSECT);
    //INTERSECT

    private final boolean intersect;
    private final HighlightType intersectType;

//    HighlightType() {
//        this(false, null);
//    }

    HighlightType(boolean intersect) {
        this(intersect, null);
    }

    HighlightType(@NotNull HighlightType intersectType) {
        this(false, intersectType);
    }

    HighlightType(boolean intersect, @Nullable HighlightType intersectType) {
        this.intersect = intersect;
        this.intersectType = intersectType;
    }

    public boolean isIntersect() {
        return this.intersect;
    }

    public HighlightType getIntersectType() {
        return this.intersectType;
    }
}
