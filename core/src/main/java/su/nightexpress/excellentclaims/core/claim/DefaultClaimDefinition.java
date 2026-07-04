package su.nightexpress.excellentclaims.core.claim;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class DefaultClaimDefinition implements ClaimDefinition {

    private String       name          = "null";
    private List<String> description   = List.of();
    private int          priority;
    private NightItem    icon          = NightItem.fromType(Material.GRASS_BLOCK);
    private ExactPos     spawnLocation = ExactPos.empty();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public NightItem getIcon() {
        return icon.copy();
    }

    public void setIcon(NightItem icon) {
        this.icon = icon.copy();
    }

    public ExactPos getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(ExactPos spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
