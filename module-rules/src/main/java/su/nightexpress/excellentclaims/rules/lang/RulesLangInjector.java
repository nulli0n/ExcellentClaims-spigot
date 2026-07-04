package su.nightexpress.excellentclaims.rules.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;

@NullMarked
public final class RulesLangInjector {

    private RulesLangInjector() {
    }

    public static void inject(ClaimPlugin plugin) {
        plugin.injectLang(RulesLang.class);
    }
}
