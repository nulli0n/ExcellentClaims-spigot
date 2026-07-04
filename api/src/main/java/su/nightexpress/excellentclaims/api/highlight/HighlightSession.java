package su.nightexpress.excellentclaims.api.highlight;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

public class HighlightSession {

    private final HighlightCalculator            calculator;
    private final Set<HighlightPoint>            activeHighlights;
    private final Map<BlockPos, EntityReference> entityReferences;

    public HighlightSession(HighlightCalculator calculator) {
        this.calculator = calculator;
        this.activeHighlights = new HashSet<>();
        this.entityReferences = new ConcurrentHashMap<>();
    }

    /**
     * Updates the player's view bounds and returns the precise block changes.
     */
    public HighlightDelta update(Cuboid selectedRegion, Cuboid view) {
        Set<HighlightPoint> visiblePoints = this.calculator.calculateVisiblePoints(selectedRegion, view);

        Set<HighlightPoint> toAdd = new HashSet<>(visiblePoints);
        toAdd.removeAll(activeHighlights);

        Set<HighlightPoint> toRemove = new HashSet<>(activeHighlights);
        toRemove.removeAll(visiblePoints);

        // Update internal state
        activeHighlights.clear();
        activeHighlights.addAll(visiblePoints);

        return new HighlightDelta(toAdd, toRemove);
    }

    /**
     * Cleans up all highlights when the session ends.
     */
    public void terminate() {
        this.activeHighlights.clear();
        this.entityReferences.values();
    }

    public Set<EntityReference> getEntityReferences() {
        return Set.copyOf(this.entityReferences.values());
    }

    public @Nullable EntityReference getEntityReference(BlockPos blockPos) {
        return this.entityReferences.get(blockPos);
    }

    public void addEntityReference(EntityReference reference) {
        this.entityReferences.put(reference.pos(), reference);
    }

    public void removeEntityReference(BlockPos blockPos) {
        this.entityReferences.remove(blockPos);
    }
}