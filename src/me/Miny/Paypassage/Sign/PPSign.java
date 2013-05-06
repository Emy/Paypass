package me.Miny.Paypassage.Sign;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class PPSign {

	private Sign sign;
	private String owner;
    private Location destination;
    private String name;
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

	public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
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
