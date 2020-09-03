package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class bank extends structure {
	
	public static String description = "Повышает все доходы до 25%";
	public static int cost = 1100;
	public static String name = "Банк";
	public static String id = "Bank";

	public bank(Location loc, team team) {
		
		super(400, loc, id, team, cost);
		
	}
	
}
