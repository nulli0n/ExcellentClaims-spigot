package su.nightexpress.excellentclaims.region.editor.ui.dialog;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public final class EditorDialogKeys {

    public static final DialogKey<RegionClaim> NAME        = new DialogKey<>("regions.editor.name");
    public static final DialogKey<RegionClaim> DESCRIPTION = new DialogKey<>("regions.editor.description");
    public static final DialogKey<RegionClaim> PRIORITY    = new DialogKey<>("regions.editor.priority");

    private EditorDialogKeys() {
    }
}
