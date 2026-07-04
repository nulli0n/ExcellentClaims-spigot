package su.nightexpress.excellentclaims.api.highlight;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public interface HighlightCalculator {

    /**
     * Calculates the points of the target region that fall within the view bounds.
     */
    Set<HighlightPoint> calculateVisiblePoints(Cuboid selectedRegion, Cuboid view);
}