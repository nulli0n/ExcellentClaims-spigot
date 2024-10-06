package su.nightexpress.excellentclaims.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.nightcore.database.AbstractUserDataHandler;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.util.Lists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<ClaimPlugin, ClaimUser> {

    private final Function<ResultSet, ClaimUser> userFunction;

    public DataHandler(@NotNull ClaimPlugin plugin) {
        super(plugin);
        this.userFunction = resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                return new ClaimUser(plugin, uuid, name, dateCreated, lastOnline);
            }
            catch (IllegalArgumentException | SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };
    }

    @Override
    public void onSynchronize() {

    }

    @Override
    @NotNull
    protected List<SQLColumn> getExtraColumns() {
        return Lists.newList();
    }

    @Override
    @NotNull
    protected List<SQLValue> getSaveColumns(@NotNull ClaimUser user) {
        return Lists.newList();
    }

    @Override
    @NotNull
    protected Function<ResultSet, ClaimUser> getUserFunction() {
        return this.userFunction;
    }
}
