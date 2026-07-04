package su.nightexpress.excellentclaims.region.selection.ui.bossbar;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.selection.session.SelectionSession;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIComponent;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.BossBarUtils;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.DimensionType;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class BossBarComponent implements SelectionUIComponent {

    public static final Identifier ID = Identifier.of("boss_bar");

    private final RegionQuotaValidator quotaValidator;
    private final BossBarSettings      settings;

    private final NightBossBar bossBar;


    public BossBarComponent(RegionQuotaValidator quotaValidator, BossBarSettings settings) {
        this.quotaValidator = quotaValidator;
        this.settings = settings;
        this.bossBar = BossBarUtils.createBossBar("", settings.getColor(), settings.getOverlay());
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void onAttach(Player player, SelectionSession session) {
        this.bossBar.addViewer(player);
        this.onUpdate(player, session, null);
    }

    @Override
    public void onDetach(Player player, SelectionSession session) {
        this.bossBar.removeViewers();
    }

    @Override
    public void onUpdate(Player player, SelectionSession session, @Nullable Cuboid cuboid) {
        DimensionType type = this.quotaValidator.getSizeValidationType();
        int selectedBlocks = cuboid == null ? 0 : cuboid.getVolume(type);
        int claimBlocks = this.quotaValidator.getAvailableClaimBlocks(player);

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(ClaimsPlaceholders.GENERIC_CURRENT, () -> NumberUtil.format(selectedBlocks))
            .with(ClaimsPlaceholders.GENERIC_MAX, () -> {
                return claimBlocks < 0 ? CoreLang.OTHER_INFINITY.text() : NumberUtil.format(claimBlocks);
            })
            .with(ClaimsPlaceholders.GENERIC_STATE, () -> CoreLang.STATE_ON_OFF.get(!session.isPaused()))
            .build();

        String formattedTitle = placeholders.apply(this.settings.getTitle());

        float progress = 1f;
        if (claimBlocks == 0) {
            progress = 0f;
        }
        else if (claimBlocks > 0) {
            progress = selectedBlocks / (float) claimBlocks;
        }

        this.bossBar.addViewer(player);
        this.bossBar.setProgress(progress);
        this.bossBar.setName(NightMessage.parse(formattedTitle));
    }
}
