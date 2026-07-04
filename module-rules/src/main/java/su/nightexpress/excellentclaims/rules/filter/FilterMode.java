package su.nightexpress.excellentclaims.rules.filter;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum FilterMode {

    WHITELIST("whitelist"),
    BLACKLIST("blacklist"),
    ;

    private final String id;

    FilterMode(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }
}
