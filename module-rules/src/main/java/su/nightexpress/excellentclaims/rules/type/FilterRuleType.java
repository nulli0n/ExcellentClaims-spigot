package su.nightexpress.excellentclaims.rules.type;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.api.rule.filter.ElementDisplay;
import su.nightexpress.excellentclaims.api.rule.filter.StringAdapter;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.filter.codec.FilterTypeCodec;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class FilterRuleType<E> implements RuleType<FilteredSet<E>> {

    private final StringAdapter<E>            adapter;
    private final ElementDisplay<E>           display;
    private final ConfigCodec<FilteredSet<E>> codec;

    public FilterRuleType(StringAdapter<E> adapter, ElementDisplay<E> display) {
        this.adapter = adapter;
        this.display = display;
        this.codec = new FilterTypeCodec<>(adapter);
    }

    public StringAdapter<E> getAdapter() {
        return this.adapter;
    }

    public ElementDisplay<E> getDisplay() {
        return this.display;
    }

    @Override
    public ConfigCodec<FilteredSet<E>> getCodec() {
        return this.codec;
    }

    @Override
    public String formatSummary(FilteredSet<E> value) {
        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(ClaimsPlaceholders.GENERIC_TYPE, () -> RulesLang.FILTER_MODE.getLocalized(value.getMode()))
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(value.size()))
            .build();

        return placeholders.apply(RulesLang.FILTER_SUMMARY.text());
    }
}
