
package me.Miny.Paypassage.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import sun.nio.cs.ext.GB18030;

/**
 *
 * @author ibhh
 */
public class DatabaseConnector {
    private Connection cn;
    private Paypassage plugin;
    private boolean use_MySQL;

    public DatabaseConnector(Paypassage plugin, boolean use_MySQL) {
        this.plugin = plugin;
        this.use_MySQL = use_MySQL;
    }
    
    public Connection createConnection() {
        if (use_MySQL) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://" + 
                        plugin.getConfig().getString("dbPath"),
                        plugin.getConfig().getString("dbUser"),
                        plugin.getConfig().getString("dbPassword"));
                cn.setAutoCommit(false);
                return cn;
            } catch (SQLException e) {
                plugin.getLoggerUtility().log("could not be enabled: Exception occured while trying to connect to DB", LoggerUtility.Level.ERROR);
                SQLErrorHandler(e);
                if (cn != null) {
                    System.out.println("Old Connection still activated");
                    try {
                        cn.close();
                        plugin.getLoggerUtility().log("Old connection that was still activated has been successfully closed", LoggerUtility.Level.ERROR);
                    } catch (SQLException e2) {
                        plugin.getLoggerUtility().log("Failed to close old connection that was still activated", LoggerUtility.Level.ERROR);
                        SQLErrorHandler(e2);
                    }
                }
                return null;
            } catch (ClassNotFoundException e) {
                plugin.getLoggerUtility().log(e.getMessage(), LoggerUtility.Level.ERROR);
                return null;
            }
        } else {
            try {
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException cs) {
                    plugin.getLoggerUtility().log(cs.getMessage(), LoggerUtility.Level.ERROR);
                }
                File root = new File(plugin.getDataFolder() + File.separator + "Databases");
                if(!root.exists()){
                    root.mkdir();
                }
                cn = DriverManager.getConnection("jdbc:sqlite:"
                        + plugin.getDataFolder()
                        + File.separator + "Databases" + File.separator
                        + "Signs.sqlite");
                cn.setAutoCommit(false);
                return cn;
            } catch (SQLException e) {
                SQLErrorHandler(e);
            }
        }
        return null;
    }

    private void SQLErrorHandler(SQLException ex) {
        do {
            try {
                plugin.getLoggerUtility().log("Exception Message: " + ex.getMessage(), LoggerUtility.Level.ERROR);
                plugin.getLoggerUtility().log("DBMS Code: " + ex.getErrorCode(), LoggerUtility.Level.ERROR);
                ex.printStackTrace();
            } catch (Exception ne) {
                plugin.getLoggerUtility().log(ne.getMessage(), LoggerUtility.Level.ERROR);
            }
        } while ((ex = ex.getNextException()) != null);
    }

    public boolean CloseCon() {
        try {
            cn.close();
            return true;
        } catch (SQLException e) {
            plugin.getLoggerUtility().log("Failed to close connection to DB!", LoggerUtility.Level.ERROR);
            SQLErrorHandler(e);
            return false;
        }
    }
}
