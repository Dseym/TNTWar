package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class bank extends structure {
	
	public static List<String> description = Arrays.asList("Повышает все доходы на 20%");
	public static int cost = 1100;
	public static String name = "Банк";
	public static String id = "Bank";

	public bank(Location loc, team team) {
		
		super(400, loc, id, team, cost);
		
	}
	
}
