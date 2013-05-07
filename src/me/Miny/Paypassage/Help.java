package me.Miny.Paypassage;

import me.Miny.Paypassage.logger.LoggerUtility;

import org.bukkit.entity.Player;

/**
 * 
 * @author ibhh
 */
public class Help {

	private Paypassage plugin;

	/**
	 * Konstruktor of help page
	 * 
	 * @param pl
	 */
	public Help(Paypassage pl) {
		plugin = pl;
	}

	/**
	 * Returns help to player
	 * 
	 * @param sender
	 * @param args
	 */
	public void help(final Player player, String[] args) {
		if (args.length == 0) {
			for (String command : plugin.getCommands()) {
				if (plugin.getPermissions().checkpermissionssilent(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".permission"))) {
					plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".usage"), LoggerUtility.Level.INFO);
				}
			}
		} else if (args.length == 2 || args.length == 1) {
			boolean found = false;
			for (String command : plugin.getCommands()) {
				if (plugin.getConfig().getString("commands." + command + ".name").equalsIgnoreCase(args[1])) {
					if (plugin.getPermissions().checkpermissions(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".permission"))) {
						plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".description"), LoggerUtility.Level.INFO);
						found = true;
					}
				}
			}
			if (!found) {
				for (String command : plugin.getCommands()) {
					if (plugin.getPermissions().checkpermissionssilent(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".permission"))) {
						plugin.getLoggerUtility().log(player, "-----------", LoggerUtility.Level.INFO);
						plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".usage"), LoggerUtility.Level.INFO);
						plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".description"), LoggerUtility.Level.INFO);
					}
				}
			}
		}
	}
}