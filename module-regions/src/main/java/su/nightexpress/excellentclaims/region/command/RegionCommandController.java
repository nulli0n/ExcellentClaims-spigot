package su.nightexpress.excellentclaims.region.command;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class RegionCommandController implements CommandRegistry, LifecycleComponent {

    private final ClaimPlugin            plugin;
    private final RegionSettings         settings;
    private final List<CommandExtension> extensions;

    @Nullable
    private NightCommand command;

    public RegionCommandController(ClaimPlugin plugin, RegionSettings settings) {
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
            root.description(RegionLang.COMMAND_REGION_DESC);
            root.permission(RegionPerms.COMMAND_REGION);

            this.extensions.forEach(extension -> {
                root.branch(extension.createCommand());
            });
        });

        this.command.register();
    }
}
