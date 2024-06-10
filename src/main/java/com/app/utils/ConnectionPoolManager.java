package com.app.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.app.utils.PropertiesUtil.getProperty;

public final class ConnectionPoolManager {

    private static final String PROPS_DRIVER = "db.driver";
    private static final String PROPS_URL = "db.url";
    private static final HikariConfig HIKARI_CONFIG;
    private static final HikariDataSource DATA_SOURCE;
    private static final String HIKARI_PROPERTY_URL = "url";

    private ConnectionPoolManager() {

    }

    static {
        HIKARI_CONFIG = new HikariConfig();
        HIKARI_CONFIG.setDataSourceClassName(getProperty(PROPS_DRIVER));
        HIKARI_CONFIG.addDataSourceProperty(HIKARI_PROPERTY_URL, getProperty(PROPS_URL));
        DATA_SOURCE = new HikariDataSource(HIKARI_CONFIG);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
