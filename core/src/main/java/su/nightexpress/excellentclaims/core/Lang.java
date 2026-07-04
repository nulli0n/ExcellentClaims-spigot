package su.nightexpress.excellentclaims.core;

import org.bukkit.Sound;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class Lang implements LangContainer {

    public static final EnumLocale<ClaimPermission> CLAIM_PERMISSION = LangEntry.builder("Other.ClaimPermission")
        .enumeration(ClaimPermission.class);

    public static final EnumLocale<RuleCategory> RULE_CATEGORY = LangEntry.builder("Other.PropertyCategory")
        .enumeration(RuleCategory.class);

    public static final TextLocale OTHER_NO_DESCRIPTION = LangEntry.builder("Other.Claim.NoDescription")
        .text(TagWrappers.GRAY.wrap("<No Description>"));

    public static final TextLocale OTHER_UNSET      = LangEntry.builder("Other.Unset").text("< Unset >");
    public static final TextLocale OTHER_EVERYTHING = LangEntry.builder("Other.Everything").text("Everything");

    public static final TextLocale COMMAND_ARGUMENT_NAME_TEXT = LangEntry.builder("Command.Argument.Name.Text")
        .text("text");

    public static final MessageLocale GREETING_CLAIM = LangEntry.builder("Greeting.Claim")
        .titleMessage(
            CommonPlaceholders.GENERIC_NAME,
            TagWrappers.GRAY.wrap(ClaimsPlaceholders.GENERIC_DESCRIPTION),
            10, 40
        );

    public static final MessageLocale ERROR_NOT_CLAIM_OWNER = LangEntry.builder("Error.NotOwner")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Only claim owner can do that.")
        );

    public static final MessageLocale ERROR_NO_CLAIM_PERMISSION = LangEntry.builder("Core.Error.NoClaimPermission")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You are not allowed to do that.")
        );
}
