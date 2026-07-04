package su.nightexpress.excellentclaims.engine.command;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class ReloadCommand implements LifecycleComponent {

    private final ClaimPlugin plugin;

    @Nullable
    private NightCommand command;

    public ReloadCommand(ClaimPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {
        if (this.command != null) {
            this.command.unregister();
        }
    }

    @Override
    public void start() {
        this.command = NightCommand.literal(this.plugin, "claimsreload", builder -> builder
            .description(CoreLang.COMMAND_RELOAD_DESC)
            .permission(Perms.COMMAND_RELOAD)
            .executes((context, arguments) -> {
                this.plugin.reloadPlugin();
                CoreLang.PLUGIN_RELOADED.withPrefix(this.plugin).send(context.getSender());
                return true;
            })
        );
        this.command.register();
    }

}
