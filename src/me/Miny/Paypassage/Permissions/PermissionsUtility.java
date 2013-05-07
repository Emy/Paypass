package me.Miny.Paypassage.Permissions;

import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.entity.Player;

public class PermissionsUtility {

	private Paypassage plugin;
	public int PermPlugin = 0;

	public PermissionsUtility(Paypassage pl) {
		this.plugin = pl;
	}

	public boolean checkpermissionssilent(Player player, String action) {
		try {
			if (player.isOp()) {
				return true;
			}
			if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			sendGeneralErrorMessage(player, e);
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkpermissions(Player player, String action) {
		try {
			if (player.isOp()) {
				return true;
			}
			if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
				return true;
			} else {
				sendErrorMessage(player, action);
				return false;
			}
		} catch (Exception e) {
			sendGeneralErrorMessage(player, e);
			e.printStackTrace();
			return false;
		}

	}

	private void sendErrorMessage(Player player, String action) {
		plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfigHandler().getLanguage_config().getString("permission.error") + " (" + action + ")", LoggerUtility.Level.ERROR);
	}


	private void sendGeneralErrorMessage(Player player, Exception e) {
		plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
		plugin.getReportHandler().report(3331, "Couldnt check permission", e.getMessage(), "PermissionsChecker", e);
		plugin.getLoggerUtility().log(player, "Error on checking permissions!", LoggerUtility.Level.ERROR);
	}
}