package structuresClass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tntWar.main;
import tntWar.structure;
import tntWar.team;

public class minerIron extends structure {
	
	public static String description = "Добывает железо, которое можно собрать(находясь рядом), со временем уровень добычи повышается";
	public static int cost = 850;
	public static String name = "Шахта железа";
	public static String id = "MinerIron";
	
	int iron = 0;
	int exp = 0;

	public minerIron(Location loc, team team) {
		
		super(600, loc, id, team, cost);
		
		Bukkit.getScheduler().runTaskTimer(main.plugin, new Runnable() {@Override public void run() {generateIron();}}, 5*20, 5*20);
		
	}
	
	void generateIron() {
		
		if(!super.isBuild)
			return;
		
		iron += (exp / 10) + 1;
		if(exp < 40)
			exp++;
		
		for(Player p: Bukkit.getOnlinePlayers())
			if(super.chunks.contains(p.getLocation().getChunk())) {
				
				p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, iron));
				iron = 0;
				break;
				
			}
		
	}
	
}
