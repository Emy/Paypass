package me.Miny.Paypassage.Sign;

import me.Miny.Paypassage.Paypassage;

/**
 *
 * @author ibhh
 */
public class SignCreate extends PPSign{

    public SignCreate(String owner) {
    	super(owner);
    }

    /**
     * Saves the selected destination, sign and name to the DB
     * @param plugin Needed for the scheduler and the DB connection
     * @throws InvalidSignCreation if one value is NOT set.
     */
    public void save(final Paypassage plugin) throws InvalidSignCreation {
        if(super.getSignLocation() == null){
            throw new InvalidSignCreation("You must choose a sign");
        }
        if(super.getDestination() == null){
            throw new InvalidSignCreation("You have to choose a destination first");
        }
        if(super.getName().equals("")){
            throw new InvalidSignCreation("You have to choose an unique name first");
        }
        if(super.getPrice() == -1){
            throw new InvalidSignCreation("You have to choose a price first");
        }
        final PPSign thisinstance = this;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getDatabaseUtility().insertNewSign(thisinstance);
            }
        });
    }
}
