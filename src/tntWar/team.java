package tntWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import structuresClass.townHall;

public class team {

	public Map<structure, String> structures = new HashMap<structure, String>();
	List<Player> players = new ArrayList<Player>();
	String name;
	String colorNick;
	public townHall townHall;
	scoreboard scoreboard = new scoreboard(this);
	public int money = 0;
	double speedBuild = 80;
	structure isNowBuild = null;
	
	team(String name, String colorNick) {
		
		this.name = name;
		this.colorNick = colorNick;
		
	}
	
	void addPlayer(Player player) {
		
		player.sendMessage(main.tagPlugin + "§bВы играете за команду " + colorNick + "§o" + name);
		player.setCustomName(colorNick + player.getName());
		players.add(player);
		
	}
	
	void isLost() {
		
		sendMessage("§4Ваша Ратуша уничтожена, §lВы проиграли");
		for(structure structure: structures.keySet())
			structure.destroy(false);
		for(Player p: players)
			p.setGameMode(GameMode.SPECTATOR);
		gameProcess.teams.remove(name);
		
		if(gameProcess.teams.size() == 1) {
			
			for(team winnerTeam: gameProcess.teams.values())
				winnerTeam.sendMessage("§2§lВы выйграли!");
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
			
		}

	}
	
	void sendMessage(String mess) {
		
		for(Player p: players) {
			
			p.sendMessage(main.tagPlugin + "§4§lКоманда>> §r" + mess);
			
		}
		
	}
	
}
