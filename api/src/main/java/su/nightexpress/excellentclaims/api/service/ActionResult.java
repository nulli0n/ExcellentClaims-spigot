package su.nightexpress.excellentclaims.api.service;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public record ActionResult(boolean result,
                           ActionReason reason,
                           @Nullable MessageLocale locale,
                           @Nullable Consumer<PlaceholderContext.Builder> placeholders) {

    public static ActionResult of(boolean result) {
        return new ActionResult(result, result ? CommonReason.SUCCESS : CommonReason.FAILURE, null, null);
    }

    public static ActionResult of(boolean result, ActionReason reason) {
        return new ActionResult(result, reason, null, null);
    }

    public static ActionResult of(boolean result, MessageLocale locale) {
        return new ActionResult(result, CommonReason.SUCCESS, locale, null);
    }

    public static ActionResult of(boolean result, ActionReason reason, MessageLocale locale) {
        return new ActionResult(result, reason, locale, null);
    }

    public static ActionResult of(boolean result, MessageLocale locale,
                                  Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(result, CommonReason.SUCCESS, locale, placeholders);
    }

    public static ActionResult of(boolean result, ActionReason reason, MessageLocale locale,
                                  Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(result, reason, locale, placeholders);
    }

    public static ActionResult ok() {
        return new ActionResult(true, CommonReason.SUCCESS, null, null);
    }

    public static ActionResult ok(ActionReason reason) {
        return new ActionResult(true, reason, null, null);
    }

    public static ActionResult ok(MessageLocale locale) {
        return new ActionResult(true, CommonReason.SUCCESS, locale, null);
    }

    public static ActionResult ok(ActionReason reason, MessageLocale locale) {
        return new ActionResult(true, reason, locale, null);
    }

    public static ActionResult ok(MessageLocale locale, Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(true, CommonReason.SUCCESS, locale, placeholders);
    }

    public static ActionResult ok(ActionReason reason, MessageLocale locale,
                                  Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(true, reason, locale, placeholders);
    }

    public static ActionResult fail() {
        return new ActionResult(false, CommonReason.FAILURE, null, null);
    }

    public static ActionResult fail(ActionReason reason) {
        return new ActionResult(false, reason, null, null);
    }

    public static ActionResult fail(MessageLocale locale) {
        return new ActionResult(false, CommonReason.FAILURE, locale, null);
    }

    public static ActionResult fail(ActionReason reason, MessageLocale locale) {
        return new ActionResult(false, reason, locale, null);
    }

    public static ActionResult fail(MessageLocale locale, Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(false, CommonReason.FAILURE, locale, placeholders);
    }

    public static ActionResult fail(ActionReason reason, MessageLocale locale,
                                    Consumer<PlaceholderContext.Builder> placeholders) {
        return new ActionResult(false, reason, locale, placeholders);
    }

    public Optional<MessageLocale> feedback() {
        return Optional.ofNullable(this.locale);
    }

    public boolean handleFeedback(Consumer<MessageLocale> consumer) {
        if (this.locale != null) {
            consumer.accept(this.locale);
        }
        return this.result;
    }

    public boolean handleFeedback(BiConsumer<MessageLocale, Consumer<PlaceholderContext.Builder>> consumer) {
        if (this.locale != null) {
            Consumer<PlaceholderContext.Builder> contextPlaceholders;
            if (this.placeholders == null) {
                contextPlaceholders = ctx -> {
                };
            }
            else contextPlaceholders = this.placeholders;

            consumer.accept(this.locale, contextPlaceholders);
        }
        return this.result;
    }

    public boolean is(ActionReason reason) {
        return this.reason == reason;
    }

    public boolean success() {
        return this.result;
    }

    public boolean failure() {
        return !this.result;
    }
}
