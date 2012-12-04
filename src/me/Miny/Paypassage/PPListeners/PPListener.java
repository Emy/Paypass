package me.Miny.Paypassage.PPListeners;

import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author ibhh
 */
public class PPListener implements org.bukkit.event.Listener {

    public Paypassage plugin;

    public PPListener(Paypassage plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        long time = System.nanoTime();
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
            final Player player = event.getPlayer();
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!plugin.getPrivacy().getConfig().containsKey(player.getName()) && plugin.getConfigHandler().getConfig().getBoolean("users_can_choose_privacy")) {
                        plugin.getLoggerUtility().log("no privacy set!", LoggerUtility.Level.DEBUG);
                        plugin.getLoggerUtility().log(player.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.1"), LoggerUtility.Level.WARNING);
                        plugin.getLoggerUtility().log(player.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.2"), LoggerUtility.Level.WARNING);
                        plugin.getLoggerUtility().log(player.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("privacy.notification.3"), LoggerUtility.Level.WARNING);
                    }
                }
            }, 20);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Paypassage] Error: Uncatched Exeption!");
            this.plugin.getReportHandler().report(3321, "Player join throws error", e.getMessage(), "PaypassageListener", e);
        }
        this.plugin.getLoggerUtility().log("Player join event handled in " + (System.nanoTime() - time) / 1000000 + " ms", LoggerUtility.Level.DEBUG);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void precommand(PlayerCommandPreprocessEvent event) {
        long time = System.nanoTime();
        if (plugin.isEnabled() && (event.getMessage().toLowerCase().startsWith("/pp".toLowerCase())) && (plugin.getConfigHandler().getConfig().getBoolean("debugfile"))) {
            plugin.getLoggerUtility().log("Player: " + event.getPlayer().getName() + " command: " + event.getMessage());
        }
        this.plugin.getLoggerUtility().log("PlayerCommandPreprocessEvent handled in " + (System.nanoTime() - time) / 1000000 + " ms", LoggerUtility.Level.DEBUG);
    }
}
