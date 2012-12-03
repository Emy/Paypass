package me.Miny.Paypassage.Listeners;

import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author ibhh
 */
public class Listener implements org.bukkit.event.Listener {

    public Paypassage plugin;

    public Listener(Paypassage plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            if (plugin.isEnabled()) {
                if ((this.plugin.getPermissions().checkpermissionssilent(event.getPlayer(), "Paypassage.admin")) && (plugin.getUpdate().isUpdateaviable())) {
                    this.plugin.getLoggerUtility().log(event.getPlayer(), "installed Paypassage version: " + this.plugin.getVersion() + ", latest version: " + this.plugin.getUpdate().getNewversion(), LoggerUtility.Level.WARNING);
                    this.plugin.getLoggerUtility().log(event.getPlayer(), "New Paypassage update aviable: type \"/Paypassage update\" to install!", LoggerUtility.Level.WARNING);
                    if (!this.plugin.getConfig().getBoolean("installondownload")) {
                        this.plugin.getLoggerUtility().log(event.getPlayer(), "Please edit the config.yml if you wish that the plugin updates itself atomatically!", LoggerUtility.Level.WARNING);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Paypassage] Error: Uncatched Exeption!");
            this.plugin.getReportHandler().report(3321, "Player join throws error", e.getMessage(), "PaypassageListener", e);
        }
        if (!plugin.getPrivacy().getConfig().containsKey(event.getPlayer().getName()) && plugin.getConfigHandler().getConfig().getBoolean("users_can_choose_privacy")) {
            this.plugin.getLoggerUtility().log("no privacy set!", LoggerUtility.Level.DEBUG);
            this.plugin.getLoggerUtility().log(event.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.1"), LoggerUtility.Level.WARNING);
            this.plugin.getLoggerUtility().log(event.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.2"), LoggerUtility.Level.WARNING);
            this.plugin.getLoggerUtility().log(event.getPlayer(),  plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.3"), LoggerUtility.Level.WARNING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void precommand(PlayerCommandPreprocessEvent event) {
        if (plugin.isEnabled() && (event.getMessage().toLowerCase().startsWith("/Paypassage".toLowerCase())) && (plugin.getConfigHandler().getConfig().getBoolean("debugfile"))) {
            plugin.getLoggerUtility().log("Player: " + event.getPlayer().getName() + " command: " + event.getMessage());
        }
    }
}
