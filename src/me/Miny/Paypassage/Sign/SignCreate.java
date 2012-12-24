package me.Miny.Paypassage.Sign;

import me.Miny.Paypassage.Paypassage;
import org.bukkit.Location;
import org.bukkit.block.Sign;

/**
 *
 * @author ibhh
 */
public class SignCreate {

    private Sign sign;
    private Location location;
    private String name;

    public SignCreate(Sign sign) {
        this.sign = sign;
    }

    public void setDestination(Location loc) {
        location = loc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save(final Paypassage plugin) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getDatabaseUtility().insertNewSign(sign.getLocation(), location, name);
            }
        });
    }
}
