package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class towerArrow extends structure {
	
	public static String description = "Стреляет во врага стрелами на расстоянии 40 блоков";
	public static int cost = 900;
	public static String name = "Башня лучников";
	public static String id = "TowerArrow";

	public towerArrow(Location loc, team team) {
		
		super(500, loc, id, team, cost);
		
	}
	
}
