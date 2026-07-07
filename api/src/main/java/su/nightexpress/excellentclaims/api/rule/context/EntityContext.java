package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EntityContext {

    Entity entity();
}
