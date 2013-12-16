package me.Miny.Paypassage.update;

import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import me.Miny.Paypassage.update.Updater.UpdateResult;
import me.Miny.Paypassage.update.Updater.UpdateType;

public class Update {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    transient int i; // transient: not stored
    private Paypassage plugin;
    
    public Update(Paypassage up) {
        plugin = up;
    }
    
    public void startUpdateTimer(){
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    if (plugin.getConfigHandler().getConfig().getBoolean("updateCheck")) {
                        try {
                            plugin.getLoggerUtility().log("Searching update for Paypassage!", LoggerUtility.Level.DEBUG);
                            Updater updater = new Updater(plugin, 48021, plugin.getPluginFile(), UpdateType.NO_DOWNLOAD, true);
        		    if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
                                plugin.getLoggerUtility().log("New version: " + updater.getLatestName() + " found!", LoggerUtility.Level.WARNING);
                                plugin.getLoggerUtility().log("******************************************", LoggerUtility.Level.WARNING);
                                plugin.getLoggerUtility().log("*********** Please update!!!! ************", LoggerUtility.Level.WARNING);
                                plugin.getLoggerUtility().log("* http://dev.bukkit.org/Paypassage *******", LoggerUtility.Level.WARNING);
                                plugin.getLoggerUtility().log("******************************************", LoggerUtility.Level.WARNING);
                            } else {
                                plugin.getLoggerUtility().log("No update found!", LoggerUtility.Level.DEBUG);
                            }
                        } catch (Exception e) {
                            plugin.getLoggerUtility().log("Error on doing update check! Message: " + e.getMessage(), LoggerUtility.Level.ERROR);
                            plugin.getLoggerUtility().log("may the mainserver is down!", LoggerUtility.Level.ERROR);
                            plugin.getReportHandler().report(335, "Checking for update failed", e.getMessage(), "Paypassage", e);
                        }
                    }
                }
            }, 400L, 50000L);
    }

}
