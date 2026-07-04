package su.nightexpress.excellentclaims.region.selection;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum SelectionPosition {

    FIRST,
    SECOND;

    public SelectionPosition opposite() {
        return this == FIRST ? SECOND : FIRST;
    }
}
