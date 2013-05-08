
package me.Miny.Paypassage.Sign;

import me.Miny.Paypassage.Paypassage;

/**
 *
 * @author ibhh
 */
public class ToLongTeleportException extends Exception {

	private static final long serialVersionUID = 1L;

	public ToLongTeleportException(final Paypassage plugin, final double distance) {
        super(plugin.getConfigHandler().getLanguage_config().getString("creation.sign.notification7") + distance + " max: " + plugin.getConfigHandler().getConfig().getInt("maxTeleportDistance"));
    }
}
