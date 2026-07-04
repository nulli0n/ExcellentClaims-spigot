package su.nightexpress.excellentclaims.region.editor.ui.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.excellentclaims.region.editor.ui.RegionEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class EditorPriorityButton implements DefaultButtonExtension {

    private final RegionEditorService      editor;
    private final RegionEditorUIController controller;

    public EditorPriorityButton(RegionEditorService editor, RegionEditorUIController controller) {
        this.editor = editor;
        this.controller = controller;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("region_priority", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.REPEATER)
                    .setDisplayName(TagWrappers.GOLD.wrap(TagWrappers.BOLD.wrap("Priority")))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Current: " + TagWrappers.GOLD.wrap(RegionsPlaceholders.REGION_PRIORITY)),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to change"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> {
                    item.replace(ctx -> ctx.with(context.getObject(RegionClaim.class).placeholders()));
                })
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(32)
            .build()
        );
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        RegionClaim claim = context.getObject(RegionClaim.class);

        this.controller.onSetPriorityClick(player, claim, () -> context.getViewer().refresh());
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        RegionClaim claim = context.getObject(RegionClaim.class);

        return this.editor.canChangePriority(player, claim).success();
    }
}
