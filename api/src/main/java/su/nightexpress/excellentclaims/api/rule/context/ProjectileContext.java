package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.entity.Projectile;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ProjectileContext {

    Projectile projectile();
}
