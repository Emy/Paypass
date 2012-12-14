package me.Miny.Paypassage.Database;

import me.Miny.Paypassage.Paypassage;

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
    
    public void closeConnection(){
        connector.CloseCon();
    }
    
}
