package su.nightexpress.excellentclaims.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ClaimUtils {

    @NotNull
    public static String createChunkClaimID(@NotNull Player player, @NotNull ChunkPos chunkPos) {
        // Timestamp to make sure no duplicated IDs were produced (valid for chunk separation of the original claim chunk).
        return player.getName() + "_" + chunkPos.getX() + "_" + chunkPos.getZ() + "_" + System.currentTimeMillis();
    }

    public static void inheritanceSettings(@NotNull Claim claim, @NotNull Claim from) {
        //claim.setDisplayName(from.getDisplayName());
        //claim.setDescription(from.getDescription());
        claim.setIcon(from.getIcon());
        claim.setPriority(from.getPriority());
        claim.getFlags().putAll(from.getFlags());
        claim.getMemberMap().putAll(from.getMemberMap());
    }

    @Nullable
    public static Location getSafeLocation(@NotNull Location origin) {
        Location location = origin.clone();

        while (!isSafeLocation(location) && location.getBlockY() > 0) {
            location = location.add(0, -1, 0);
        }

        return location.getBlockY() == 0 ? null : location;
    }

    public static boolean isSafeLocation(@NotNull Location origin) {
        Block block = origin.getBlock();
        Block under = block.getRelative(BlockFace.DOWN);

        return !isDangerousBlock(block) && isSafeBlock(block.getRelative(BlockFace.DOWN));
    }

    public static boolean isSafeBlock(@NotNull Block block) {
        Material material = block.getType();
        return material.isSolid() && !isDangerousBlock(block);
    }

    public static boolean isDangerousBlock(@NotNull Block block) {
        Material material = block.getType();
        return material == Material.LAVA || material == Material.MAGMA_BLOCK;
    }

    @NotNull
    public static String getDefaultName(@NotNull String id, @NotNull Player player, @NotNull ClaimType type) {
        return switch (type) {
            case CHUNK -> Config.LAND_DEFAULT_NAME.get().replace(Placeholders.PLAYER_NAME, player.getName());
            case REGION -> StringUtil.capitalizeUnderscored(id);
        };
    }

    @NotNull
    public static ItemStack getDefaultIcon(@NotNull ClaimType type) {
        String skin = type == ClaimType.REGION ? "67f0dcba5d10bd5efeb849a7d6e106fa6f40300e26cf88cdfbc4d465beab89b2" : "89f7a04ac334fcaf618da9e841f03c00d749002dc592f8540ef9534442cecf42";
        return ItemUtil.getSkinHead(skin);
    }

    @NotNull
    public static ItemStack getDefaultSelectionItem() {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
        ItemUtil.editMeta(itemStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Region Wand")));
            meta.setLore(Lists.newList(
                DARK_GRAY.enclose("(Drop to exit selection mode)"),
                "",
                LIGHT_YELLOW.enclose("[▶] ") + LIGHT_GRAY.enclose("Left-Click to " + LIGHT_YELLOW.enclose("set 1st") + " point."),
                LIGHT_YELLOW.enclose("[▶] ") + LIGHT_GRAY.enclose("Right-Click to " + LIGHT_YELLOW.enclose("set 2nd") + " point.")
            ));
        });
        return itemStack;
    }

    @NotNull
    public static ItemStack getDefaultMergeItem() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.editMeta(itemStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Merge Tool")));
            meta.setLore(Lists.newList(
                DARK_GRAY.enclose("(Drop to exit merge mode)"),
                "",
                LIGHT_YELLOW.enclose("[▶] ") + LIGHT_GRAY.enclose("Click to " + LIGHT_YELLOW.enclose("select chunk") + " .")
            ));
        });
        return itemStack;
    }

    @NotNull
    public static ItemStack getDefaultSeparateItem() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.editMeta(itemStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Separation Tool")));
            meta.setLore(Lists.newList(
                DARK_GRAY.enclose("(Drop to exit separation mode)"),
                "",
                LIGHT_YELLOW.enclose("[▶] ") + LIGHT_GRAY.enclose("Click to " + LIGHT_YELLOW.enclose("select chunk") + " .")
            ));
        });
        return itemStack;
    }
}
