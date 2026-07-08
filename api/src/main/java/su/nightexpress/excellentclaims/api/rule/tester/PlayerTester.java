package su.nightexpress.excellentclaims.api.rule.tester;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PlayerTester {

    /**
     * Evaluates whether the player is permitted to alter the state of the specified block.
     * 
     * @param player
     * @param block
     * @return
     */
    boolean canChange(Player player, Block block);

    /**
     * Evaluates whether the player can inflict damage upon the specified entity using the provided damage source.
     * 
     * @param player
     * @param entity
     * @param source
     * @return
     */
    boolean canDamage(Player player, Entity entity, DamageSource source);

    /**
     * Evaluates whether the player is permitted to mine or break the specified block.
     * 
     * @param player
     * @param block
     * @return
     */
    boolean canDestroy(Player player, Block block);

    /**
     * Evaluates whether the player is permitted to break or remove the specified entity (e.g., armor stands,
     * minecarts).
     * 
     * @param player
     * @param entity
     * @return
     */
    boolean canDestroy(Player player, Entity entity);

    /**
     * Evaluates whether the player can perform a specific physical interaction (e.g., right-click, pressure plate step)
     * with a block.
     * 
     * @param player
     * @param block
     * @param action
     * @return
     */
    boolean canInteract(Player player, Block block, Action action);

    /**
     * Evaluates whether the player can interact with a specific entity (e.g., trading, riding).
     * 
     * @param player
     * @param entity
     * @return
     */
    boolean canInteract(Player player, Entity entity);

    /**
     * Evaluates whether the player is allowed to use a specific item in their hand.
     * 
     * @param player
     * @param itemStack
     * @return
     */
    boolean canInteract(Player player, ItemStack itemStack);

    /**
     * Evaluates whether the player is permitted to drop the specified item on the ground.
     * 
     * @param player
     * @param itemStack
     * @return
     */
    boolean canDropItem(Player player, ItemStack itemStack);

    /**
     * Evaluates whether the player is permitted to collect the specified dropped item.
     * 
     * @param player
     * @param itemStack
     * @return
     */
    boolean canPickupItem(Player player, ItemStack itemStack);

    /**
     * Evaluates whether the player is permitted to place a block of the specified material at the target location.
     * 
     * @param player
     * @param block
     * @param placedType
     * @return
     */
    boolean canPlace(Player player, Block block, Material placedType);

    /**
     * Evaluates whether the player is permitted to place or deploy the specified entity at the target location.
     * 
     * @param player
     * @param block
     * @param entity
     * @return
     */
    boolean canPlace(Player player, Block block, Entity entity);

    /**
     * Evaluates whether the player is permitted to fire the specified projectile.
     * 
     * @param player
     * @param projectile
     * @return
     */
    boolean canShoot(Player player, Projectile projectile);

    /**
     * Evaluates whether the player is permitted to teleport between the specified locations given the teleport cause.
     * 
     * @param player
     * @param from
     * @param to
     * @param cause
     * @return
     */
    boolean canTeleport(Player player, Location from, Location to, TeleportCause cause);

    /**
     * Evaluates whether the player is permitted to execute the specified server command at the target location.
     * 
     * @param player
     * @param command
     * @return
     */
    boolean canUseCommand(Player player, Location location, Command command);

}
