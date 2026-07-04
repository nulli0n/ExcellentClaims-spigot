package su.nightexpress.excellentclaims.rules.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.EditorContext;
import su.nightexpress.excellentclaims.api.rule.RuleUIEditor;
import su.nightexpress.excellentclaims.rules.editor.EditorRegistry;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleListContext;

public class RuleUIController {

    private final Logger         logger;
    private final EditorRegistry editors;
    private final RuleUIService  uiService;

    public RuleUIController(Logger logger, EditorRegistry editors, RuleUIService uiService) {
        this.logger = logger;
        this.editors = editors;
        this.uiService = uiService;
    }

    public <T> void onRuleClick(Player player,
                                ClaimRule<T> rule,
                                InventoryClickEvent event,
                                RuleListContext context) {
        RuleUIEditor<T> editor = this.editors.getEditor(rule.getType());
        if (editor == null) {
            this.logger.log(Level.WARNING, "No editor found for the '%s' rule".formatted(rule.key().asString()));
            return;
        }

        Runnable onBack = () -> this.uiService.openRules(player, context);
        Claim claim = context.claim();
        EditorContext editorContext = new EditorContext(event, context::markDirty, onBack);

        editor.onClick(player, claim, rule, editorContext);
    }
}
