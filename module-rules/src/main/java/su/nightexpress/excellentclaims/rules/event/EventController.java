package su.nightexpress.excellentclaims.rules.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;

public class EventController extends AbstractController {

    private final RuleRegistry       rules;
    private final ClaimRegistry      claimRegistry;
    private final ClaimPermissionAPI permissions;
    private final MessageDispatcher  dispatcher;

    private final EventRegistry eventRegistry;

    public EventController(ClaimPlugin plugin,
                           RuleRegistry rules,
                           ClaimRegistry claimRegistry,
                           ClaimPermissionAPI permissions,
                           MessageDispatcher dispatcher) {
        super(plugin);
        this.rules = rules;
        this.claimRegistry = claimRegistry;
        this.permissions = permissions;
        this.dispatcher = dispatcher;

        this.eventRegistry = new EventRegistry();
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        // Listeners are unregistered by abstract parent class.
    }

    @Override
    protected void onStart() {
        this.rules.values().forEach(this::registerRuleEvents);
    }

    public <E extends Event, T> void registerRuleEvents(RegisteredRule<E, T> rule) {
        RuleBehavior<E, T> behavior = rule.getBehavior();
        Class<E> eventType = behavior.getEventType();
        EventPriority priority = behavior.getEventPriority();

        if (!this.eventRegistry.isRegistered(eventType, priority)) {
            this.registerBukkitEvent(eventType, priority);
        }

        this.eventRegistry.bindRule(eventType, priority, rule);
    }

    private <E extends Event> void registerBukkitEvent(Class<E> eventType, EventPriority priority) {
        EventExecutor executor = (listener, event) -> {
            if (!eventType.isInstance(event)) return;

            E typedEvent = eventType.cast(event);
            this.handleEvent(typedEvent, priority);
        };

        this.plugin.getPluginManager().registerEvent(eventType, this, priority, executor, this.plugin, true);
    }

    public void handleEvent(Event event, EventPriority priority) {
        List<RegisteredRule<?, ?>> rules = this.eventRegistry.getRules(event.getClass(), priority);
        if (rules == null || rules.isEmpty()) return;

        for (RegisteredRule<?, ?> rule : rules) {
            RuleResult result = this.handleRule(rule, event);
            EventState state = result.state();

            if (state != EventState.PASS) {
                break;
            }
        }
    }

    private <E extends Event, T> RuleResult handleRule(RegisteredRule<E, T> rule, Event event) {
        RuleBehavior<E, T> behavior = rule.getBehavior();
        E typedEvent = behavior.getEventType().cast(event);

        // Rule don't want to handle this event, pass.
        if (!behavior.shouldHandle(typedEvent)) {
            return RuleResult.pass();
        }

        // There is no claim, so pass.
        Claim claim = behavior.getClaim(typedEvent, this.claimRegistry);
        if (claim == null) return RuleResult.pass();

        ClaimRules rules = claim.getRules();

        // Wilderness can have "unset" properties to ignore their behavior, pass.
        if (claim.isSupportingUnsetRules() && !rules.has(rule)) {
            return RuleResult.pass();
        }

        Player player = behavior.getUser(typedEvent);
        if (player != null && this.permissions.hasBypass(player)) {
            return RuleResult.allow();
        }

        T value = rules.getOrDefault(rule);

        RuleResult result = behavior.handle(typedEvent, this.claimRegistry, claim, rule, value);
        EventState state = result.state();
        if (state == EventState.DENY) {
            behavior.denyEvent(typedEvent);
        }
        else if (state == EventState.ALLOW) {
            behavior.allowEvent(typedEvent);
        }

        ActionResult feedback = result.feedback();
        if (player != null && feedback != null) {
            feedback.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
        }

        return result;
    }
}
