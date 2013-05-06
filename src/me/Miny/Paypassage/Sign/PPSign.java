package me.Miny.Paypassage.Sign;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class PPSign {

	private Sign sign;
    private Location location;
    private String name;
    private double price = -1;

    public PPSign() {
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
