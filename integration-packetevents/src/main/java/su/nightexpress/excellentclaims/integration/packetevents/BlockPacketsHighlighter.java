package su.nightexpress.excellentclaims.integration.packetevents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityPositionSync;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import su.nightexpress.excellentclaims.api.highlight.BlockContext;
import su.nightexpress.excellentclaims.api.highlight.BlockHighlighter;
import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class BlockPacketsHighlighter implements BlockHighlighter {

    private final PlayerManager manager;

    public BlockPacketsHighlighter() {
        this.manager = PacketEvents.getAPI().getPlayerManager();
    }

    @Override
    public void clearHighlight(Player player, Collection<Integer> entityIds) {
        int[] arr = entityIds.stream().mapToInt(i -> i).toArray();

        WrapperPlayServerDestroyEntities destroyPacket = new WrapperPlayServerDestroyEntities(arr);
        this.manager.sendPacket(player, destroyPacket);
    }

    @Override
    public void highlightBlock(Player player, EntityReference reference, BlockContext context, Color color,
                               float size) {
        EntityType type = EntityType.BLOCK_DISPLAY;

        ExactPos position = context.position();
        BlockData blockData = context.data();

        WrappedBlockState state = WrappedBlockState.getByString(blockData.getAsString());

        var spawnPacket = this.createSpawnPacket(type, position, reference.id(), reference.uuid());

        var dataPacket = this.createMetadataPacket(reference.id(), dataList -> {
            // Glow
            dataList.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) (0x20 | 0x40)));
            // Scale
            dataList.add(new EntityData<>(12, EntityDataTypes.VECTOR3F, new Vector3f(size, size, size)));
            // Glow color override
            dataList.add(new EntityData<>(22, EntityDataTypes.INT, color.asARGB()));
            // Block State ID
            dataList.add(new EntityData<>(23, EntityDataTypes.BLOCK_STATE, state.getGlobalId()));
        });

        this.manager.sendPacket(player, spawnPacket);
        this.manager.sendPacket(player, dataPacket);
    }


    @Override
    public void updateHighlight(Player player, EntityReference reference, ExactPos pos, Color color, float size) {
        var dataPacket = this.createMetadataPacket(reference.id(), dataList -> {
            // Glow
            dataList.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) (0x20 | 0x40)));
            // Scale
            dataList.add(new EntityData<>(12, EntityDataTypes.VECTOR3F, new Vector3f(size, size, size)));
            // Glow color override
            dataList.add(new EntityData<>(22, EntityDataTypes.INT, color.asARGB()));
        });

        Vector3d position = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
        EntityPositionData positionData = new EntityPositionData(position, new Vector3d(), 0, 0);
        WrapperPlayServerEntityPositionSync positionSync = new WrapperPlayServerEntityPositionSync(
            reference.id(), positionData, false);

        this.manager.sendPacket(player, dataPacket);
        this.manager.sendPacket(player, positionSync);
    }

    private WrapperPlayServerSpawnEntity createSpawnPacket(EntityType type, ExactPos location, int entityID,
                                                           UUID uuid) {

        com.github.retrooper.packetevents.protocol.entity.type.EntityType wrappedType = SpigotConversionUtil
            .fromBukkitEntityType(type);

        Location wrappedLocation = new Location(
            location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        return new WrapperPlayServerSpawnEntity(entityID, uuid, wrappedType, wrappedLocation, 0f, 0, new Vector3d());
    }

    private PacketWrapper<?> createMetadataPacket(int entityID, Consumer<List<EntityData<?>>> consumer) {
        List<EntityData<?>> dataList = new ArrayList<>();

        consumer.accept(dataList);

        return new WrapperPlayServerEntityMetadata(entityID, dataList);
    }
}
