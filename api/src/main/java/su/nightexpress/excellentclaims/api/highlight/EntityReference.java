package su.nightexpress.excellentclaims.api.highlight;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public record EntityReference(int id, UUID uuid, BlockPos pos) {

}
