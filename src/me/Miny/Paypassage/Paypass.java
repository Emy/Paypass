package me.Miny.Paypassage;
 
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
    
    //status
    private boolean started = false;
    //Modules
    private LoggerUtility logger;
    private ConfigurationHandler conifg;
    private ReportToHost report;

    public ReportToHost getReportHandler() {
        if(report == null){
            report = new ReportToHost(this);
        }
        return report;
    }
    
    public ConfigurationHandler getConfigHandler() {
        return conifg;
    }

    public LoggerUtility getLoggerUtility() {
        return logger;
    }

    public boolean isStarted() {
        return started;
    }
    
    @Override
    public void onDisable() {
        long time = System.nanoTime();
        System.out.println("Paypassage Deaktiviert in " + ((System.nanoTime() - time) / 1000000 ) + " ms");
 
    }
 
    @Override
    public void onEnable() {
        long time = System.nanoTime();
        logger = new LoggerUtility(this);
        conifg = new ConfigurationHandler(this);
        report = new ReportToHost(this);
        System.out.println("Paypassage Aktiviert in" + ((System.nanoTime() - time) / 1000000 ) + " ms");
    }
 
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
 
            Player player = (Player) sender;
            /**
             * /pp create command
             */
            if (command.getName().equalsIgnoreCase("pp")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("create")) {
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
