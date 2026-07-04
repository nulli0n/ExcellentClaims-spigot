package su.nightexpress.excellentclaims.region.selection.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.Identifiable;
import su.nightexpress.excellentclaims.region.selection.session.SelectionSession;
import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public interface SelectionUIComponent extends Identifiable {

    void onAttach(Player player, SelectionSession session);

    void onUpdate(Player player, SelectionSession session, @Nullable Cuboid view);

    void onDetach(Player player, SelectionSession session);
}
