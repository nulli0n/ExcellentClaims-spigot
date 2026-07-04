package su.nightexpress.excellentclaims.api.claim;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public interface ClaimDefinition {

    String getName();

    void setName(String name);

    List<String> getDescription();

    void setDescription(List<String> description);

    int getPriority();

    void setPriority(int priority);

    NightItem getIcon();

    void setIcon(NightItem icon);

    ExactPos getSpawnLocation();

    void setSpawnLocation(ExactPos spawnLocation);
}
