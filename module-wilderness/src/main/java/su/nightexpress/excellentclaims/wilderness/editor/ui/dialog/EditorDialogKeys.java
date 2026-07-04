package su.nightexpress.excellentclaims.wilderness.editor.ui.dialog;

import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public final class EditorDialogKeys {

    public static final DialogKey<WildernessRegion> NAME        = new DialogKey<>("wilderness.editor.name");
    public static final DialogKey<WildernessRegion> DESCRIPTION = new DialogKey<>("wilderness.editor.description");
    public static final DialogKey<WildernessRegion> PRIORITY    = new DialogKey<>("wilderness.editor.priority");

    private EditorDialogKeys() {
    }
}
