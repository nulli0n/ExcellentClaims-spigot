package su.nightexpress.excellentclaims.core.claim.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class ClaimDefinitionCodec implements ConfigCodec<DefaultClaimDefinition> {

    @Override
    public DefaultClaimDefinition read(FileConfig config, String path) throws CodecReadException {
        DefaultClaimDefinition definition = new DefaultClaimDefinition();

        definition.setSpawnLocation(ExactPos.read(config, path + ".SpawnPos"));
        definition.setName(config.getOrSet(path + ".DisplayName", ConfigCodecs.STRING, "Claim"));
        definition.setDescription(config.getStringList(path + ".Description"));
        definition.setPriority(config.getInt(path + ".Priority", 0));
        definition.setIcon(config.getCosmeticItem(path + ".Icon"));

        return definition;
    }

    @Override
    public void write(FileConfig config, String path, DefaultClaimDefinition value) {
        config.set(path + ".SpawnPos", value.getSpawnLocation());
        config.set(path + ".DisplayName", value.getName());
        config.set(path + ".Description", value.getDescription());
        config.set(path + ".Priority", value.getPriority());
        config.set(path + ".Icon", value.getIcon());
    }
}
