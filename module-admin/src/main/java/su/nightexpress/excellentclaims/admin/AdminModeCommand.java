package su.nightexpress.excellentclaims.admin;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class AdminModeCommand implements LifecycleComponent {

    private final ClaimPlugin        plugin;
    private final AdminBypassService service;

    @Nullable
    private NightCommand command;

    public AdminModeCommand(ClaimPlugin plugin, AdminBypassService service) {
        this.plugin = plugin;
        this.service = service;
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
        this.command = NightCommand.literal(this.plugin, "claimadmin", builder -> builder
            .description(AdminModeLang.COMMAND_ADMIN_MODE_DESC)
            .permission(AdminModePerms.COMMAND_ADMIN_MODE)
            .executes((context, arguments) -> {
                Player player = context.getPlayerOrThrow();
                boolean state = this.service.isAdminMode(player);
                this.service.setAdminMode(player, !state);

                if (this.service.isAdminMode(player)) {
                    AdminModeLang.ADMIN_MODE_TOGGLE_ON.message().send(player);
                }
                else {
                    AdminModeLang.ADMIN_MODE_TOGGLE_OFF.message().send(player);
                }

                return true;
            })
        );

        this.command.register();
    }
}
