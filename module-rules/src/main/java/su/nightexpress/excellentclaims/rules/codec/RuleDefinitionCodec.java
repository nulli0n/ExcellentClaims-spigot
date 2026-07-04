package su.nightexpress.excellentclaims.rules.codec;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RuleDefinitionCodec implements ConfigCodec<RuleDefinition> {

    public static final RuleDefinitionCodec INSTANCE = new RuleDefinitionCodec();

    @Override
    public RuleDefinition read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".Name", ConfigCodecs.STRING, "null");
        List<String> description = config.getOrSet(path + ".Description", ConfigCodecs.STRING_LIST, List.of());
        NightItem icon = config.getOrSet(path + ".Icon", ConfigCodecs.NIGHT_ITEM, NightItem.fromType(Material.STONE));

        return new RuleDefinition(name, description, icon);
    }

    @Override
    public void write(FileConfig config, String path, RuleDefinition value) {
        config.set(path + ".Name", value.name());
        config.set(path + ".Description", value.description());
        config.set(path + ".Icon", value.icon());
    }
}
