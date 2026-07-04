package su.nightexpress.excellentclaims.api.rule;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public record RuleDefinition(String name, List<String> description, NightItem icon) {

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {

        private String       name;
        private List<String> description = List.of();
        private NightItem    icon        = NightItem.fromType(Material.STONE);

        public Builder(String name) {
            this.name = name;
        }

        public RuleDefinition build() {
            return new RuleDefinition(name, description, icon);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String... description) {
            this.description = Lists.newList(description).stream().map(TagWrappers.GRAY::wrap).toList();
            return this;
        }

        public Builder icon(NightItem icon) {
            this.icon = icon.copy();
            return this;
        }

        public Builder icon(Material material) {
            return this.icon(NightItem.fromType(material));
        }
    }
}