package tntWar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class gameProcess {
	
	static boolean start = false;
	static Map<String, team> teams = new HashMap<String, team>();
	static Map<Integer, BukkitTask> nowBuild = new HashMap<Integer, BukkitTask>();
	static Map<Player, BossBar> bossBars = new HashMap<Player, BossBar>();

	static structure getStructureToLocation(Location loc) {
		
		Chunk chunk = loc.getChunk();
		
		for(team team: teams.values()) {
			
			for(structure structure: team.structures.keySet()) {
				
				if(structure.chunks.contains(chunk))
					return structure;
				
			}
			
		}
		
		return null;
		
	}
	
	static boolean startGame() {
		
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		
		if(players.size() % 2 != 0)
			return false;
		
		team blue = new team("blue", "§9");
		team red = new team("red", "§c");
		teams.put("blue", blue);
		teams.put("red", red);
		
		for(Player player: players) {
			
			if(teams.get("blue").playerNames.size() == players.size() / 2)
				teams.get("red").addPlayer(player);
			else
				teams.get("blue").addPlayer(player);
			
		}
		
		start = true;
		
		return true;
		
	}
	
	public static team getTeamPlayer(Player p) {
		
		for(team team: teams.values()) {
			
			if(team.playerNames.contains(p.getName()))
				return team;
			
		}
		
		return null;
		
	}
	
	static void sendMessageAll(String mess) {
		
		for(Player p: Bukkit.getOnlinePlayers())
			p.sendMessage(main.tagPlugin + mess);
		
	}
	
	
	static void process() {
		
		for(Player p: bossBars.keySet()) {
			
			structure structure = getStructureToLocation(p.getLocation());
			BossBar bossBar = bossBars.get(p);
			
			if(structure != null) {
				
				bossBar.setVisible(true);
				bossBar.setTitle(structure.name);
				bossBar.setProgress(((double)structure.health / (double)structure.maxHealth));
				
			} else {
				
				bossBar.setVisible(false);
				
			}
			
		}

		for(team team: gameProcess.teams.values()) {
			
			team.scoreboard.reload();
			
			for(structure structure: team.structures.keySet()) {
				
				if(structure.name.equalsIgnoreCase("Engineering"))
					team.speedBuild += 0.1;
				
				if((structure.name.equalsIgnoreCase("Wall") || structure.name.equalsIgnoreCase("StrongWall")) && structure.health < structure.maxHealth)
					structure.health++;
				
				for(Block arrow: structure.arrows) {
					
					for(Player p: Bukkit.getOnlinePlayers()) {
						
						Location locPlayer = new Location(p.getLocation().getWorld(), p.getLocation().getX(), 0, p.getLocation().getZ());
						Location locArrow = new Location(arrow.getLocation().getWorld(), arrow.getLocation().getX(), 0, arrow.getLocation().getZ());
						if(getTeamPlayer(p) != team && locArrow.distance(locPlayer) < 51) {
							
							Entity arrowEntity = arrow.getLocation().getWorld().spawnEntity(arrow.getLocation(), EntityType.ARROW);
							arrowEntity.setVelocity(p.getLocation().toVector().subtract(arrow.getLocation().toVector()));
							
						}
						
					}
					
				}
				
				for(Block repair: structure.repairBlocks) {
					
					if(repair.getType() == Material.IRON_BLOCK && structure.health + 99 < structure.maxHealth) {
						
						repair.setType(Material.AIR);
						structure.health += 100;
						
					}
					
				}
				
			}
			
		}
		
		Bukkit.getScheduler().runTaskLater(main.plugin, new Runnable() {@Override public void run() {process();}}, 20);
		
	}
	
}
