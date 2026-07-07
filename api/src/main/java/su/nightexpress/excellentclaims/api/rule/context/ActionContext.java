package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ActionContext {

    @Nullable
    @Deprecated
    default Player actor() {
        return null;
    }
}
