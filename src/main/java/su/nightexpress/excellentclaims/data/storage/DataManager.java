package su.nightexpress.excellentclaims.data.storage;

import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.data.user.ClaimUser;
import su.nightexpress.nightcore.db.AbstractUserDataManager;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.impl.SelectQuery;
import su.nightexpress.nightcore.db.sql.query.type.ValuedQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class DataManager extends AbstractUserDataManager<ClaimPlugin, ClaimUser> {

    public DataManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return builder;
    }

    @Override
    @NotNull
    protected Function<ResultSet, ClaimUser> createUserFunction() {
        return resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                return new ClaimUser(uuid, name, dateCreated, lastOnline);
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
    protected void addUpsertQueryData(@NotNull ValuedQuery<?, ClaimUser> query) {

    }

    @Override
    protected void addSelectQueryData(@NotNull SelectQuery<ClaimUser> query) {

    }

    @Override
    protected void addTableColumns(@NotNull List<Column> columns) {

    }
}
