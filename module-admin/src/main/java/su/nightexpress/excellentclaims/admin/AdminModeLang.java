package su.nightexpress.excellentclaims.admin;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class AdminModeLang implements LangContainer {

    public static final TextLocale COMMAND_ADMIN_MODE_DESC = LangEntry.builder("Command.AdminMode.Desc")
        .text("Toggle Admin Mode.");

    public static final MessageLocale ADMIN_MODE_TOGGLE_ON = LangEntry.builder("AdminMode.Toggle.ON")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have toggled Admin Mode " + TagWrappers.GREEN.wrap("ON") + ".")
        );

    public static final MessageLocale ADMIN_MODE_TOGGLE_OFF = LangEntry.builder("AdminMode.Toggle.OFF")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have toggled Admin Mode " + TagWrappers.RED.wrap("OFF") + ".")
        );

    private AdminModeLang() {
    }
}
