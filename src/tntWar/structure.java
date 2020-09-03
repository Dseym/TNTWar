package tntWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;

import structuresClass.townHall;

public class structure implements Runnable {
	
	public int maxHealth;
	public int health;
	Location location;
	public List<Chunk> chunks = new ArrayList<Chunk>();
	public String name;
	public team team;
	List<Block> blocksStructure = new ArrayList<Block>();
	Map<Block, Block> bombs = new HashMap<Block, Block>();
	List<Block> arrows = new ArrayList<Block>();
	List<Block> repairBlocks = new ArrayList<Block>();
	BukkitTask timer = null;
	int cost = 0;
	public boolean isBuild = false;
	build build;
	
	
	public structure(int maxHealth, Location loc, String name, team team, int cost) {
		
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.name = name;
		this.team = team;
		this.cost = cost + ((int)((cost/6) * team.structures.size()));
		
		chunks.add(loc.getChunk());
		location = new Location(loc.getWorld(), (chunks.get(0).getX()*16), loc.getY(), chunks.get(0).getZ()*16);
		
	}
	
	void damage(int damage) {
		
		health -= damage;
		
		if(health < 1)
			destroy(false);
		
	}
	
	void destroy(boolean isCommand) {	
		
		team.sendMessage("§cСтроение §o" + name + " §r§cуничтожено");
		if(timer != null)
			timer.cancel();
		isBuild = false;
		team.structures.remove(this);
		
		if(name == "TownHall")
			team.isLost();
		
		World world = BukkitAdapter.adapt(location.getWorld());
		
		if(isCommand)
			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
			    
				CuboidRegion cuboid = new CuboidRegion(BlockVector3.at(location.getX(), blocksStructure.get(0).getY()-1, location.getZ()), BlockVector3.at(location.getX()+15, blocksStructure.get(blocksStructure.size()-1).getY()+1, location.getZ()+15));
				world.regenerate(cuboid, editSession);
				
			}
		
	}
	
	String build(boolean instant, Player player) {
		
		if(name == "TownHall" && team.structures.containsValue("TownHall"))
			return "Эта структура может быть только одна на команду";
		if(team.isNowBuild != null)
			return "В вашей команде уже что-то строиться";
		if(team.money < cost)
			return "Не хватает монет, нужно " + cost;
		
		int id = gameProcess.nowBuild.size();
		if(instant)
			build = new build(id, location, this, 20000, player);
		else
			build = new build(id, location, this, (int)team.speedBuild, player);
		
		for(Chunk chunk: chunks)
			if(gameProcess.getStructureToLocation(new Location(chunk.getWorld(), chunk.getX()*16, 0, chunk.getZ()*16)) != null)
				return "Эта структура задевает другую структуру";
		
		
		team.structures.put(this, name);
		
		if(name == "TownHall")
			team.townHall = (townHall)this;
		
		team.sendMessage("§9Началось строительство §o" + name);
		
		team.isNowBuild = this;
		team.money -= cost;
		gameProcess.nowBuild.put(id, Bukkit.getScheduler().runTaskTimer(main.plugin, build, 20, 20));
		
		return "true";
		
	}
	
	@SuppressWarnings("incomplete-switch")
	void isBuild() {
		
		team.sendMessage("§aСтруктура §o" + name + " §r§aдостроилась");
		team.isNowBuild = null;
		isBuild = true;
		build = null;
		
		for(Block block: blocksStructure) {
			
			if(block.getType() != Material.BARRIER)
				continue;
			
			Location locationUpBlock = new Location(location.getWorld(), block.getX(), block.getY()+1, block.getZ());
			switch (locationUpBlock.getBlock().getType()) {
				case OAK_SIGN:
					bombs.put(block, null);
					
					if(timer == null)
						timer = Bukkit.getScheduler().runTaskTimer(main.plugin, this, 6*20, 5*20);
					locationUpBlock.getBlock().setType(Material.AIR);
				break;

				case BIRCH_SIGN:
					arrows.add(block);
					locationUpBlock.getBlock().setType(Material.AIR);
					block.setType(Material.AIR);
				break;
					
				case SPRUCE_SIGN:
					repairBlocks.add(block);
					locationUpBlock.getBlock().setType(Material.AIR);
					block.setType(Material.AIR);
				break;
			
			}
			
		}
		
	}

	
	@Override
	public void run() {
		
		for(Block bomb: bombs.keySet()) {
			
			if(bombs.get(bomb) != null) {
				
				Entity bombEntity = location.getWorld().spawnEntity(bomb.getLocation(), EntityType.PRIMED_TNT);
				bombEntity.setVelocity(bombs.get(bomb).getLocation().toVector().subtract(bomb.getLocation().toVector()).multiply(0.04));
				
				for(structure structure: team.structures.keySet())
					if(structure.name.equalsIgnoreCase("Artillery")) {
						
						bombEntity = location.getWorld().spawnEntity(bomb.getLocation(), EntityType.PRIMED_TNT);
						bombEntity.setVelocity(bombs.get(bomb).getLocation().toVector().subtract(bomb.getLocation().toVector()).multiply(0.04));
				
						
					}
				
			}
			
		}
		
	}

}
