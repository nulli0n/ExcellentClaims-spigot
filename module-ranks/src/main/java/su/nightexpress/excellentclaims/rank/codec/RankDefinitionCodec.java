package su.nightexpress.excellentclaims.rank.codec;

import java.util.EnumSet;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.rank.model.DefaultRankDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RankDefinitionCodec implements ConfigCodec<DefaultRankDefinition> {

    private static final ConfigCodec<EnumSet<ClaimPermission>> PERMS_CODEC = ConfigCodecs.forEnumSet(
        ClaimPermission.class);

    @Override
    public DefaultRankDefinition read(FileConfig config, String path) throws CodecReadException {
        String displayName = config.getOrSet(path + ".DisplayName", ConfigCodecs.STRING, "Rank");
        int priority = config.getOrSet(path + ".Priority", ConfigCodecs.INT, 0);

        Set<ClaimPermission> permissions = config.getOrSet(path + ".Permissions", PERMS_CODEC, EnumSet.noneOf(
            ClaimPermission.class));

        return new DefaultRankDefinition(displayName, priority, permissions);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRankDefinition value) {
        config.set(path + ".DisplayName", value.getDisplayName());
        config.set(path + ".Priority", value.getPriority());
        config.set(path + ".Permissions", PERMS_CODEC, EnumSet.copyOf(value.getPermissions()));
    }
}
