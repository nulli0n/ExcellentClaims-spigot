package su.nightexpress.excellentclaims.api;

import java.util.Collection;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public interface MessageDispatcher {

    void send(MessageLocale locale, CommandSender sender);

    void send(MessageLocale locale, CommandSender sender,
              @Nullable Consumer<PlaceholderContext.Builder> consumer);

    void send(MessageLocale locale, CommandSender sender,
              @Nullable PlaceholderContext context);

    void send(MessageLocale locale, Collection<? extends CommandSender> receivers);

    void send(MessageLocale locale, Collection<? extends CommandSender> receivers,
              @Nullable Consumer<PlaceholderContext.Builder> consumer);

    void send(MessageLocale locale, Collection<? extends CommandSender> receivers,
              @Nullable PlaceholderContext context);

    void broadcast(MessageLocale locale);

    void broadcast(MessageLocale locale,
                   @Nullable Consumer<PlaceholderContext.Builder> consumer);

    void broadcast(MessageLocale locale, @Nullable PlaceholderContext context);
}
