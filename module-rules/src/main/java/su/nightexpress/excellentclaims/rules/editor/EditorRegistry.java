package su.nightexpress.excellentclaims.rules.editor;

import java.util.IdentityHashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.api.rule.RuleUIEditor;

@NullMarked
public class EditorRegistry {

    private final Map<RuleType<?>, RuleUIEditor<?>> editors;

    public EditorRegistry() {
        this.editors = new IdentityHashMap<>();
    }

    public <T> void register(RuleType<T> type, RuleUIEditor<T> editor) {
        this.editors.put(type, editor);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable RuleUIEditor<T> getEditor(RuleType<T> type) {
        return (RuleUIEditor<T>) this.editors.get(type);
    }
}
