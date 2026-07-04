package su.nightexpress.excellentclaims.core.claim.base;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public abstract class AbstractClaim implements Claim {

    protected final ClaimIdentity   identity;
    protected final ClaimDefinition definition;
    protected final ClaimRules      rules;

    private boolean disabled;

    public AbstractClaim(ClaimIdentity identity, ClaimDefinition definition, ClaimRules rules) {
        this.identity = identity;
        this.definition = definition;
        this.rules = rules;
    }

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public boolean isEnabled() {
        return !this.isDisabled();
    }

    @Override
    public void setDisabled() {
        this.disabled = true;
    }

    @Override
    public void setEnabled() {
        this.disabled = false;
    }

    @Override
    public boolean isWorld(AdaptedKey worldKey) {
        return this.getWorldKey().equals(worldKey);
    }

    @Override
    public boolean isWorld(World world) {
        return this.isWorld(BukkitKeys.getKey(world));
    }

    @Override
    public World getWorld() {
        return this.world()
            .orElseThrow(() -> new IllegalStateException("Claim world is null! Was the claim unloaded?"));
    }

    @Override
    public Optional<World> world() {
        return Optional.ofNullable(Bukkit.getWorld(this.getWorldKey().bukkit()));
    }

    @Override
    public <T> Optional<T> getRuleOrIgnoreIfUnset(ClaimRule<T> rule) {
        if (!this.rules.has(rule) && this.isSupportingUnsetRules()) return Optional.empty();

        return Optional.of(this.rules.getOrDefault(rule));
    }

    @Override
    public Identifier getId() {
        return this.identity.idKey();
    }

    @Override
    public AdaptedKey getWorldKey() {
        return this.identity.worldKey();
    }

    @Override
    public Identifier getModuleKey() {
        return this.identity.moduleKey();
    }

    @Override
    public ClaimDefinition definition() {
        return this.definition;
    }

    @Override
    public ClaimRules getRules() {
        return this.rules;
    }

    @Override
    public String getName() {
        return this.definition.getName();
    }

    @Override
    public String getRawName() {
        return NightMessage.stripTags(this.getName());
    }

    @Override
    public void setName(String name) {
        this.definition.setName(name);
    }

    @Override
    public List<String> getDescription() {
        return this.definition.getDescription();
    }

    @Override
    public void setDescription(List<String> description) {
        this.definition.setDescription(description);
    }

    @Override
    public NightItem getIcon() {
        return this.definition.getIcon();
    }

    @Override
    public void setIcon(NightItem icon) {
        this.definition.setIcon(icon);
    }

    @Override
    public int getPriority() {
        return this.definition.getPriority();
    }

    @Override
    public void setPriority(int priority) {
        this.definition.setPriority(priority);
    }

    @Override
    public ExactPos getSpawnLocation() {
        return this.definition.getSpawnLocation();
    }

    @Override
    public void setSpawnLocation(ExactPos pos) {
        this.definition.setSpawnLocation(pos);
    }

    @Override
    public <T> String formatSummary(ClaimRule<T> rule) {
        if (this.isSupportingUnsetRules() && !this.rules.has(rule)) {
            return Lang.OTHER_UNSET.text();
        }

        T value = this.rules.getOrDefault(rule);

        return rule.getType().formatSummary(value);
    }
}
