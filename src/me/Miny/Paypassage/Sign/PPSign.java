package me.Miny.Paypassage.Sign;

import org.bukkit.Location;

public class PPSign {

	private Location sign_loc = null;
	private String owner;
    private Location destination = null;
    private String name = "";
    private double price = -1;

    public PPSign(String owner) {
    	this.setOwner(owner);
    }

    public Location getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

	public Location getSignLocation() {
        return sign_loc;
    }

    public void setSignLocation(Location sign) {
        this.sign_loc = sign;
    }
    
    public void setDestination(Location loc) {
        destination = loc;
    }

    public void setName(String name) {
        this.name = name;
    }

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
