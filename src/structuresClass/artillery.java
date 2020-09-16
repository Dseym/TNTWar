package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class artillery extends structure {
	
	public static List<String> description = Arrays.asList("Повышает количество выстреливающего динамита на 1");
	public static int cost = 2950;
	public static String name = "Артиллерия";
	public static String id = "Artillery";

	public artillery(Location loc, team team) {
		
		super(300, loc, id, team, cost);
		
	}
	
}
