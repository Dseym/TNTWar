package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class strongWall extends structure {
	
	public static List<String> description = Arrays.asList("����� ������� �����, ������������");
	public static int cost = 350;
	public static String name = "������� �����";
	public static String id = "StrongWall";

	public strongWall(Location loc, team team) {
		
		super(300, loc, id, team, cost);
		
	}
	
}
