package dev.nh3.lifesteal.database;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.database.entities.PlayerEntity;

import java.sql.SQLException;

public class Database {
    private static ConnectionSource connectionSource;
    private Dao<PlayerEntity, String> playerDao;
    public Database (LifeSteal instance, String url) throws SQLException {
        connectionSource = new JdbcConnectionSource(url);
        TableUtils.createTableIfNotExists(connectionSource, PlayerEntity.class);
        playerDao = DaoManager.createDao(connectionSource, PlayerEntity.class);
    }

    public Dao<PlayerEntity, String> getPlayerDao() {
        return playerDao;
    }
}
