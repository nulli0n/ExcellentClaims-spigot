package su.nightexpress.excellentclaims.api.rule;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.key.KeyHolder;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;

@NullMarked
public interface ClaimRule<T> extends KeyHolder, PlaceholderResolvable {

    void update(RuleDefinition definition, T defaultValue);

    RuleType<T> getType();

    RuleBehavior<?, T> getBehavior();

    RuleCategory getCategory();

    T getDefaultValue();

    String getDisplayName();

    List<String> getDescription();

    NightItem getIcon();

    String getPermission();

    boolean hasPermission(Player player);
}
