package me.Miny.Paypassage;

import me.Miny.Paypassage.logger.LoggerUtility;
import me.Miny.Paypassage.logger.LoggerUtility.Level;

import org.bukkit.entity.Player;

/**
 * 
 * @author ibhh
 */
public class Help {

	private Paypassage plugin;

	/**
	 * Constructor of help page
	 * 
	 * @param pl
	 */
	public Help(Paypassage pl) {
		plugin = pl;
	}

	/**
	 * Returns help to player
	 * 
	 * @param player A valid online player object
	 * @param args the command parameter
	 */
	public void help(final Player player, String[] args) {
		plugin.getLoggerUtility().log("Help executed!", Level.DEBUG);
		if (args.length == 0) {
			/**
			 * List all command with their usage
			 */
			for (String command : plugin.getCommands()) {
				if (plugin.getPermissions().checkpermissionssilent(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".permission"))) {
					plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".usage"), LoggerUtility.Level.INFO);
				}
			}
		} else if (args.length > 0) {
			boolean found = false;
			for (String command : plugin.getCommands()) {
				if (plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".name").equalsIgnoreCase(args[0])) {
					if (plugin.getPermissions().checkpermissions(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".permission"))) {
						plugin.getLoggerUtility().log(player, "-----------", LoggerUtility.Level.INFO);
						plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".usage"), LoggerUtility.Level.INFO);
						plugin.getLoggerUtility().log(player, plugin.getConfigHandler().getLanguage_config().getString("commands." + command + ".description"), LoggerUtility.Level.INFO);
						found = true;
						return;
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