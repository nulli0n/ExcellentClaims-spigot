package su.nightexpress.excellentclaims.land.editor.ui.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.editor.LandEditorService;
import su.nightexpress.excellentclaims.land.editor.ui.LandEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class EditorHomeButton implements DefaultButtonExtension {

    private final LandEditorService      editor;
    private final LandEditorUIController controller;

    public EditorHomeButton(LandEditorService editor, LandEditorUIController controller) {
        this.editor = editor;
        this.controller = controller;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("land_home", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_PEARL)
                    .setDisplayName(TagWrappers.LIGHT_PURPLE.wrap(TagWrappers.BOLD.wrap("Teleport")))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Teleport to claim's spawn point."),
                        "",
                        TagWrappers.LIGHT_PURPLE.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to teleport"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> {
                    item.replace(ctx -> ctx.with(context.getObject(LandClaim.class).placeholders()));
                })
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(31)
            .build()
        );
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        this.controller.onHomeClick(player, claim, false);
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        return this.editor.canUseHomeTeleport(player, claim).success();
    }
}
