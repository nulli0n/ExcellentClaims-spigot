package su.nightexpress.excellentclaims.land.merge.settings;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class MergeSettingsSchema {

    public static final ConfigProperty<Integer> SESSION_TIME_OUT = ConfigProperty.of(
        ConfigCodecs.INT,
        "Session.Timeout",
        30,
        ""
    );

    public static final ConfigProperty<NightItem> MERGE_TOOL = ConfigProperty.of(
        ConfigCodecs.NIGHT_ITEM,
        "Tools.Merge.Item",
        getDefaultMergeItem(),
        ""
    );

    public static final ConfigProperty<NightItem> SPLIT_TOOL = ConfigProperty.of(
        ConfigCodecs.NIGHT_ITEM,
        "Tools.Split.Item",
        getDefaultSeparateItem(),
        ""
    );

    private MergeSettingsSchema() {
    }

    private static NightItem getDefaultMergeItem() {
        return new NightItem(Material.STICK)
            .setDisplayName(TagWrappers.GOLD.wrap(TagWrappers.BOLD.wrap("Merge Tool")))
            .setLore(Lists.newList(
                TagWrappers.DARK_GRAY.wrap("(Drop to exit merge mode)"),
                "",
                TagWrappers.GOLD.wrap("[▶] ") + TagWrappers.GRAY.wrap("Click to " + TagWrappers.GOLD.wrap(
                    "select chunk") + " .")
            ));
    }


    private static NightItem getDefaultSeparateItem() {
        return new NightItem(Material.STICK)
            .setDisplayName(TagWrappers.RED.wrap(TagWrappers.BOLD.wrap("Separation Tool")))
            .setLore(Lists.newList(
                TagWrappers.DARK_GRAY.wrap("(Drop to exit separation mode)"),
                "",
                TagWrappers.RED.wrap("[▶] ") + TagWrappers.GRAY.wrap("Click to " + TagWrappers.RED.wrap(
                    "select chunk") + " .")
            ));
    }
}
