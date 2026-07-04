package su.nightexpress.excellentclaims.region.selection.ui.highlight;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.api.highlight.HighlightSession;
import su.nightexpress.excellentclaims.api.highlight.WireframeCalculator;
import su.nightexpress.excellentclaims.region.selection.ui.UIComponentExtension;

@NullMarked
public class HighlightExtension implements UIComponentExtension {

    private final HighlightAPI      highlightAPI;
    private final HighlightSettings settings;

    public HighlightExtension(HighlightAPI highlightAPI, HighlightSettings settings) {
        this.highlightAPI = highlightAPI;
        this.settings = settings;
    }

    @Override
    public HighlightComponent createComponent() {
        WireframeCalculator calculator = new WireframeCalculator();
        HighlightSession highlightSession = new HighlightSession(calculator);

        return new HighlightComponent(this.settings, this.highlightAPI, highlightSession);
    }
}
