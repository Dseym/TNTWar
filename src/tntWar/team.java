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
	List<String> playerNames = new ArrayList<String>();
	String name;
	String colorNick;
	public townHall townHall;
	scoreboard scoreboard = new scoreboard(this);
	public int money = 0;
	double speedBuild = 100;
	structure isNowBuild = null;
	
	team(String name, String colorNick) {
		
		this.name = name;
		this.colorNick = colorNick;
		
	}
	
	void addPlayer(Player player) {
		
		player.sendMessage(main.tagPlugin + "§bВы играете за команду " + colorNick + "§o" + name);
		player.setCustomName(colorNick + player.getName());
		playerNames.add(player.getName());
		
	}
	
	@SuppressWarnings("deprecation")
	void isLost() {
		
		sendMessage("§4Ваша Ратуша уничтожена, §lВы проиграли");
		for(structure structure: structures.keySet())
			structure.destroy(false);
		for(String p: playerNames)
			Bukkit.getPlayer(p).setGameMode(GameMode.SPECTATOR);
		gameProcess.teams.remove(name);
		
		if(gameProcess.teams.size() == 1) {
			
			for(team winnerTeam: gameProcess.teams.values())
				winnerTeam.sendMessage("§2§lВы выйграли!");
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
			
		}

	}
	
	private String lastMessage = "";
	@SuppressWarnings("deprecation")
	void sendMessage(String mess) {
		
		if(lastMessage.equalsIgnoreCase(mess))
			return;
		
		for(String p: playerNames)
			Bukkit.getPlayer(p).sendMessage(main.tagPlugin + "§4§lКоманда>> §r" + mess);
		
		lastMessage = mess;
		
	}
	
}
