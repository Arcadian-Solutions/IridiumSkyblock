package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class which handles the database connection and acts as a DAO.
 */
public class DatabaseManager {

    private final SQL sqlConfig;
    private final ConnectionSource connectionSource;

    private final Dao<User, UUID> userDao;
    private final Dao<Island, Integer> islandDao;
    private final Dao<SchematicData, String> schematicDao;
    private final Dao<IslandInvite, Integer> islandInviteDao;
    private final Dao<IslandPermission, Integer> islandPermissionDao;
    private final Dao<IslandBlocks, Integer> islandblocksDao;
    private final Dao<IslandBank, Integer> islandBankDao;
    private final Dao<IslandMission, Integer> islandMissionDao;

    @Getter
    private final List<User> userList;
    @Getter
    private final List<Island> islandList;
    @Getter
    private final List<IslandInvite> islandInviteList;
    @Getter
    private final List<SchematicData> schematicDataList;
    @Getter
    private final List<IslandPermission> islandPermissionList;
    @Getter
    private final List<IslandBlocks> islandBlocksList;
    @Getter
    private final List<IslandBank> islandBankList;
    @Getter
    private final List<IslandMission> islandMissionList;

    /**
     * The default constructor.
     *
     * @throws SQLException If the connection or any operations failed
     */
    public DatabaseManager() throws SQLException {
        this.sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL();

        connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Island.class);
        TableUtils.createTableIfNotExists(connectionSource, SchematicData.class);
        TableUtils.createTableIfNotExists(connectionSource, IslandInvite.class);
        TableUtils.createTableIfNotExists(connectionSource, IslandPermission.class);
        TableUtils.createTableIfNotExists(connectionSource, IslandBlocks.class);
        TableUtils.createTableIfNotExists(connectionSource, IslandBank.class);
        TableUtils.createTableIfNotExists(connectionSource, IslandMission.class);

        this.userDao = DaoManager.createDao(connectionSource, User.class);
        this.islandDao = DaoManager.createDao(connectionSource, Island.class);
        this.schematicDao = DaoManager.createDao(connectionSource, SchematicData.class);
        this.islandInviteDao = DaoManager.createDao(connectionSource, IslandInvite.class);
        this.islandPermissionDao = DaoManager.createDao(connectionSource, IslandPermission.class);
        this.islandblocksDao = DaoManager.createDao(connectionSource, IslandBlocks.class);
        this.islandBankDao = DaoManager.createDao(connectionSource, IslandBank.class);
        this.islandMissionDao = DaoManager.createDao(connectionSource, IslandMission.class);

        userDao.setAutoCommit(getDatabaseConnection(), false);
        islandDao.setAutoCommit(getDatabaseConnection(), false);
        islandInviteDao.setAutoCommit(getDatabaseConnection(), false);
        islandPermissionDao.setAutoCommit(getDatabaseConnection(), false);
        islandblocksDao.setAutoCommit(getDatabaseConnection(), false);
        islandBankDao.setAutoCommit(getDatabaseConnection(), false);
        islandMissionDao.setAutoCommit(getDatabaseConnection(), false);

