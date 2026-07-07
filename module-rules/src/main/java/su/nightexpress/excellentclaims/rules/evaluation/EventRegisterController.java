package su.nightexpress.excellentclaims.rules.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.api.rule.EventController;

public class EventRegisterController extends AbstractController {

    private final EventControllerService controllerService;
    private final List<EventController>  registeredControllers;

    public EventRegisterController(ClaimPlugin plugin, EventControllerService controllerService) {
        super(plugin);
        this.controllerService = controllerService;
        this.registeredControllers = new ArrayList<>();
    }

    @Override
    protected void onReload() {
        // Nothing
    }

    @Override
    protected void onShutdown() {
        this.registeredControllers.forEach(controller -> {
            HandlerList.unregisterAll(controller);
        });
        this.registeredControllers.clear();
    }

    @Override
    protected void onStart() {
        this.controllerService.createControllers().forEach(controller -> {
            this.plugin.registerListener(controller);
            this.registeredControllers.add(controller);
        });
    }
}
