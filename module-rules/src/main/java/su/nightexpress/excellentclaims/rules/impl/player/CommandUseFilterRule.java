package su.nightexpress.excellentclaims.rules.impl.player;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.CommandPreProcessContext;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.spec.AbstractFilterSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.CommandUtil;
import su.nightexpress.nightcore.util.bridge.Software;

@NullMarked
public class CommandUseFilterRule extends AbstractFilterSpec<CommandPreProcessContext, Command> {

    private final ClaimPermissionAPI permissions;
    private final Set<String>        defaultBlacklist;

    public CommandUseFilterRule(ClaimPermissionAPI permissions, Set<String> defaultBlacklist) {
        super(CommandPreProcessContext.class, RuleTypes.COMMANDS, RuleCategory.PLAYER);
        this.permissions = permissions;
        this.defaultBlacklist = defaultBlacklist;
    }

    @Override
    public RuleBehavior<CommandPreProcessContext, FilteredSet<Command>> createBehavior() {
        return this.behaviorBuilder()
            .weight(50)
            .allValues(() -> getCommands())
            .shouldHandle(context -> true)
            .process((context, registry, resolver) -> {
                Player player = context.actor();
                Location location = player.getLocation();
                if (location == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(location);
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.USE_COMMANDS)) {
                    return RuleResult.allow();
                }

                FilteredSet<Command> commandList = resolver.resolveValue(claim).orElse(null);
                if (commandList != null && !commandList.isAllowed(context.command())) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Command Usage")
            .description(
                "Controls which commands",
                "are allowed for use here."
            )
            .icon(Material.COMMAND_BLOCK)
            .build();
    }

    @Override
    public FilteredSet<Command> getDefaultValue() {
        List<Command> commands = this.defaultBlacklist.stream()
            .map(name -> CommandUtil.getCommand(name).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        return FilteredSet.valued(FilterMode.BLACKLIST, commands);
    }

    private static Set<Command> getCommands() {
        return Software.get().getCommandMap().getCommands().stream()
            .distinct()
            .collect(Collectors.toSet());
    }
}
