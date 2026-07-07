package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public interface ActorContext {

    @Nullable
    Player actor();
}
