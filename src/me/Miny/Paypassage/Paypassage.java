package me.Miny.Paypassage;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.Miny.Paypassage.Database.DatabaseTools;
import me.Miny.Paypassage.Database.DatabaseUtility;
import me.Miny.Paypassage.PPListeners.PPListener;
import me.Miny.Paypassage.Permissions.PermissionsUtility;
import me.Miny.Paypassage.Report.ReportToHost;
import me.Miny.Paypassage.Sign.InvalidSignCreation;
import me.Miny.Paypassage.Sign.ListofCreations;
import me.Miny.Paypassage.Sign.SignCreate;
import me.Miny.Paypassage.config.ConfigurationHandler;
import me.Miny.Paypassage.logger.LoggerUtility;
import me.Miny.Paypassage.update.Update;
import me.Miny.Paypassage.update.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Miny
 */
public class Paypassage extends JavaPlugin {

    //Modules
    private LoggerUtility logger;
    private ConfigurationHandler config;
    private ReportToHost report;
    private PermissionsUtility permissions;
    private Privacy privacy;
    private Update update;
    private Utilities pluginmanager;
    private PPListener listener;
    private DatabaseUtility databaseUtility;
    //Commands array
    private String[] commands = {
        "help",
        "debugfile",
        "internet",
        "version",
        "update",
        "reload",
        "statuschange",
        "language",
        "report",
        "denytracking",
        "allowtracking",
        "create"
    };

    public String[] getCommands() {
        return commands;
    }

    /**
     * returns an Error-Reporting-API
     *
     * @return ReportToHost
     */
    public ReportToHost getReportHandler() {
        if (report == null) {
            report = new ReportToHost(this);
        }
        return report;
    }

    public DatabaseUtility getDatabaseUtility() {
        if (databaseUtility == null) {
            databaseUtility = new DatabaseUtility(this);
        }
        return databaseUtility;
    }

    public PPListener getListener() {
        if (listener == null) {
            listener = new PPListener(this);
        }
        return listener;
    }

    public Utilities getPluginManager() {
        if (pluginmanager == null) {
            pluginmanager = new Utilities(this);
        }
        return pluginmanager;
    }

    public Privacy getPrivacy() {
        if (privacy == null) {
            privacy = new Privacy(this);
        }
        return privacy;
    }

    public Update getUpdate() {
        if (update == null) {
            update = new Update(this);
        }
        return update;
    }

    /**
     * Returns a permissions API (GroupManager, PermissionsEx, bPermissions,
     * BukkitPermissions)
     *
     * @return PermissionsUtility
     */
    public PermissionsUtility getPermissions() {
        if (permissions == null) {
            permissions = new PermissionsUtility(this);
        }
        return permissions;
    }

    /**
     * Returns a handler that manages all config files
     *
     * @return ConfigurationHandler
     */
    public ConfigurationHandler getConfigHandler() {
        if (config == null) {
            config = new ConfigurationHandler(this);
        }
        return config;
    }

    /**
     * returns a custom loggertool
     *
     * @return LoggerUtility
     */
    public LoggerUtility getLoggerUtility() {
        if (logger == null) {
            logger = new LoggerUtility(this);
        }
        return logger;
    }

    /**
     * returns the same as isEnabled()
     *
     * @return boolean isEnabled
     */
    public boolean isStarted() {
        return this.isEnabled();
    }

    /**
     * Called by ConfigurationHandler if loading of files failed Disables the
     * plugin
     */
    @Override
    public void onDisable() {
        setEnabled(false);
        getPrivacy().savePrivacyFiles();
        long time = System.nanoTime();
        System.out.println("Paypassage disabled in " + ((System.nanoTime() - time) / 1000000) + " ms");
    }

