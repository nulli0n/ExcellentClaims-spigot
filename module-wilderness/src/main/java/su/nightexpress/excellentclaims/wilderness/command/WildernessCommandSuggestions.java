package su.nightexpress.excellentclaims.wilderness.command;

import java.util.ArrayList;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.nightcore.commands.SuggestionsProvider;

@NullMarked
public class WildernessCommandSuggestions {

    private final WildernessRepository repository;

    public WildernessCommandSuggestions(WildernessRepository repository) {
        this.repository = repository;
    }

    /**
     * Suggests all regions in the repository.
     */
    public SuggestionsProvider all() {
        return (reader, context) -> new ArrayList<>(this.repository.idValues());
    }
}