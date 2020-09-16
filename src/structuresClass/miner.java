package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import tntWar.main;
import tntWar.structure;
import tntWar.team;

public class miner extends structure {
	
	public static List<String> description = Arrays.asList("Добывает монеты", "со временем уровень добычи повышается");
	public static int cost = 500;
	public static String name = "Шахта";
	public static String id = "Miner";
	
	int exp = 0;

	public miner(Location loc, team team) {
		
		super(400, loc, id, team, cost);
		
		Bukkit.getScheduler().runTaskTimer(main.plugin, new Runnable() {@Override public void run() {generateMoney();}}, 5*20, 5*20);
		
	}
	
	void generateMoney() {
		
		if(!super.isBuild)
			return;
		
		int money = (int)(Math.floor(Math.random() * exp+1) + 10 + Math.floor(exp/10));
		super.team.townHall.giveMoney(money);
		exp++;
		
	}
	
}
