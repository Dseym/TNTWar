package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import tntWar.main;
import tntWar.structure;
import tntWar.team;

public class townHall extends structure {
	
	public static List<String> description = Arrays.asList("Главное здание, при уничтожении - проигрыш");
	public static int cost = 0;
	public static String name = "Ратуша";
	public static String id = "TownHall";

	public townHall(Location loc, team team) {
		
		super(2000, loc, id, team, cost);
		
		Bukkit.getScheduler().runTaskTimer(main.plugin, new Runnable() {@Override public void run() {giveMoney(100);}}, 5*20, 5*20);
		
	}

	void giveMoney(int money) {
		
		if(!super.isBuild)
			return;
		
		for(structure structure: super.team.structures.keySet())
			if(structure.name.equalsIgnoreCase("Bank"))
				money += (int)(money/5);
		
		super.team.money += money;
		
	}
	
}
