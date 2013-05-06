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

    public SignCreate() {
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Sign getSign() {
        return sign;
    }
    
    

    public void setSign(Sign sign) {
        this.sign = sign;
    }
    
    public void setDestination(Location loc) {
        location = loc;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Saves the selected destination, sign and name to the DB
     * @param plugin Needed for the scheduler and the DB connection
     * @throws InvalidSignCreation if one value is NOT set.
     */
    public void save(final Paypassage plugin) throws InvalidSignCreation {
        if(sign == null){
            throw new InvalidSignCreation("You must choose a sign");
        }
        if(location == null){
            throw new InvalidSignCreation("You have to choose a destination first");
        }
        if(name.equals("")){
            throw new InvalidSignCreation("You have to choose an unique name first");
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getDatabaseUtility().insertNewSign(sign.getLocation(), location, name);
            }
        });
    }
}
