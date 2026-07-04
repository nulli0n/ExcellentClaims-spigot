package su.nightexpress.excellentclaims.rules.ui.menu;

import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;

public record RuleListContext(Claim claim,
                              Runnable dirtyFlag,
                              @Nullable RuleCategory category,
                              @Nullable Runnable onBack) {

    public void markDirty() {
        this.dirtyFlag.run();
    }

    public void moveBack() {
        if (this.onBack == null) return;

        this.onBack.run();
    }
}
