package tntWar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import structuresClass.artillery;
import structuresClass.bank;
import structuresClass.engineering;
import structuresClass.miner;
import structuresClass.minerIron;
import structuresClass.strongWall;
import structuresClass.towerArrow;
import structuresClass.towerBomb;
import structuresClass.townHall;
import structuresClass.wall;

public class commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player)sender;
		
		if(args[0].equalsIgnoreCase("start")) {
			
			if(gameProcess.start) {
				
				player.sendMessage(main.tagPlugin + "Сейчас идет игра, команда заблокирована");
				return true;
				
			}
			
			if(!gameProcess.startGame()) {
			
				player.sendMessage(main.tagPlugin + "Не хватает игроков");
				return true;
			
			}
			
			gameProcess.sendMessageAll("§eВыбирайте место для Ратуши, у Вас §nминута!");
			Bukkit.getPluginManager().registerEvents(new event(), main.plugin);
			
			Bukkit.getScheduler().runTaskLater(main.plugin, new Runnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					
					gameProcess.sendMessageAll("§4§l§nВойна началась!");
					
					for(team team: gameProcess.teams.values())
						if(!team.structures.containsValue("TownHall"))
							Bukkit.dispatchCommand(Bukkit.getPlayer(team.playerNames.get(0)), "tnt build TownHall");
					
					gameProcess.process();
					
				}
				
			}, 60*20);
			
		} else if(args[0].equalsIgnoreCase("build")) {
			
			if(gameProcess.getTeamPlayer(player) == null) {
				
				player.sendMessage(main.tagPlugin + "Вы не в команде");
				return true;
			
			}
			
			if(!gameProcess.start) {
			
				player.sendMessage(main.tagPlugin + "Игра не идет, команда недоступна");
				return true;
			
			}
			
			
				
			structure structure = structuresList(args[1], player);
			
			if(structure == null) {
				
				player.sendMessage(main.tagPlugin + "Этой структуры не существует");
				return true;
				
			}

			String messMeth = structure.build(structure.name == "TownHall", player);
			if(!messMeth.equalsIgnoreCase("true")) {
				
				player.sendMessage(main.tagPlugin + messMeth);
				structure = null;
				
			}
			
		} else if(args[0].equalsIgnoreCase("destroy")) {
			
			if(gameProcess.getTeamPlayer(player) == null) {
				
				player.sendMessage(main.tagPlugin + "Вы не в команде");
				return true;
			
			}
			
			if(!gameProcess.start) {
			
				player.sendMessage(main.tagPlugin + "Игра не идет, команда недоступна");
				return true;
			
			}
			
			structure structure = gameProcess.getStructureToLocation(player.getLocation());
			
			if(structure == null) {
				
				player.sendMessage(main.tagPlugin + "Здесь нет структур");
				return true;
			
			}
			
			if(structure.team != gameProcess.getTeamPlayer(player)) {
				
				player.sendMessage(main.tagPlugin + "Вы не состоите в команде этой структуры");
				return true;
			
			}
			
			structure.destroy(true);
			
		} else {
			
			player.sendMessage(main.tagPlugin + "Такой команды не существует");
			return true;
			
		}
		
		return true;
		
	}
	
	
	structure structuresList(String name, Player player) {
		
		structure structure = null;
		
		if(name.equalsIgnoreCase("TownHall"))
			structure = new townHall(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("TowerBomb"))
			structure = new towerBomb(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("Miner"))
			structure = new miner(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("Engineering"))
			structure = new engineering(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("TowerArrow"))
			structure = new towerArrow(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("Wall"))
			structure = new wall(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("StrongWall"))
			structure = new strongWall(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("Bank"))
			structure = new bank(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("Artillery"))
			structure = new artillery(player.getLocation(), gameProcess.getTeamPlayer(player));
		else if(name.equalsIgnoreCase("MinerIron"))
			structure = new minerIron(player.getLocation(), gameProcess.getTeamPlayer(player));
		
		return structure;
		
	}

}
