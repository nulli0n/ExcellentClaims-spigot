package su.nightexpress.excellentclaims.api.highlight;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record HighlightDelta(Set<HighlightPoint> toAdd, Set<HighlightPoint> toRemove) {

    public boolean hasChanges() {
        return !toAdd.isEmpty() || !toRemove.isEmpty();
    }
}