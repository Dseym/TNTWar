package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class engineering extends structure {
	
	public static String description = "���������� ���������";
	public static int cost = 1250;
	public static String name = "����������";
	public static String id = "Engineering";

	public engineering(Location loc, team team) {
		
		super(500, loc, id, team, cost);
		
	}
	
}
