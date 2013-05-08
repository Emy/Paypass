package me.Miny.Paypassage.Sign;

public class PPTeleport {

	private double price;
	private int sign_id;
	private long time;
	private String player;
	private String owner;
	
	public PPTeleport(int SignID, long time, String player, String owner, double price) {
		this.price = price;
		this.sign_id = SignID;
		this.time = time;
		this.player = player;
		this.owner = owner;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getSign_id() {
		return sign_id;
	}
	
	public long getTime() {
		return time;
	}
	
}
