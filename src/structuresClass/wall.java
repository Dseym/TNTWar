package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class wall extends structure {
	
	public static String description = "������� �����, ������������";
	public static int cost = 100;
	public static String name = "�����";
	public static String id = "Wall";

	public wall(Location loc, team team) {
		
		super(100, loc, id, team, cost);
		
	}
	
}