        this.userList = getUsers();
        this.islandList = getIslands();
        this.islandInviteList = getIslandInvites();
        this.schematicDataList = getSchematics();
        this.islandPermissionList = getIslandPermissions();
        this.islandBlocksList = getIslandBlocks();
        this.islandBankList = getIslandBank();
        this.islandMissionList = getIslandMissions();
    }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private @NotNull String getDatabaseURL() {
        switch (sqlConfig.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlConfig.driver + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL;
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlConfig.host + ":" + sqlConfig.port + ";databaseName=" + sqlConfig.database;
            case H2:
                return "jdbc:h2:file:" + sqlConfig.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    /**
     * Returns a connection from the connection source.
     *
     * @return The connection to the database for operations
     * @throws SQLException If there is an error with the exception
     */
    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    /**
     * Returns a list of all users in the database.
     * Empty if an error occurs.
     *
     * @return a List of all users
     */
    private @NotNull List<User> getUsers() {
        try {
            return userDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all islands in the database.
     * Empty if an error occurs.
     *
     * @return a List of all islands
     */
    private @NotNull List<Island> getIslands() {
        try {
            return islandDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all islandInvites in the database.
     * Empty if an error occurs.
     *
     * @return a List of all islandInvites
     */
    private @NotNull List<IslandInvite> getIslandInvites() {
        try {
            return islandInviteDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all islandPermissions in the database.
     * Empty if an error occurs.
     *
     * @return a List of all islandPermissions
     */
    private @NotNull List<IslandPermission> getIslandPermissions() {
        try {
            return islandPermissionDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all island blocks in the database.
     * Empty if an error occurs.
     *
     * @return a List of all island blocks
     */
    private @NotNull List<IslandBlocks> getIslandBlocks() {
        try {
            return islandblocksDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all schematics in the database.
     * Empty if an error occurs.
     *
     * @return a list of all schematics in the database
     */
    private @NotNull List<SchematicData> getSchematics() {
        try {
            return schematicDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns a list of all island banks's in the database.
     * Empty if an error occurs.
     *
     * @return a List of all island blocks
     */
    private @NotNull List<IslandBank> getIslandBank() {
        try {
            return islandBankDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all island missions in the database.
     * Empty if an error occurs.
     *
     * @return a List of all island blocks
     */
    private @NotNull List<IslandMission> getIslandMissions() {
        try {
            return islandMissionDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Saves an island to the database and initializes variables like ID
     *
     * @param island The island we are saving
     * @return The island with variables like id added
     */
    public Island registerIsland(Island island) {
        try {
            islandDao.createOrUpdate(island);
            islandDao.commit(getDatabaseConnection());
            Island is = islandDao.queryBuilder().where().eq("name", island.getName()).queryForFirst();
            islandList.add(is);
            return is;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return island;
    }

    /**
     * Saves all users to the database.
     * Creates them if they don't exist
     */
    public void saveUsers() {
        try {
            for (User user : userList) {
                userDao.createOrUpdate(user);
            }
            userDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all Islands to the database.
     * Creates them if they don't exist.
     */
    public void saveIslands() {
        try {
            for (Island island : islandList) {
                islandDao.createOrUpdate(island);
            }
            islandDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all Island invites to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandInvites() {
        try {
            for (IslandInvite islandInvite : islandInviteList) {
                islandInviteDao.createOrUpdate(islandInvite);
            }
            islandInviteDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all Island permissions to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandPermissions() {
        try {
            for (IslandPermission islandPermission : islandPermissionList) {
                islandPermissionDao.createOrUpdate(islandPermission);
            }
            islandPermissionDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all schematics to the database.
     * Creates them if they don't exist.
     */
    public void saveSchematics() {
        try {
            for (SchematicData schematic : schematicDataList) {
                schematicDao.createOrUpdate(schematic);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all island blocks to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandBlocks() {
        try {
            for (IslandBlocks islandBlocks : islandBlocksList) {
                islandblocksDao.createOrUpdate(islandBlocks);
            }
            islandblocksDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all Island bank items to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandBank() {
        try {
            for (IslandBank islandBank : islandBankList) {
                islandBankDao.createOrUpdate(islandBank);
            }
            islandBankDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all Island missions to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandMissions() {
        try {
            for (IslandMission islandMission : islandMissionList) {
                islandMissionDao.createOrUpdate(islandMission);
            }
            islandMissionDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Deletes all daily missions saved in the database
     */
    public void deleteDailyMissions() {
        try {
            List<IslandMission> islandMissions = getIslandMissionList().stream().filter(islandMission -> islandMission.getType() == Mission.MissionType.DAILY).collect(Collectors.toList());
            islandMissionDao.delete(islandMissions);
            islandMissionDao.commit(getDatabaseConnection());
            islandMissions.forEach(islandMissionList::remove);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Removes an island and all references to the island from the database.
     *
     * @param island The island being deleted.
     */
    public void deleteIsland(@NotNull Island island) {
        try {
            island.getMembers().forEach(user -> user.setIsland(null));
            islandDao.delete(island);
            islandList.remove(island);
            islandDao.commit(getDatabaseConnection());
            saveIslands();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Removes an IslandInvite from the database.
     *
     * @param islandInvite The island Invite being deleted
     */
    public void deleteInvite(@NotNull IslandInvite islandInvite) {
        try {
            islandInviteDao.delete(islandInvite);
            islandInviteList.remove(islandInvite);
            islandInviteDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
