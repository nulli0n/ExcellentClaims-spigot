package su.nightexpress.excellentclaims.land.command;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class LandCommandController implements CommandRegistry, LifecycleComponent {

    private final ClaimPlugin            plugin;
    private final LandSettings           settings;
    private final List<CommandExtension> extensions;

    @Nullable
    private NightCommand command;

    public LandCommandController(ClaimPlugin plugin, LandSettings settings) {
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
            root.description(LandLang.COMMAND_LAND_DESC);
            root.permission(LandPerms.COMMAND_LAND);

            this.extensions.forEach(extension -> {
                root.branch(extension.createCommand());
            });
        });

        this.command.register();
    }
}
