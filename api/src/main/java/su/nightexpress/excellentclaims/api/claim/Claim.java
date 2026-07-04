package su.nightexpress.excellentclaims.api.claim;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifiable;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;

@NullMarked
public interface Claim extends Identifiable, PlaceholderResolvable {

    @Deprecated
    boolean isEmpty();

    boolean isEnabled();

    boolean isDisabled();

    void setDisabled();

    void setEnabled();

    boolean isWorld(World world);

    boolean isWorld(AdaptedKey worldKey);

    boolean isBackgroundClaim();

    boolean isSupportingUnsetRules();

    boolean contains(Location location);

    boolean contains(BlockPos blockPos);

    boolean isIntersecting(Cuboid cuboid);

    <T> Optional<T> getRuleOrIgnoreIfUnset(ClaimRule<T> rule);

    AdaptedKey getWorldKey();

    Identifier getModuleKey();

    World getWorld() throws IllegalStateException;

    Optional<World> world();

    ClaimDefinition definition();

    ClaimRules getRules();

    Set<Long> getEffectiveChunkKeys();

    String getName();

    String getRawName();

    void setName(String name);

    List<String> getDescription();

    void setDescription(List<String> description);

    NightItem getIcon();

    void setIcon(NightItem icon);

    int getPriority();

    void setPriority(int priority);

    ExactPos getSpawnLocation();

    void setSpawnLocation(ExactPos pos);

    <T> String formatSummary(ClaimRule<T> rule);
}
