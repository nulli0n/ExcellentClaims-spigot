package su.nightexpress.excellentclaims.region.selection.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.selection.session.SelectionSession;

@NullMarked
public class SelectionUIService {

    private final List<UIComponentExtension> components;

    public SelectionUIService() {
        this.components = new ArrayList<>();
    }

    public void addComponent(UIComponentExtension extension) {
        this.components.add(extension);
    }

    public void createSessionUI(Player player, SelectionSession session) {
        this.components.forEach(extension -> {
            session.attachComponent(player, extension.createComponent());
        });
    }

    public void clearSessionUI(Player player, SelectionSession session) {
        session.terminate(player);
    }

    public void updateSessionUI(Player player, SelectionSession session) {
        session.update(player, session.createEffectiveCuboid());
    }
}