    /**
     * Enables the plugin
     */
    @Override
    public void onEnable() {
        long time = System.nanoTime();
        getConfigHandler().onStart();
        getLoggerUtility();
        getLoggerUtility().log("creating config!", LoggerUtility.Level.DEBUG);
        getLoggerUtility().log("init logger!", LoggerUtility.Level.DEBUG);
        getReportHandler();
        getLoggerUtility().log("init report!", LoggerUtility.Level.DEBUG);
        getPermissions();
        getLoggerUtility().log("init permissions!", LoggerUtility.Level.DEBUG);
        getLoggerUtility().log("init database!", LoggerUtility.Level.DEBUG);
        try {
            getDatabaseUtility().PrepareDB();
        } catch (SQLException ex) {
            DatabaseTools.SQLErrorHandler(this, ex);
            setEnabled(false);
            return;
        }
        getPrivacy().loadData();
        getPrivacy().autoSave();
        getLoggerUtility().log("init privacy control!", LoggerUtility.Level.DEBUG);
        getUpdate().startUpdateTimer();
        getLoggerUtility().log("init update control!", LoggerUtility.Level.DEBUG);
        getListener();
        getLoggerUtility().log("init listeners!", LoggerUtility.Level.DEBUG);
        getLoggerUtility().log("Paypassage enabled in " + ((System.nanoTime() - time) / 1000000) + " ms", LoggerUtility.Level.INFO);
        setEnabled(true);
    }

    public float getVersion() {
        try {
            return Float.parseFloat(getDescription().getVersion());
        } catch (Exception e) {
            getLoggerUtility().log("Could not parse version in float", LoggerUtility.Level.INFO);
            getLoggerUtility().log("Error getting version of " + this.getName() + "! Message: " + e.getMessage(), LoggerUtility.Level.ERROR);
            this.report.report(3310, "Error getting version of " + this.getName() + "!", e.getMessage(), "Paypassage", e);
            getLoggerUtility().log("Uncatched Exeption!", LoggerUtility.Level.ERROR);
        }
        return 0;
    }

    /**
     * Handles commands
     *
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!isEnabled()) {
            getLoggerUtility().log("Paypassage plugin is NOT enabled!", LoggerUtility.Level.ERROR);
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            /**
             * /pp create command
             */
            if (command.getName().equalsIgnoreCase("pp")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase(getConfigHandler().getLanguage_config().getString("commands.create.name"))) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.create.permission"))) {
                            ListofCreations.getList().put(player.getName(), new SignCreate());
                            getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("creation.sign.notification1"), LoggerUtility.Level.INFO);
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase(getConfigHandler().getLanguage_config().getString("commands.cancel.name"))) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.cancel.permission"))) {
                        	ListofCreations.getList().remove(player.getName());
                            getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("creation.sign.notification.cancel"), LoggerUtility.Level.INFO);
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase(getConfigHandler().getLanguage_config().getString("commands.setdestination.name"))) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.setdestination.permission"))) {
                            ListofCreations.getList().get(player.getName()).setDestination(player.getLocation());
                            try {
								ListofCreations.getList().get(player.getName()).save(this);
								getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("creation.sign.notification.success"), LoggerUtility.Level.INFO);
							} catch (InvalidSignCreation e) {
								getLoggerUtility().log(player, e.getMessage(), LoggerUtility.Level.ERROR);
							}
                            getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("creation.sign.notification5"), LoggerUtility.Level.INFO);
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("info")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage Status:" + ChatColor.GREEN + "Working!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.reload.permission"))) {
                            try {
                                getLoggerUtility().log(player, "Please wait: Reloading this plugin!", LoggerUtility.Level.WARNING);
                                getPluginManager().unloadPlugin("BookShop");
                                getPluginManager().loadPlugin("BookShop");
                                getLoggerUtility().log(player, "Reloaded!", LoggerUtility.Level.INFO);
                            } catch (InvalidPluginException | InvalidDescriptionException | NoSuchFieldException | IllegalAccessException ex) {
                                Logger.getLogger(Paypassage.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase(getConfigHandler().getLanguage_config().getString("commands.denytracking.name"))) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.denytracking.permission"))) {
                            if (getPrivacy().getConfig().containsKey(player.getName())) {
                                getPrivacy().getConfig().remove(player.getName());
                            }
                            getPrivacy().getConfig().put(player.getName(), false);
                            getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("privacy.notification.denied"), LoggerUtility.Level.INFO);
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase(getConfigHandler().getLanguage_config().getString("commands.allowtracking.name"))) {
                        if (getPermissions().checkpermissions(player, getConfigHandler().getLanguage_config().getString("commands.allowtracking.permission"))) {
                            if (getPrivacy().getConfig().containsKey(player.getName())) {
                                getPrivacy().getConfig().remove(player.getName());
                            }
                            getPrivacy().getConfig().put(player.getName(), Boolean.TRUE);
                            getLoggerUtility().log(player, getConfigHandler().getLanguage_config().getString("privacy.notification.allowed"), LoggerUtility.Level.INFO);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
