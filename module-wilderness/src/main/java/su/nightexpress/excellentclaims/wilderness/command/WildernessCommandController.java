package su.nightexpress.excellentclaims.wilderness.command;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.permission.WildernessPerms;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class WildernessCommandController implements CommandRegistry, LifecycleComponent {

    private final ClaimPlugin            plugin;
    private final WildernessSettings     settings;
    private final List<CommandExtension> extensions;

    @Nullable
    private NightCommand command;

    public WildernessCommandController(ClaimPlugin plugin, WildernessSettings settings) {
        this.plugin = plugin;
        this.settings = settings;

        this.extensions = new ArrayList<>();
    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {
        if (this.command != null) {
            this.command.unregister();
        }
        this.extensions.clear();
    }

    @Override
    public void start() {
        this.load();
    }

    @Override
    public void registerCommand(CommandExtension extension) {
        this.extensions.add(extension);
    }

    public void load() {
        String[] aliases = this.settings.getCommandAliases();

        this.command = NightCommand.hub(this.plugin, aliases, root -> {
            root.description(WildernessLang.COMMAND_WILDERNESS_DESC);
            root.permission(WildernessPerms.COMMAND_REGION);

            this.extensions.forEach(extension -> {
                root.branch(extension.createCommand());
            });
        });

        this.command.register();
    }
}
