package me.Miny.Paypassage;
 
import Permissions.PermissionsUtility;
import me.Miny.Paypassage.Report.ReportToHost;
import me.Miny.Paypassage.config.ConfigurationHandler;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
 
/**
 *
 * @author Miny
 */
public class Paypass extends JavaPlugin {
    
    //Modules
    private LoggerUtility logger;
    private ConfigurationHandler config;
    private ReportToHost report;
    private PermissionsUtility permissions;

    
    /**
     * returns an Error-Reporting-API
     * @return ReportToHost
     */
    public ReportToHost getReportHandler() {
        if(report == null){
            report = new ReportToHost(this);
        }
        return report;
    }

    /**
     * Returns a permissions API (GroupManager, PermissionsEx, bPermissions, BukkitPermissions)
     * @return PermissionsUtility
     */
    public PermissionsUtility getPermissions() {
        if(permissions == null){
            permissions = new PermissionsUtility(this);
        }
        return permissions;
    }
    
    /**
     * Returns a handler that manages all config files
     * @return ConfigurationHandler
     */
    public ConfigurationHandler getConfigHandler() {
        if(config == null){
            config = new ConfigurationHandler(this);
        }
        return config;
    }

    
    /**
     * returns a custom loggertool
     * @return LoggerUtility
     */
    public LoggerUtility getLoggerUtility() {
        if(logger == null){
            logger = new LoggerUtility(this);
        }
        return logger;
    }

    
    /**
     * returns the same as isEnabled()
     * @return boolean isEnabled
     */
    public boolean isStarted() {
        return this.isEnabled();
    }
    
    
    /**
     * Called by ConfigurationHandler if loading of files failed
     * Disables the plugin
     */
    @Override
    public void onDisable() {
        setEnabled(false);
        long time = System.nanoTime();
        System.out.println("Paypassage disabled in " + ((System.nanoTime() - time) / 1000000 ) + " ms");
    }
 
    /**
     * Enables the plugin
     */
    @Override
    public void onEnable() {
        long time = System.nanoTime();
        getConfigHandler().onStart();
        logger = getLoggerUtility();
        getLoggerUtility().log("creating config!", LoggerUtility.Level.DEBUG);
        getLoggerUtility().log("init logger!", LoggerUtility.Level.DEBUG);
        report = getReportHandler();
        getLoggerUtility().log("init report!", LoggerUtility.Level.DEBUG);
        permissions = getPermissions();
        getLoggerUtility().log("init permissions!", LoggerUtility.Level.DEBUG);
        getLoggerUtility().log("Paypassage enabled in " + ((System.nanoTime() - time) / 1000000 ) + " ms", LoggerUtility.Level.INFO);
        setEnabled(true);
    }
    
    /**
     * Handles commands
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!isEnabled()){
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
                    if (args[0].equalsIgnoreCase("create")) {
                        //create Command
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage created");
                        return true;
                    }else if (args[0].equalsIgnoreCase("delete")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage deleted");
                        return true;
                    }else if (args[0].equalsIgnoreCase("info")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage Status:" + ChatColor.GREEN + "Working!" );
                        return true;
                    } else {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.RED + "Du hast irgendetwas falsch gemacht!");
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
