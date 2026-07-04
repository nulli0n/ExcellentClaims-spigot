package su.nightexpress.excellentclaims.api.rank;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RankResolver {

    Rank resolveOrLowest(String name);
}
