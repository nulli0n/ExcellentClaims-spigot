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

    boolean canChange(Player player, Block block);

    boolean canDamage(Player player, Entity entity, DamageSource source);

    boolean canDestroy(Player player, Block block);

    boolean canDestroy(Player player, Entity entity);

    boolean canInteract(Player player, Block block, Action action);

    boolean canInteract(Player player, Entity entity);

    boolean canInteract(Player player, ItemStack itemStack);

    boolean canDropItem(Player player, ItemStack itemStack);

    boolean canPickupItem(Player player, ItemStack itemStack);

    boolean canPlace(Player player, Block block, Material placedType);

    boolean canPlace(Player player, Block block, Entity entity);

    boolean canShoot(Player player, Projectile projectile);

    boolean canTeleport(Player player, Location from, Location to, TeleportCause cause);

    boolean canUseCommand(Player player, Command command);

}
