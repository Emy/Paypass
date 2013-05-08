package me.Miny.Paypassage.Sign;

import java.util.ArrayList;
import java.util.Iterator;

public class PPSignStats extends ArrayList<PPTeleport> {

	private static final long serialVersionUID = 1L;
	
	public int getCountOfTeleports() {
		return this.size();
	}
	
	public double getProfit() {
		double a = 0;
		for (Iterator<PPTeleport> iterator = iterator(); iterator.hasNext();) {
			a = iterator.next().getPrice() + a;
		}
		return a;
	}
	
}
