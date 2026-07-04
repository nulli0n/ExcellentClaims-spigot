package su.nightexpress.excellentclaims.land.merge.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.merge.MergeModule;
import su.nightexpress.excellentclaims.land.merge.command.impl.MergeCommand;
import su.nightexpress.excellentclaims.land.merge.command.impl.SplitCommand;
import su.nightexpress.excellentclaims.land.merge.session.SessionOrchestrator;

@NullMarked
public final class MergeCommandConfiguration {

    private MergeCommandConfiguration() {
    }

    public static void configure(MergeModule module, DependencyContainer container) {
        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        SessionOrchestrator orchestrator = container.get(SessionOrchestrator.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        commands.registerCommand(new MergeCommand(resolver, suggestions, orchestrator, dispatcher));
        commands.registerCommand(new SplitCommand(resolver, suggestions, orchestrator, dispatcher));
    }
}
