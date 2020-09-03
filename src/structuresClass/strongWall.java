package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class strongWall extends structure {
	
	public static String description = "Более крепкая стена, регенерирует";
	public static int cost = 350;
	public static String name = "Крепкая стена";
	public static String id = "StrongWall";

	public strongWall(Location loc, team team) {
		
		super(300, loc, id, team, cost);
		
	}
	
}
