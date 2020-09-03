package tntWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import structuresClass.artillery;
import structuresClass.bank;
import structuresClass.engineering;
import structuresClass.miner;
import structuresClass.minerIron;
import structuresClass.strongWall;
import structuresClass.towerArrow;
import structuresClass.towerBomb;
import structuresClass.wall;

public class event implements Listener {
	
	static Map<Player, Block> selectTarget = new HashMap<Player, Block>();
	Inventory build = Bukkit.createInventory(null, 18, "Строительство");
	
	event() {
		
		for(int i = 0; i < 9; i++) {
			
			ItemStack item = new ItemStack(Material.QUARTZ_BLOCK);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			switch (i) {
				case 0:
					lore.add(miner.description);
					lore.add("Стоимость: " + miner.cost);
					lore.add("ID: " + miner.id);
					meta.setDisplayName(miner.name);
				break;
				case 1:
					lore.add(towerArrow.description);
					lore.add("Стоимость: " + towerArrow.cost);
					lore.add("ID: " + towerArrow.id);
					meta.setDisplayName(towerArrow.name);
				break;
				case 2:
					lore.add(towerBomb.description);
					lore.add("Стоимость: " + towerBomb.cost);
					lore.add("ID: " + towerBomb.id);
					meta.setDisplayName(towerBomb.name);
				break;
				case 3:
					lore.add(wall.description);
					lore.add("Стоимость: " + wall.cost);
					lore.add("ID: " + wall.id);
					meta.setDisplayName(wall.name);
				break;
				case 4:
					lore.add(strongWall.description);
					lore.add("Стоимость: " + strongWall.cost);
					lore.add("ID: " + strongWall.id);
					meta.setDisplayName(strongWall.name);
				break;
				case 5:
					lore.add(bank.description);
					lore.add("Стоимость: " + bank.cost);
					lore.add("ID: " + bank.id);
					meta.setDisplayName(bank.name);
				break;
				case 6:
					lore.add(engineering.description);
					lore.add("Стоимость: " + engineering.cost);
					lore.add("ID: " + engineering.id);
					meta.setDisplayName(engineering.name);
				break;
				case 7:
					lore.add(artillery.description);
					lore.add("Стоимость: " + artillery.cost);
					lore.add("ID: " + artillery.id);
					meta.setDisplayName(artillery.name);
				break;
				case 8:
					lore.add(minerIron.description);
					lore.add("Стоимость: " + minerIron.cost);
					lore.add("ID: " + minerIron.id);
					meta.setDisplayName(minerIron.name);
				break;

			}
			
			meta.setLore(lore);
			item.setItemMeta(meta);
			build.setItem(i, item);
			
		}
		
	}
	
	@EventHandler
	void isPlayerBuyBuild(InventoryClickEvent e) {
		
		if(e.getClickedInventory() == build) {
			
			Bukkit.dispatchCommand(e.getWhoClicked(), "tnt build " + e.getCurrentItem().getItemMeta().getLore().get(2).split("ID: ")[1]);
			e.getWhoClicked().closeInventory();
			e.setCancelled(true);
			
		}

	}

	@EventHandler
	void isPlayerBreakBlockStructure(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		
		if(gameProcess.getTeamPlayer(player) == null)
			return;
		
		structure structure = gameProcess.getStructureToLocation(e.getBlock().getLocation());	
		
		if(structure != null && structure.blocksStructure.contains(e.getBlock())) {
			
			e.setCancelled(true);
			
			if(gameProcess.getTeamPlayer(player) == structure.team)
				return;
			
			structure.damage(1);
			player.sendMessage(main.tagPlugin + "§bРазрушение: " + structure.health + "/" + structure.maxHealth);
			
		}
		
	}
	
	@EventHandler
	void isPlayerClickToBomb(PlayerInteractEvent e) {
		
		Player player = e.getPlayer();
		
		if(!selectTarget.containsKey(player)) {
		
			Block block = e.getClickedBlock();
			try {
				
				if(block != null && block.getType() == Material.BARRIER) {
					
					structure structure = gameProcess.getStructureToLocation(block.getLocation());
					
					if(structure == null || !structure.bombs.containsKey(block))
						return;
					
					if(gameProcess.getTeamPlayer(player) != structure.team) {
						
						player.sendMessage(main.tagPlugin + "Это не Ваша структура");
						return;
						
					}
					
					player.sendMessage(main.tagPlugin + "Укажите траекторию и нажмите туда");
					selectTarget.put(player, block);
					e.setCancelled(true);
					
					Bukkit.getScheduler().runTaskLater(main.plugin, new Runnable() {
						
						@Override
						public void run() {
							
							if(!selectTarget.containsKey(player))
								return;
							
							player.sendMessage(main.tagPlugin + "Выбор траектории отменен");
							selectTarget.remove(player);
							
						}
						
					}, 5*20);
					
				} else if(e.getMaterial() == Material.NAME_TAG) {
					
					e.getPlayer().openInventory(build);
					e.setCancelled(true);
					
				}
				
			} catch (Exception e1) {
				
				
				
			}
			
		} else {
			
			structure structure = gameProcess.getStructureToLocation(selectTarget.get(player).getLocation());
			structure.bombs.replace(selectTarget.get(player), player.getTargetBlock(null, 50));
			player.sendMessage(main.tagPlugin + "Траектория задана");
			selectTarget.remove(player);
			e.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	void isBombAttackStructure(EntityExplodeEvent e) {
		
		for(Block block: e.blockList()) {
			
			structure structure = gameProcess.getStructureToLocation(block.getLocation());
			
			if(structure == null || !structure.blocksStructure.contains(block))
				continue;
			
			structure.damage(1);
			e.setCancelled(true);
			
		}
		
	}
	
}
