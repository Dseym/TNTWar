package tntWar;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.TransformRegion;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.block.BlockState;

public class build implements Runnable {
	
	int id;
	Clipboard clipboard;
	Location location;
	int process = 0;
	List<BlockVector3> blocks = new ArrayList<BlockVector3>();
	int speed = 100;
	List<BlockState> state = new ArrayList<BlockState>();
	structure structure;
	Direction direction;
	
	@SuppressWarnings("incomplete-switch")
	build(int id, Location loc, structure structure, int speed, Player player) {
		
		this.id = id;
		File file = new File(Bukkit.getPluginManager().getPlugin("WorldEdit").getDataFolder() + File.separator + "schematics" + File.separator + (structure.name + ".schem"));
		location = loc;
		this.speed = speed;
		this.structure = structure;

		ClipboardFormat format = ClipboardFormats.findByFile(file);
		try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
			
		    clipboard = reader.read();
		    
		} catch (Exception e) {}
		
		for(BlockVector3 block: clipboard.getRegion())
			state.add(clipboard.getBlock(block));
		
		direction = BukkitAdapter.adapt(player).getCardinalDirection();
		Transform transform = new AffineTransform();
		switch (direction) {
			case SOUTH:
				transform = ((AffineTransform)transform).rotateY(90);
			break;
			
			case EAST:
				transform = ((AffineTransform)transform).rotateY(180);
			break;
			
			case NORTH:
				transform = ((AffineTransform)transform).rotateY(270);
			break;
		
		}
		TransformRegion reg = new TransformRegion(clipboard.getRegion(), transform);
		
		for(BlockVector3 locBlock: reg) {
			
			locBlock = locBlock.add((int)location.getX(), (int)location.getY()-100, (int)location.getZ());
			
			switch (direction) {
				case SOUTH:
					locBlock = locBlock.add(0, 0, 15);
				break;
				
				case EAST:
					locBlock = locBlock.add(15, 0, 15);
				break;
				
				case NORTH:
					locBlock = locBlock.add(15, 0, 0);
				break;
			
			}
			blocks.add(locBlock);
			Chunk chunk = BukkitAdapter.adapt(location.getWorld(), locBlock).getChunk();
			if(!structure.chunks.contains(chunk))
				structure.chunks.add(chunk);
			
		}
		
	}
	
	@Override
	public void run() {
		
		World world = location.getWorld();
		
		for(int i = 0; i < speed; i++) {
			
			BlockVector3 locationBlock = blocks.get(process);
			Material material = BukkitAdapter.adapt(state.get(process).getBlockType());
			new Location(world, locationBlock.getX(), locationBlock.getY(), locationBlock.getZ()).getBlock().setType(material);
			
			Chunk chunk = BukkitAdapter.adapt(location.getWorld(), locationBlock).getChunk();
			if(!structure.chunks.contains(chunk))
				structure.chunks.add(chunk);
			
			List<String> ignoreMaterials = new ArrayList<String>();
			ignoreMaterials.addAll(Arrays.asList("AIR", "DIRT", "GRASS", "GRASS_BLOCK", "TORCH", "REDSTONE_TORCH", "REDSTONE", "LADDER", "RAIL", "OAK_DOOR", "SPRUCE_DOOR", "OAK_BUTTON", "STONE_BUTTON", "GRAVEL", "SAND", "OAK_SIGN", "IRON_DOOR", "VINE", "STONE_PRESSURE_PLATE", "FLOWER_POT", "TRIPWIRE_HOOK", "OAK_PRESSURE_PLATE", "WHITE_BED", "SMOOTH_QUARTZ_STAIRS", "LANTERN", "IRON_TRAPDOOR", "OAK_TRAPDOOR", "SPRUCE_TRAPDOOR", "DAYLIGHT_DETECTOR", "OAK_LEAVES", "CAMPFIRE", "GRASS_PATH", "SNOW", "BREWING_STAND"));
			
			if(!ignoreMaterials.contains(material.toString()))
				gameProcess.getStructureToLocation(location).blocksStructure.add(BukkitAdapter.adapt(world, locationBlock).getBlock());

			process++;

			if(process > blocks.size()-1) {
				
				gameProcess.getStructureToLocation(location).isBuild();
				gameProcess.nowBuild.get(id).cancel();
				gameProcess.nowBuild.replace(id, null);
				
				return;
				
			}
			
		}
		
	}
	
}
