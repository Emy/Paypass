package me.Miny.Paypassage.PPListeners;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.Sign.InvalidSignLocationException;
import me.Miny.Paypassage.Sign.ListofCreations;
import me.Miny.Paypassage.Sign.ListofUsers;
import me.Miny.Paypassage.Sign.PPSign;
import me.Miny.Paypassage.Sign.PPSignStats;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for the Bukkit API
 * 
 * @author ibhh
 */
public class PPListener implements org.bukkit.event.Listener {

	/**
	 * Reference to the plugin instance
	 */
	public Paypassage plugin;

	public PPListener(Paypassage plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeft(PlayerQuitEvent e) {
		if (ListofCreations.getList().containsKey(e.getPlayer().getName())) {
			ListofCreations.getList().remove(e.getPlayer().getName());
		}
	}

	/**
	 * Recieves join events
	 * 
	 * @param event
	 */
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
			if (plugin.getPrivacy().getConfig().containsKey(event.getPlayer().getName())) {
				plugin.getLoggerUtility().log("user privacy set", LoggerUtility.Level.DEBUG);
				if (plugin.getPrivacy().getConfig().get(event.getPlayer().getName())) {
					plugin.getLoggerUtility().log("userdata not allowed", LoggerUtility.Level.DEBUG);
					plugin.getLoggerUtility().log("Player: " + "Anonymous" + " command: " + event.getMessage(), LoggerUtility.Level.DEBUG);
				} else {
					plugin.getLoggerUtility().log("Player: " + event.getPlayer().getName() + " command: " + event.getMessage(), LoggerUtility.Level.DEBUG);
				}
			}
		}
		this.plugin.getLoggerUtility().log("PlayerCommandPreprocessEvent handled in " + (System.nanoTime() - time) / 1000000 + " ms", LoggerUtility.Level.DEBUG);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e) {
		long time = System.nanoTime();
		if (BlockTools.isSign(e.getBlock())) {
			this.plugin.getLoggerUtility().log("Block is sign", LoggerUtility.Level.DEBUG);
			if (((Sign) e.getBlock().getState()).getLine(0).equalsIgnoreCase("[" + plugin.getConfig().getString("sign_headline") + "]")) {
				final BlockBreakEvent event = e;
				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@Override
					public void run() {
						try {
							if (!plugin.getDatabaseUtility().isindb(event.getBlock().getLocation())) {
								return;
							} else {
								PPSign sign = plugin.getDatabaseUtility().getSign(event.getBlock().getLocation());
								if (!event.getPlayer().getName().equals(sign.getOwner())) {
									if (!plugin.getPermissions().checkpermissions(event.getPlayer(), "Paypassage.admin")) {
										event.setCancelled(true);
										return;
									}
								}
								plugin.getDatabaseUtility().deleteSign(event.getBlock().getLocation());
								plugin.getLoggerUtility().log(event.getPlayer(), "PP Sign deleted", LoggerUtility.Level.INFO);
							}
						} catch (SQLException ex) {
							Logger.getLogger(PPListener.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				});
			}
		}
		this.plugin.getLoggerUtility().log("PlayerInteractEvent handled in " + (System.nanoTime() - time) / 1000000 + " ms", LoggerUtility.Level.DEBUG);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		long time = System.nanoTime();
		if (e.hasBlock()) {
			if (BlockTools.isSign(e.getClickedBlock())) {
				this.plugin.getLoggerUtility().log("Block is sign", LoggerUtility.Level.DEBUG);
				if (((Sign) e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase("[" + plugin.getConfig().getString("sign_headline") + "]")) {
					final PlayerInteractEvent event = e;
					plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
						@Override
						public void run() {
							try {
								if (!plugin.getDatabaseUtility().isindb(((Sign) event.getClickedBlock().getState()).getLocation())) {
									plugin.getLoggerUtility().log(event.getPlayer(), "Sign not registered!", LoggerUtility.Level.WARNING);
									return;
								} else {
									if (event.getPlayer().isSneaking()) {
										try {
											PPSignStats stats = plugin.getDatabaseUtility().getSignStatsByID(plugin.getDatabaseUtility().getSignID(event.getClickedBlock().getLocation()));
											plugin.getLoggerUtility().log(event.getPlayer(), "Teleports: " + stats.getCountOfTeleports(), LoggerUtility.Level.INFO);
											plugin.getLoggerUtility().log(event.getPlayer(), "Profit: " + stats.getProfit(), LoggerUtility.Level.INFO);
										} catch (InvalidSignLocationException e) {
											e.printStackTrace();
										}
										return;
									} else {
										PPSign sign = plugin.getDatabaseUtility().getSign(event.getClickedBlock().getLocation());
										if (ListofUsers.getList().containsKey(event.getPlayer().getName())) {
											ListofUsers.getList().remove(event.getPlayer().getName());
										}
										ListofUsers.getList().put(event.getPlayer().getName(), sign);
										plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

											@Override
											public void run() {
												if (ListofUsers.getList().containsKey(event.getPlayer().getName())) {
													ListofUsers.getList().remove(event.getPlayer().getName());
													plugin.getLoggerUtility().log(event.getPlayer(), "Teleport canceled!", LoggerUtility.Level.ERROR);
												}
											}
										}, 20 * 20);
										plugin.getLoggerUtility().log(event.getPlayer(), String.format(plugin.getConfigHandler().getLanguage_config().getString("interact.sign.notification.confirm"), sign.getPrice()), LoggerUtility.Level.WARNING);
									}
								}
							} catch (SQLException ex) {
								Logger.getLogger(PPListener.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					});
				}
				if (ListofCreations.getList().containsKey(e.getPlayer().getName())) {
					if (ListofCreations.getList().get(e.getPlayer().getName()).getSignLocation() == null) {
						if (((Sign) e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase("[" + plugin.getConfig().getString("sign_headline") + "]")) {
							try {
								if (plugin.getDatabaseUtility().isindb(e.getClickedBlock().getLocation())) {
									plugin.getLoggerUtility().log(e.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("creation.sign.notification21"), LoggerUtility.Level.ERROR);
									return;
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							ListofCreations.getList().get(e.getPlayer().getName()).setSignLocation(e.getClickedBlock().getLocation());
							plugin.getLoggerUtility().log(e.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("creation.sign.notification2"), LoggerUtility.Level.INFO);
							plugin.getLoggerUtility().log(e.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("creation.sign.notification4"), LoggerUtility.Level.INFO);
						} else {
							plugin.getLoggerUtility().log(e.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("creation.sign.nopaypassagesign"), LoggerUtility.Level.INFO);
						}
					} else {
						plugin.getLoggerUtility().log(e.getPlayer(), plugin.getConfigHandler().getLanguage_config().getString("creation.sign.notification3"), LoggerUtility.Level.ERROR);
					}
				}
			}
		}
		this.plugin.getLoggerUtility().log("PlayerInteractEvent handled in " + (System.nanoTime() - time) / 1000000 + " ms", LoggerUtility.Level.DEBUG);
	}
}
