package me.lxc.artxeapi.data;

import me.lxc.artxeapi.utils.ArtxeDebug;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.text.MessageFormat;
import java.util.logging.Level;

public class ArtxeSQL {
    private JavaPlugin plugin;
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private Connection connection = null;
    private Statement statement = null;
    private boolean connected = false;
    private ArtxeDebug debug;

    public ArtxeSQL(JavaPlugin plugin, String host, int port, String username, String password, String database) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        debug = new ArtxeDebug(plugin, false);
    }

    public ArtxeSQL(JavaPlugin plugin, String host, int port, String username, String password, String database, boolean debugEnable) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        debug = new ArtxeDebug(plugin, debugEnable);
    }

    public boolean connect() {
        if (!isConnected()) {
            plugin.getLogger().log(Level.INFO, "Connecting to SQL");
            debug.debug(MessageFormat.format("Host: {0}:{1}", this.host, this.port));
            debug.debug(MessageFormat.format("Database: ''{0}''", this.database));
            debug.debug(MessageFormat.format("Username: ''{0}''", this.username));
            debug.debug(MessageFormat.format("Using password: {0}", this.password.isEmpty() ? "NO" : "YES"));
            try {
                this.connection = DriverManager.getConnection(MessageFormat.format("jdbc:mysql://{0}:{1}?autoConnect=true", this.host, this.port), this.username, this.password);

                statement = getConnection().createStatement();
                statement.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s", this.database));

                plugin.getLogger().log(Level.INFO, "Connected to SQL");

                this.connected = true;
                return true;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while connect to SQL: ", e);
                this.connected = false;
                return false;
            }
        }
        return false;
    }

    public boolean disconnect() {
        if (isConnected()) {
            plugin.getLogger().log(Level.INFO, "Disconnecting SQL");
            try {
                getConnection().close();
                statement.close();
                this.connection = null;
                plugin.getLogger().log(Level.INFO, "Disconnected SQL");
                this.connected = false;
                return true;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while disconnect SQL: ", e);
                return false;
            }
        }
        return false;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void update(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while update database: ", e);
        }
    }

    public ResultSet query(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while query database: ", e);
            return null;
        }
    }
}
