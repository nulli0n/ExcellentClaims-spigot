package su.nightexpress.excellentclaims.integration.protocollib;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import su.nightexpress.excellentclaims.api.highlight.BlockContext;
import su.nightexpress.excellentclaims.api.highlight.BlockHighlighter;
import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class BlockProtocolHighlighter implements BlockHighlighter {

    private final ProtocolManager manager;

    public BlockProtocolHighlighter() {
        this.manager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void clearHighlight(Player player, Collection<Integer> entityIds) {
        PacketContainer destroyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntLists().write(0, new ArrayList<>(entityIds));
        this.manager.sendServerPacket(player, destroyPacket);
    }

    @Override
    public void highlightBlock(Player player, EntityReference reference, BlockContext context, Color color,
                               float size) {
        EntityType type = EntityType.BLOCK_DISPLAY;

        ExactPos location = context.position();
        BlockData blockData = context.data();

        PacketContainer spawnPacket = this.createSpawnPacket(type, location, reference.id(), reference.uuid());

        PacketContainer dataPacket = this.createMetadataPacket(reference.id(), metadata -> {
            // Glow
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                0, WrappedDataWatcher.Registry.get((Type) Byte.class)), (byte) (0x20 | 0x40)
            );

            // Scale
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                12, WrappedDataWatcher.Registry.get((Type) Vector3f.class)), new Vector3f(size, size, size)
            );

            // Glow Color Override
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                22, WrappedDataWatcher.Registry.get((Type) Integer.class)), color.asARGB()
            );

            // Block Data
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                23, WrappedDataWatcher.Registry.getBlockDataSerializer(false)),
                WrappedBlockData.createData(blockData)
            );
        });

        this.manager.sendServerPacket(player, spawnPacket);
        this.manager.sendServerPacket(player, dataPacket);
    }


    @Override
    public void updateHighlight(Player player, EntityReference reference, ExactPos pos, Color color, float size) {
        PacketContainer dataPacket = this.createMetadataPacket(reference.id(), metadata -> {
            // Glow
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                0, WrappedDataWatcher.Registry.get((Type) Byte.class)), (byte) (0x20 | 0x40)
            );

            // Scale
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                12, WrappedDataWatcher.Registry.get((Type) Vector3f.class)), new Vector3f(size, size, size)
            );

            // Glow Color Override
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(
                22, WrappedDataWatcher.Registry.get((Type) Integer.class)), color.asARGB()
            );
        });

        PacketContainer syncPacket = new PacketContainer(PacketType.Play.Server.ENTITY_POSITION_SYNC);
        syncPacket.getIntegers().write(0, reference.id());
        syncPacket.getDoubles().write(0, pos.getX());
        syncPacket.getDoubles().write(1, pos.getY());
        syncPacket.getDoubles().write(2, pos.getZ());
        syncPacket.getFloat().write(0, 0F);
        syncPacket.getFloat().write(1, 0F);
        syncPacket.getBooleans().write(0, false);

        this.manager.sendServerPacket(player, dataPacket);
        this.manager.sendServerPacket(player, syncPacket);
    }

    private PacketContainer createSpawnPacket(EntityType entityType, ExactPos location, int entityID,
                                              UUID uuid) {
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, entityID);
        spawnPacket.getUUIDs().write(0, uuid);
        spawnPacket.getEntityTypeModifier().write(0, entityType);
        spawnPacket.getDoubles().write(0, location.getX());
        spawnPacket.getDoubles().write(1, location.getY());
        spawnPacket.getDoubles().write(2, location.getZ());
        return spawnPacket;
    }


    private PacketContainer createMetadataPacket(int entityID, Consumer<WrappedDataWatcher> consumer) {
        PacketContainer dataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher metadata = new WrappedDataWatcher();

        consumer.accept(metadata);

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        metadata.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject
                .getSerializer(), entry.getRawValue()));
        });

        dataPacket.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        dataPacket.getIntegers().write(0, entityID);

        return dataPacket;
    }
}
