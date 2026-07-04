package su.nightexpress.excellentclaims.land.merge;

import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class MergeLang implements LangContainer {

    public static final TextLocale COMMAND_LAND_MERGE_DESC = LangEntry.builder("Command.Land.Merge.Desc")
        .text("Merge chnuk to a claim.");

    public static final TextLocale COMMAND_LAND_SPLIT_DESC = LangEntry.builder("Command.Land.Split.Desc")
        .text("Split claim chunk.");

    public static final MessageLocale LAND_MERGE_ERROR_FOREIGN = LangEntry.builder("Land.Merge.Error.Foreign")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Foreign Claim!")),
            TagWrappers.GRAY.wrap("You can't merge chunks with different owners."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_MERGE_ERROR_SAME = LangEntry.builder("Land.Merge.Error.Same").titleMessage(
        TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Same Claim!")),
        TagWrappers.GRAY.wrap("You can't merge chunks of the same claim."),
        Sound.ENTITY_VILLAGER_NO
    );

    public static final MessageLocale LAND_MERGE_ERROR_DIFFERENT_WORLD = LangEntry.builder(
        "Land.Merge.Error.DifferentWorld").titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Different Worlds!")),
            TagWrappers.GRAY.wrap("You can't merge claims of different worlds."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_MERGE_ERROR_INACTIVE = LangEntry.builder("Land.Merge.Error.Inactive")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Invalid Claim!")),
            TagWrappers.GRAY.wrap("Claim selected for merge does not exist anymore."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_MERGE_ERROR_NOTHING = LangEntry.builder("Land.Merge.Error.Nothing")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("No Claim!")),
            TagWrappers.GRAY.wrap("There is no claimed chunk to merge into."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_MERGE_ERROR_MAX_CHUNKS = LangEntry.builder("Land.Merge.Error.MaxChunks")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Too Many Chunks!")),
            TagWrappers.GRAY.wrap("This claim has maximum of " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_AMOUNT) + " merged chunks."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_MERGE_SUCCESS = LangEntry.builder("Land.Merge.Success").titleMessage(
        TagWrappers.GREEN.wrap(TagWrappers.BOLD.wrap("Claims Merged!")),
        TagWrappers.GRAY.wrap("You successfully merged the claimed chunks."),
        Sound.ENTITY_ENDERMAN_TELEPORT
    );

    public static final MessageLocale LAND_MERGE_INFO = LangEntry.builder("Land.Merge.Info").chatMessage(
        TagWrappers.GRAY.wrap("Select a claim where you want to merge this claim chunks by using the " +
            TagWrappers.YELLOW.wrap("Merge Tool") +
            ".")
    );

    public static final MessageLocale LAND_SPLIT_INFO = LangEntry.builder("Land.Separate.Info").chatMessage(
        TagWrappers.GRAY.wrap("Select a chunk you want to split from this claim by using the " +
            TagWrappers.YELLOW.wrap("Split Tool") + ".")
    );

    public static final MessageLocale LAND_SPLIT_ERROR_NOTHING = LangEntry.builder("Land.Separate.Error.Nothing")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("No Claim!")),
            TagWrappers.GRAY.wrap("There is no claimed chunk to split."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_SPLIT_ERROR_DIFFERENT = LangEntry.builder("Land.Separate.Error.Different")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Different Claim!")),
            TagWrappers.GRAY.wrap("This chunk is not from current claim."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_SPLIT_ERROR_NOT_MERGED = LangEntry.builder("Land.Separate.Error.NotMerged")
        .titleMessage(
            TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Not Merged!")),
            TagWrappers.GRAY.wrap("This claim has single chunk only."),
            Sound.ENTITY_VILLAGER_NO
        );

    public static final MessageLocale LAND_SPLIT_SUCCESS = LangEntry.builder("Land.Separate.Success").titleMessage(
        TagWrappers.GREEN.wrap(TagWrappers.BOLD.wrap("Chunk Split!")),
        TagWrappers.GRAY.wrap("You successfully split chunk as new claim."),
        Sound.BLOCK_PISTON_EXTEND
    );

    private MergeLang() {
    }
}
