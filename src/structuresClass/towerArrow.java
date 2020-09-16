package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class towerArrow extends structure {
	
	public static List<String> description = Arrays.asList("Стреляет во врага стрелами на расстоянии 50 блоков");
	public static int cost = 900;
	public static String name = "Башня лучников";
	public static String id = "TowerArrow";

	public towerArrow(Location loc, team team) {
		
		super(500, loc, id, team, cost);
		
	}
	
}
