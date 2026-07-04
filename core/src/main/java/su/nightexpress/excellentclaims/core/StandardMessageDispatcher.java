package su.nightexpress.excellentclaims.core;

import java.util.Collection;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.Prefixed;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class StandardMessageDispatcher implements MessageDispatcher {

    private final Prefixed prefixed;

    public StandardMessageDispatcher(Prefixed prefix) {
        this.prefixed = prefix;
    }

    public LangMessage getPrefixed(MessageLocale locale) {
        return prefixed == null ? locale.value() : locale.withPrefix(prefixed.getPrefix());
    }

    public void send(MessageLocale locale, CommandSender sender) {
        this.getPrefixed(locale).send(sender);
    }

    public void send(MessageLocale locale, CommandSender sender,
                     @Nullable Consumer<PlaceholderContext.Builder> consumer) {
        this.getPrefixed(locale).sendWith(sender, consumer);
    }

    public void send(MessageLocale locale, CommandSender sender,
                     @Nullable PlaceholderContext context) {
        this.getPrefixed(locale).sendWith(sender, context);
    }

    public void send(MessageLocale locale, Collection<? extends CommandSender> receivers) {
        this.getPrefixed(locale).send(receivers);
    }

    public void send(MessageLocale locale, Collection<? extends CommandSender> receivers,
                     @Nullable Consumer<PlaceholderContext.Builder> consumer) {
        this.getPrefixed(locale).sendWith(receivers, consumer);
    }

    public void send(MessageLocale locale, Collection<? extends CommandSender> receivers,
                     @Nullable PlaceholderContext context) {
        this.getPrefixed(locale).sendWith(receivers, context);
    }

    public void broadcast(MessageLocale locale) {
        this.getPrefixed(locale).broadcast();
    }

    public void broadcast(MessageLocale locale,
                          @Nullable Consumer<PlaceholderContext.Builder> consumer) {
        this.getPrefixed(locale).broadcastWith(consumer);
    }

    public void broadcast(MessageLocale locale, @Nullable PlaceholderContext context) {
        this.getPrefixed(locale).broadcastWith(context);
    }
}
