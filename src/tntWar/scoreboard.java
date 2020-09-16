package tntWar;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class scoreboard {

	Scoreboard sb;
	Objective obj;
	team team;
	List<String> scores = new ArrayList<String>();
	
	@SuppressWarnings("deprecation")
	scoreboard(team team) {
		
		this.team = team;

		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		obj = sb.registerNewObjective("game", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("TNTWar");
		
	}
	
	@SuppressWarnings("deprecation")
	void reload() {
		
		for(String pN: team.playerNames) {
			
			Player p = Bukkit.getPlayer(pN);
			
			if(gameProcess.bossBars.containsKey(p))
				continue;
			
			p.setScoreboard(sb);
			BossBar bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
			bossBar.addPlayer(p);
			gameProcess.bossBars.put(p, bossBar);
			bossBar.setVisible(false);
			
		}
		
		for(String str: sb.getEntries()) {
			
			sb.resetScores(str);
			
		}
		
		obj.getScore("Команда: " + team.colorNick + team.name).setScore(7);
		obj.getScore("Игроков: " + team.playerNames.size()).setScore(6);
		obj.getScore("§0").setScore(5);
		obj.getScore("Ратуша: " + team.townHall.health + "/" + team.townHall.maxHealth).setScore(4);
		obj.getScore("Монет: " + team.money).setScore(3);
		obj.getScore("Молотки: " + (int)team.speedBuild).setScore(2);
		obj.getScore("Строится: " + (team.isNowBuild == null ? "ничего" : team.isNowBuild.name + "(" + (int)(((double)(team.isNowBuild.build.process) / (double)(team.isNowBuild.build.blocks.size()-1)) * 100) + "%)")).setScore(1);
		obj.getScore("Строений: " + team.structures.size()).setScore(0);
		
	}
	
}
