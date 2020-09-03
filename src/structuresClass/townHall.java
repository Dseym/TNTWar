package structuresClass;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import tntWar.main;
import tntWar.structure;
import tntWar.team;

public class townHall extends structure {
	
	public static String description = "Главное здание, при уничтожении - команда проигрывает";
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
				money += (int)(money/4);
		
		super.team.money += money;
		
	}
	
}
