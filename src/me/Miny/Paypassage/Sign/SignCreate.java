package me.Miny.Paypassage.Sign;

import me.Miny.Paypassage.Paypassage;
import org.bukkit.Location;
import org.bukkit.block.Sign;

/**
 *
 * @author ibhh
 */
public class SignCreate extends PPSign{

    public SignCreate() {
    	super();
    }

    /**
     * Saves the selected destination, sign and name to the DB
     * @param plugin Needed for the scheduler and the DB connection
     * @throws InvalidSignCreation if one value is NOT set.
     */
    public void save(final Paypassage plugin) throws InvalidSignCreation {
    	final Sign sign = super.getSign();
    	final Location location = super.getLocation();
    	final String name = super.getName();
    	final double price = super.getPrice();
        if(sign == null){
            throw new InvalidSignCreation("You must choose a sign");
        }
        if(location == null){
            throw new InvalidSignCreation("You have to choose a destination first");
        }
        if(name.equals("")){
            throw new InvalidSignCreation("You have to choose an unique name first");
        }
        if(price == -1){
            throw new InvalidSignCreation("You have to choose a price first");
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getDatabaseUtility().insertNewSign(sign.getLocation(), location, name);
            }
        });
    }
}
