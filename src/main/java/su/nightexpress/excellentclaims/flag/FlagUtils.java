package su.nightexpress.excellentclaims.flag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.excellentclaims.flag.registry.EntityFlags;
import su.nightexpress.excellentclaims.flag.registry.PlayerFlags;
import su.nightexpress.excellentclaims.util.BlockTypes;
import su.nightexpress.nightcore.util.BlockUtil;
import su.nightexpress.nightcore.util.Version;

import java.util.function.Predicate;

public class FlagUtils {

    // TODO Config setting for claims to follow wilderness's flag value as default value if flag is unset?

    public static boolean hasSpecificBlockFlag(@NotNull Block block) {
        return hasSpecificBlockFlag(block.getType());
    }

    public static boolean hasSpecificBlockFlag(@NotNull Material blockType) {
        return getSpecificBlockFlag(blockType) != null;
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificBlockFlag(@NotNull Material blockType) {
        BlockData blockData = blockType.createBlockData();
        BlockState state = blockData.createBlockState();
        if (state instanceof Container) {
            return state instanceof Chest ? PlayerFlags.CHEST_ACCESS : PlayerFlags.CONTAINER_ACCESS;
        }

        if (BlockTypes.isPressurePlate(blockType)) return PlayerFlags.USE_PLATES;
        if (BlockTypes.isButtonLever(blockType)) return PlayerFlags.USE_BUTTONS;
        if (BlockTypes.isDoor(blockType)) return PlayerFlags.USE_DOORS;
        if (BlockTypes.isTramplable(blockType)) return PlayerFlags.BLOCK_TRAMPLING;
        if (BlockTypes.isTripwire(blockType)) return PlayerFlags.USE_TRIPWIRES;
        if (BlockTypes.isSign(blockType)) return PlayerFlags.USE_SIGNS;
        if (BlockTypes.isCake(blockType)) return PlayerFlags.EAT_CAKES;
        if (BlockTypes.isCauldron(blockType)) return PlayerFlags.USE_CAULDRONS;
        if (BlockTypes.isAnvil(blockType)) return PlayerFlags.USE_ANVILS;
        if (BlockTypes.isBed(blockType)) return PlayerFlags.USE_BEDS;

        return null;
    }

    public static boolean hasSpecificItemFlag(@NotNull Material itemType) {
        return getSpecificItemFlag(itemType) != null;
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificItemFlag(@NotNull Material itemType) {
        return getSpecificItemFlag(new ItemStack(itemType));
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificItemFlag(@NotNull ItemStack itemStack) {
        Material itemType = itemStack.getType();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof SpawnEggMeta) return PlayerFlags.USE_SPAWN_EGGS;
        if (itemType == Material.ENDER_PEARL) return PlayerFlags.USE_ENDER_PEARLS;
        if (itemType == Material.CHORUS_FRUIT) return PlayerFlags.USE_CHORUS_FRUIT;
        if (itemType == Material.FLINT_AND_STEEL) return PlayerFlags.USE_LIGHTER;
        if (itemType == Material.FIREWORK_ROCKET) return PlayerFlags.PLACE_FIREWORKS;
        if (itemType == Material.GOAT_HORN) return PlayerFlags.USE_HORNS;

        return null;
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificProjectileFlag(@NotNull EntityType type) {
        if (type == EntityType.ARROW) return PlayerFlags.SHOOT_BOWS;
        if (type == EntityType.EGG) return PlayerFlags.THROW_EGGS;
        if (type == EntityType.TRIDENT) return PlayerFlags.THROW_TRIDENTS;
        if (type == EntityType.EYE_OF_ENDER) return PlayerFlags.THROW_ENDER_EYES;
        if (type == EntityType.WIND_CHARGE) return PlayerFlags.THROW_WIND_CHARGES;

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            if (type == EntityType.SPLASH_POTION || type == EntityType.LINGERING_POTION) return PlayerFlags.THROW_POTIONS;
        }
        else {
            if (type.name().equalsIgnoreCase("POTION")) return PlayerFlags.THROW_POTIONS;
        }

        return null;
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificEntitySpawnFlag(@NotNull Entity entity) {
        if (entity instanceof Animals) return EntityFlags.ANIMAL_SPAWN;
        if (entity instanceof Monster) return EntityFlags.MONSTER_SPAWN;

        return null;
    }

    public static boolean hasSpecificEntityInteractFlag(@NotNull EntityType type) {
        Class<? extends Entity> clazz = type.getEntityClass();
        if (clazz == null) return false;

        World world = Bukkit.getWorlds().getFirst();
        Entity entity = world.createEntity(world.getSpawnLocation(), clazz);

        return getSpecificEntityInteractFlag(entity) != null;
    }

    @Nullable
    public static ClaimFlag<Boolean> getSpecificEntityInteractFlag(@NotNull Entity entity) {
        if (entity instanceof AbstractVillager) return PlayerFlags.USE_VILLAGERS;
        if (entity instanceof ArmorStand) return PlayerFlags.USE_ARMOR_STAND;

//        if (entity instanceof ItemFrame) {
//            flag = PlayerFlags.BLOCK_BREAK; // TODO Explicit flag + more flags for other entities
//        }

        if (entity instanceof Vehicle && !(entity instanceof LivingEntity)) {
            if (entity instanceof InventoryHolder) {
                if (entity instanceof StorageMinecart || entity instanceof ChestBoat) {
                    return PlayerFlags.CHEST_ACCESS;
                }
                return PlayerFlags.CONTAINER_ACCESS;
            }
            return PlayerFlags.USE_VEHICLES;
        }

        return null;
    }





    @NotNull
    public static Predicate<Claim> getBuildingPredicate(@NotNull Player player) {
        return new FlagPredicate(PlayerFlags.BLOCK_PLACE, ClaimPermission.BUILDING, player);
    }

    @NotNull
    public static Predicate<Claim> getBreakingPredicate(@NotNull Player player) {
        return new FlagPredicate(PlayerFlags.BLOCK_BREAK, ClaimPermission.BUILDING, player);
    }

    @NotNull
    public static Predicate<Claim> getHarvestingPredicate(@NotNull Player player) {
        return new FlagPredicate(PlayerFlags.BLOCK_HARVEST, ClaimPermission.BUILDING, player);
    }

    @Nullable
    public static Predicate<Claim> getBlockInteractionPredicate(@NotNull Entity entity, @NotNull Block block, @Nullable Action action) {
        Material blockType = block.getType();

        Player user;
        if (entity instanceof Player player) {
            user = player;
        }
        else if (entity instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            user = player;
        }
        else user = null;

        ClaimFlag<Boolean> flag = getSpecificBlockFlag(blockType);
        ClaimPermission permission = ClaimPermission.BLOCK_INTERACT;

        // There is no specialized flag related to that block.
        if (flag == null) {
            if (!BlockUtil.isFunctional(blockType)) return null; // The block is not even functional/utility, skip.

            flag = PlayerFlags.BLOCK_INTERACT; // Use generic block interact flag for all other functional blocks.
        }
        else {
            boolean isPhysicalAction = (action == Action.PHYSICAL || action == null);
            if (BlockTypes.isPhysicalInteractionBlock(blockType) && !isPhysicalAction) {
                flag = PlayerFlags.BLOCK_INTERACT; // Use generic flag for plates, turtle eggs and tripwires if used by left/clicking instead of walking through.
            }
        }

        FlagPredicate predicate = new FlagPredicate(flag, permission, user);

        if (flag == PlayerFlags.BLOCK_INTERACT) {
            predicate = predicate.withExtra(claim -> claim.canUseBlock(block.getType()));
        }

        return predicate;
    }

    @Nullable
    public static FlagPredicate getItemUsagePredicate(@NotNull Player player, @NotNull ItemStack itemStack) {
        ClaimFlag<Boolean> flag = getSpecificItemFlag(itemStack);
        if (flag == null) return null;

        return new FlagPredicate(flag, null, player);
    }

    @Nullable
    public static FlagPredicate getProjectileThrowPredicate(@NotNull Player player, @NotNull Projectile projectile) {
        ClaimFlag<Boolean> flag = getSpecificProjectileFlag(projectile.getType());
        if (flag == null) return null;

        return new FlagPredicate(flag, null, player);
    }

    @NotNull
    public static Predicate<Claim> getEntityInteractPredicate(@NotNull Player player, @NotNull Entity entity) {
        ClaimFlag<Boolean> flag = getSpecificEntityInteractFlag(entity);
        ClaimPermission permission = ClaimPermission.ENTITY_INTERACT;

        if (flag == null) {
            return new FlagPredicate(PlayerFlags.ENTITY_INTERACT, permission, player).withExtra(claim -> claim.canUseMob(entity.getType()));
        }
        else {
            return new FlagPredicate(flag, permission, player);
        }
    }

    @NotNull
    public static Predicate<Claim> getMobSpawnPredicate(@NotNull Entity entity) {
        ClaimFlag<Boolean> flag = getSpecificEntitySpawnFlag(entity);
        if (flag != null) return new FlagPredicate(flag, null, null);

        return new FlagPredicate(EntityFlags.ENTITY_SPAWN, null, null).withExtra(claim -> claim.canMobSpawn(entity.getType()));
    }

    @Nullable
    public static Predicate<Claim> getEntityDamagePredicate(@NotNull Entity victim, @Nullable Entity damager, @NotNull DamageType damageType) {
        if (damager instanceof Player playerDamager) {
            if (victim instanceof Player) {
                return new FlagPredicate(PlayerFlags.PLAYER_DAMAGE_PLAYERS, null, null); // null to prevent pvp bypassing by owners/members.
            }

            ClaimFlag<Boolean> flag = switch (victim) {
                case Animals ignored -> PlayerFlags.PLAYER_DAMAGE_ANIMALS;
                case AbstractVillager ignored -> PlayerFlags.PLAYER_DAMAGE_VILLAGERS;
                default -> PlayerFlags.PLAYER_DAMAGE_ENTITIES;
            };

            return new FlagPredicate(flag, null, playerDamager);
        }

        if (victim instanceof Animals) {
            return new FlagPredicate(EntityFlags.ANIMAL_DAMAGE, null, null).withExtra(claim -> claim.isAnimalDamageAllowed(damageType));
        }
        else if (victim instanceof Player) {
            return new FlagPredicate(PlayerFlags.PLAYER_DAMAGE, null, null).withExtra(claim -> claim.isPlayerDamageAllowed(damageType));
        }
        else return null;
    }

    @NotNull
    public static Predicate<Claim> getCommandUsagePredicate(@NotNull Player player, @NotNull Command command) {
        return new FlagPredicate(PlayerFlags.USE_COMMANDS, null, player).withExtra(claim -> claim.isCommandAllowed(command));
    }
}
