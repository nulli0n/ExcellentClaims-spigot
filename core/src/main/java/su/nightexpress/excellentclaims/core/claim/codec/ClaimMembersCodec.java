package su.nightexpress.excellentclaims.core.claim.codec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMembers;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ClaimMembersCodec implements ConfigCodec<DefaultClaimMembers> {

    @Override
    public DefaultClaimMembers read(FileConfig config, String path) throws CodecReadException {
        Map<UUID, ClaimMember> members = new HashMap<>();

        Set<UUID> owners = new HashSet<>();
        config.getStringList(path + ".Owners").forEach(id -> {
            try {
                UUID uuid = UUID.fromString(id);
                owners.add(uuid);
            }
            catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                // TODO Log
            }
        });

        String memberPath = path + ".Childrens";
        config.getSection(memberPath).forEach(id -> {
            try {
                ClaimMember member = config.get(memberPath + "." + id, DefaultClaimMember.class);
                if (member == null) {
                    // TODO Logger
                    return;
                }

                members.put(member.getPlayerId(), member);
            }
            catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                // TODO Logger
            }
        });

        return new DefaultClaimMembers(owners, members);
    }

    @Override
    public void write(FileConfig config, String path, DefaultClaimMembers value) {
        config.set(path + ".Owners", value.getOwners().stream().map(UUID::toString).toList());

        String memberPath = path + ".Childrens";

        config.remove(memberPath);
        int index = 0;
        for (ClaimMember member : value.getMembers()) {
            config.set(memberPath + "." + index, member);
            index++;
        }
    }
}
