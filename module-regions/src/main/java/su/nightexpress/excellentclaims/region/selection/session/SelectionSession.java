package su.nightexpress.excellentclaims.region.selection.session;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.IdentifiableRegistry;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.region.selection.SelectionPosition;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIComponent;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class SelectionSession {

    private final IdentifiableRegistry<SelectionUIComponent> components;

    private boolean paused;

    @Nullable
    private BlockPos first;

    @Nullable
    private BlockPos second;

    public SelectionSession() {
        this.components = new IdentifiableRegistry<>();
    }

    public void attachComponent(Player player, SelectionUIComponent component) {
        this.components.register(component);
        component.onAttach(player, this);
    }

    public void detachComponent(Player player, Identifier componentId) {
        SelectionUIComponent component = this.components.remove(componentId);
        if (component != null) {
            component.onDetach(player, this);
        }
    }

    public void update(Player player, @Nullable Cuboid view) {
        for (SelectionUIComponent component : this.components.values()) {
            component.onUpdate(player, this, view);
        }
    }

    public void terminate(Player player) {
        for (SelectionUIComponent component : this.components.values()) {
            component.onDetach(player, this);
        }
        this.components.clear();
    }

    public @Nullable Cuboid createCompletedCuboid() {
        BlockPos first = this.getFirst();
        BlockPos second = this.getSecond();
        if (first == null || second == null) return null;

        return new Cuboid(second, first);
    }

    public @Nullable Cuboid createEffectiveCuboid() {
        BlockPos first = this.getFirst();
        BlockPos second = this.getSecond();

        BlockPos p1 = first == null ? second : first;
        BlockPos p2 = second == null ? first : second;
        if (p1 == null || p2 == null) return null;

        return new Cuboid(p1, p2);
    }

    public @Nullable BlockPos getPosition(SelectionPosition position) {
        return position == SelectionPosition.FIRST ? this.getFirst() : this.getSecond();
    }

    public void setPosition(SelectionPosition position, @Nullable BlockPos blockPos) {
        if (position == SelectionPosition.FIRST) {
            this.setFirst(blockPos);
        }
        else {
            this.setSecond(blockPos);
        }
    }

    public @Nullable BlockPos getFirst() {
        return first;
    }

    public void setFirst(@Nullable BlockPos first) {
        this.first = first;
    }

    public @Nullable BlockPos getSecond() {
        return second;
    }

    public void setSecond(@Nullable BlockPos second) {
        this.second = second;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
