package su.nightexpress.excellentclaims.land.claim.validation;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.settings.LandSettings;

public class LandQuotaValidator {

    private final LandSettings    settings;
    private final LandsRepository repository;

    public LandQuotaValidator(LandSettings settings, LandsRepository repository) {
        this.settings = settings;
        this.repository = repository;
    }

    public boolean canClaimMore(Player player) {
        return this.getAvailableClaims(player) != 0;
    }

    public int getMaxClaims(Player player) {
        return this.settings.getMaxClaims(player);
    }

    public int getAvailableClaims(Player player) {
        int max = this.getMaxClaims(player);
        if (max < 0) return -1;

        int owns = this.repository.countClaims(player);
        return Math.max(0, max - owns);
    }

    public boolean canExpandClaim(Player player, LandClaim claim) {
        return this.getAvailableChunks(player, claim) != 0;
    }

    public int getMaxClaimSize(Player player) {
        return this.settings.getMaxClaimSize(player);
    }

    public int getAvailableChunks(Player player, LandClaim claim) {
        int maxChunksPerLand = this.getMaxClaimSize(player);
        if (maxChunksPerLand < 0) return -1;

        int current = claim.getChunksAmount();
        return Math.max(0, maxChunksPerLand - current);
    }
}
