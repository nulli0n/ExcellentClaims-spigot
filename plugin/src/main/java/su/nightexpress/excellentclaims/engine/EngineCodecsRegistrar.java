package su.nightexpress.excellentclaims.engine;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimDefinition;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMembers;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimRules;
import su.nightexpress.excellentclaims.core.claim.codec.ClaimDefinitionCodec;
import su.nightexpress.excellentclaims.core.claim.codec.ClaimMemberCodec;
import su.nightexpress.excellentclaims.core.claim.codec.ClaimMembersCodec;
import su.nightexpress.excellentclaims.core.claim.codec.ClaimRulesCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public final class EngineCodecsRegistrar {

    private EngineCodecsRegistrar() {
    }

    public static void register() {
        ConfigCodecs.register(DefaultClaimDefinition.class, new ClaimDefinitionCodec());
        ConfigCodecs.register(DefaultClaimRules.class, ClaimRulesCodec.INSTANCE);
        ConfigCodecs.register(DefaultClaimMember.class, new ClaimMemberCodec());
        ConfigCodecs.register(DefaultClaimMembers.class, new ClaimMembersCodec());
    }
}
