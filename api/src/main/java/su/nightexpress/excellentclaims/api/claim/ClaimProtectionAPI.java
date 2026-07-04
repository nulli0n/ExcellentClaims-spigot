package su.nightexpress.excellentclaims.api.claim;

public interface ClaimProtectionAPI {

    // TODO API Impl + use Event Lookup from RuleModule
    /* public boolean testClaim(Location location, Supplier<Predicate<Claim>> predicateSupplier) {
        Claim claim = this.claimRegistry.getPrioritizedClaim(location);
        if (claim == null) return true;
    
        Predicate<Claim> predicate = predicateSupplier.get();
        return predicate == null || predicate.test(claim);
    }
    
    public boolean canMobSpawn(Entity entity, Location location) {
        if (entity instanceof Player) return true;
    
        return this.testClaim(entity.getLocation(), () -> FlagUtils.getMobSpawnPredicate(entity));
    }
    
    public boolean canUseBlock(Entity entity, Block block, @Nullable Action action) {
        return this.testClaim(block.getLocation(), () -> FlagUtils.getBlockInteractionPredicate(entity, block, action));
    }
    
    public boolean canUseItem(Player player, Location location, ItemStack itemStack) {
        return this.testClaim(location, () -> FlagUtils.getItemUsagePredicate(player, itemStack));
    }
    
    public boolean canUseEntity(Player player, Location location, Entity entity) {
        return this.testClaim(location, () -> FlagUtils.getEntityInteractPredicate(player, entity));
    }
    
    public boolean canDamage(@Nullable Entity damager, Entity target, DamageType damageType) {
        return this.testClaim(target.getLocation(), () -> FlagUtils.getEntityDamagePredicate(target, damager,
            damageType));
    }
    
    public boolean canThrowProjectile(Player player, Projectile projectile) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getProjectileThrowPredicate(player, projectile));
    }
    
    public boolean canUseCommand(Player player, Command command) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getCommandUsagePredicate(player, command));
    }
    
    public boolean canBreak(Player player, Block block) {
        return this.canBreak(player, block.getLocation());
    }
    
    public boolean canBreak(Player player, Location location) {
        return this.testClaim(location, () -> FlagUtils.getBreakingPredicate(player));
    }
    
    public boolean canBuild(Player player, Block block) {
        return this.canBuild(player, block.getLocation());
    }
    
    public boolean canBuild(Player player, Location location) {
        return this.testClaim(location, () -> FlagUtils.getBuildingPredicate(player));
    }
    
    public boolean canHarvest(Player player, Block block) {
        return this.canHarvest(player, block.getLocation());
    }
    
    public boolean canHarvest(Player player, Location location) {
        return this.testClaim(location, () -> FlagUtils.getHarvestingPredicate(player));
    } 
        
        public boolean testClaim(@NonNull Location location, @NonNull Supplier<Predicate<Claim>> predicateSupplier) {
        Claim claim = this.repository.getPrioritizedClaim(location);
        if (claim == null) return true;
    
        Predicate<Claim> predicate = predicateSupplier.get();
        return predicate == null || predicate.test(claim);
    }
    
    public boolean canMobSpawn(@NonNull Entity entity, @NonNull Location location) {
        if (entity instanceof Player) return true;
    
        return this.testClaim(entity.getLocation(), () -> FlagUtils.getMobSpawnPredicate(entity));
    }
    
    public boolean canUseBlock(@NonNull Entity entity, @NonNull Block block, @Nullable Action action) {
        return this.testClaim(block.getLocation(), () -> FlagUtils.getBlockInteractionPredicate(entity, block, action));
    }
    
    public boolean canUseItem(@NonNull Player player, @NonNull Location location, @NonNull ItemStack itemStack) {
        return this.testClaim(location, () -> FlagUtils.getItemUsagePredicate(player, itemStack));
    }
    
    public boolean canUseEntity(@NonNull Player player, @NonNull Location location, @NonNull Entity entity) {
        return this.testClaim(location, () -> FlagUtils.getEntityInteractPredicate(player, entity));
    }
    
    public boolean canDamage(@Nullable Entity damager, @NonNull Entity target, @NonNull DamageType damageType) {
        return this.testClaim(target.getLocation(), () -> FlagUtils.getEntityDamagePredicate(target, damager,
            damageType));
    }
    
    public boolean canThrowProjectile(@NonNull Player player, @NonNull Projectile projectile) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getProjectileThrowPredicate(player, projectile));
    }
    
    public boolean canUseCommand(@NonNull Player player, @NonNull Command command) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getCommandUsagePredicate(player, command));
    }
    
    public boolean canBreak(@NonNull Player player, @NonNull Block block) {
        return this.canBreak(player, block.getLocation());
    }
    
    public boolean canBreak(@NonNull Player player, @NonNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getBreakingPredicate(player));
    }
    
    public boolean canBuild(@NonNull Player player, @NonNull Block block) {
        return this.canBuild(player, block.getLocation());
    }
    
    public boolean canBuild(@NonNull Player player, @NonNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getBuildingPredicate(player));
    }
    
    public boolean canHarvest(@NonNull Player player, @NonNull Block block) {
        return this.canHarvest(player, block.getLocation());
    }
    
    public boolean canHarvest(@NonNull Player player, @NonNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getHarvestingPredicate(player));
    }
    */
}
