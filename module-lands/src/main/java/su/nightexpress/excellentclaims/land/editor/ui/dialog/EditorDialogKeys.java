package su.nightexpress.excellentclaims.land.editor.ui.dialog;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public final class EditorDialogKeys {

    public static final DialogKey<LandClaim> NAME        = new DialogKey<>("lands.editor.name");
    public static final DialogKey<LandClaim> DESCRIPTION = new DialogKey<>("lands.editor.description");
    public static final DialogKey<LandClaim> PRIORITY    = new DialogKey<>("lands.editor.priority");

    private EditorDialogKeys() {
    }
}
