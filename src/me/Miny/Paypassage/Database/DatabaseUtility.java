package me.Miny.Paypassage.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.Location;

/**
 *
 * @author Simon
 */
public class DatabaseUtility {

    private Paypassage plugin;
    private DatabaseConnector connector;

    public DatabaseUtility(Paypassage plugin) {
        this.plugin = plugin;
        connector = new DatabaseConnector(plugin, plugin.getConfig().getBoolean("use_MySQL"));
        connector.createConnection();
    }

    public void closeConnection() {
        connector.CloseCon();
    }

    public void insertNewSign(final Location sign, final Location dest, final String name) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                long time = 0;
                plugin.getLoggerUtility().log("Insert sign into table!", LoggerUtility.Level.DEBUG);
                time = System.nanoTime();
                try {
                    PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO PaypassageSigns ("
                            + "Name,"
                            + "Sign_X,"
                            + "Sign_Y,"
                            + "Sign_Z,"
                            + "Destination_X,"
                            + "Destination_Y,"
                            + "Destination_Z) VALUES (?,?,?,?,?,?,?)");
                    ps.setString(1, name);
                    ps.setInt(2, sign.getBlockX());
                    ps.setInt(3, sign.getBlockY());
                    ps.setInt(4, sign.getBlockZ());
                    ps.setInt(5, dest.getBlockX());
                    ps.setInt(6, dest.getBlockY());
                    ps.setInt(7, dest.getBlockZ());                    
                    ps.execute();
                    connector.getConnection().commit();
                    ps.close();
                } catch (SQLException e) {
                    plugin.getLoggerUtility().log("Error while inserting XP into DB! - " + e.getMessage(), LoggerUtility.Level.DEBUG);
                }
                time = (System.nanoTime() - time) / 1000000;
                plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
            }
        });

    }

    public void PrepareDB() {
        Statement st = null;
        long time = 0;
        plugin.getLoggerUtility().log("Creaeting table!", LoggerUtility.Level.DEBUG);
        time = System.nanoTime();
        try {
            st = connector.getConnection().createStatement();
            if (plugin.getConfig().getBoolean("use_MySQL")) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageSigns ("
                        + "Name VARCHAR(100) NOT NULL, "
                        + "PRIMARY KEY(Name), "
                        + "Sign_X int, "
                        + "Sign_Y int, "
                        + "Sign_Z int, "
                        + " int, "
                        + "Destination_Y int, "
                        + "Destination_Z int);");
                plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
            } else {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageSigns ("
                        + "Name VARCHAR PRIMARY KEY NOT NULL, "
                        + "Sign_X INTEGER, "
                        + "Sign_Y INTEGER, "
                        + "Sign_Z INTEGER, "
                        + "Destination_X INTEGER, "
                        + "Destination_Y INTEGER, "
                        + "Destination_Z INTEGER);");
                plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
            }
            connector.getConnection().commit();
            st.close();
        } catch (SQLException e) {
            plugin.getLoggerUtility().log("Error while creating tables! - " + e.getMessage(), LoggerUtility.Level.ERROR);
            plugin.onDisable();
        }
        time = (System.nanoTime() - time) / 1000000;
        plugin.getLoggerUtility().log("Created in " + time + " ms!", LoggerUtility.Level.DEBUG);
        //	    UpdateDB();
    }
}
