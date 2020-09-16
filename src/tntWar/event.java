package tntWar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
					lore.addAll(miner.description);
					lore.add("ID: " + miner.id);
					meta.setDisplayName(miner.name);
				break;
				case 1:
					lore.addAll(towerArrow.description);
					lore.add("ID: " + towerArrow.id);
					meta.setDisplayName(towerArrow.name);
				break;
				case 2:
					lore.addAll(towerBomb.description);
					lore.add("ID: " + towerBomb.id);
					meta.setDisplayName(towerBomb.name);
				break;
				case 3:
					lore.addAll(wall.description);
					lore.add("ID: " + wall.id);
					meta.setDisplayName(wall.name);
				break;
				case 4:
					lore.addAll(strongWall.description);
					lore.add("ID: " + strongWall.id);
					meta.setDisplayName(strongWall.name);
				break;
				case 5:
					lore.addAll(bank.description);
					lore.add("ID: " + bank.id);
					meta.setDisplayName(bank.name);
				break;
				case 6:
					lore.addAll(engineering.description);
					lore.add("ID: " + engineering.id);
					meta.setDisplayName(engineering.name);
				break;
				case 7:
					lore.addAll(artillery.description);
					lore.add("ID: " + artillery.id);
					meta.setDisplayName(artillery.name);
				break;
				case 8:
					lore.addAll(minerIron.description);
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
			
			List<String> lore = e.getCurrentItem().getItemMeta().getLore();
			Bukkit.dispatchCommand(e.getWhoClicked(), "tnt build " + lore.get(lore.size()-1).split("ID: ")[1]);
			e.getWhoClicked().closeInventory();
			e.setCancelled(true);
			
		}

	}

	@EventHandler
	void isPlayerBreakBlockStructure(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		
		if(gameProcess.getTeamPlayer(player) == null)
			return;
		
		Location locBlock = e.getBlock().getLocation();
		structure structure = gameProcess.getStructureToLocation(locBlock);	
		
		if(structure != null && structure.blocksStructure.contains(e.getBlock())) {
			
			e.setCancelled(true);
			
			if(gameProcess.getTeamPlayer(player) == structure.team)
				return;
			
			structure.damage(1);
			player.sendMessage(main.tagPlugin + "§bРазрушение: " + structure.health + "/" + structure.maxHealth);
			structure.team.sendMessage("§cРазрушение §o" + structure.name + "§r§c " + locBlock.getBlockX() + "," + locBlock.getBlockY() + "," + locBlock.getBlockZ());
			
		}
		
	}
	
	private void activateChoiceTarget(Block block, Player player, PlayerInteractEvent e) {
		
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
			
		}, 30);
		
	}
	
	private void openShopBuild(Player player, PlayerInteractEvent e) {

		e.getPlayer().openInventory(build);
		e.setCancelled(true);
		
	}
	
	@EventHandler
	void isPlayerClick(PlayerInteractEvent e) {
		
		Player player = e.getPlayer();
		
		if(!selectTarget.containsKey(player)) {
		
			Block block = e.getClickedBlock();
			try {
				
				if(block != null && block.getType() == Material.BARRIER)
					activateChoiceTarget(block, player, e);
				else if(e.getMaterial() == Material.NAME_TAG)
					openShopBuild(player, e);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}
			
		} else {
			
			structure structure = gameProcess.getStructureToLocation(selectTarget.get(player).getLocation());
			structure.bombs.replace(selectTarget.get(player), player.getTargetBlock(null, 80));
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
			
			structure.damage(40);
			e.setCancelled(true);
			
			break;
			
		}
		
	}
	
	@EventHandler
	void isArrowDamagePlayer(EntityDamageByEntityEvent e) {
		
		Entity entity = e.getEntity();
		if(e.getDamager().getType() != EntityType.ARROW || entity.getType() != EntityType.PLAYER)
			return;
		
		e.setDamage(7);
		
		PlayerInventory playerInv = ((Player)entity).getInventory();
		
		List<String> armor = new ArrayList<String>();
		for(ItemStack item: playerInv.getArmorContents())
			armor.add(item.getType().name());
			
		if(armor.containsAll(Arrays.asList("LEATHER_HELMET", "LEATHER_CHESTPLATE", "LEATHER_LEGGINGS", "LEATHER_BOOTS")))
			e.setDamage(e.getDamage() - 1);
		else if(armor.containsAll(Arrays.asList("IRON_HELMET", "IRON_CHESTPLATE", "IRON_LEGGINGS", "IRON_BOOTS")))
			e.setDamage(e.getDamage() - 2);
		else if(armor.containsAll(Arrays.asList("DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS", "DIAMOND_BOOTS")))
			e.setDamage(e.getDamage() - 3);
		
	}
	
}
