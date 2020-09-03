package tntWar;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;



public class main extends JavaPlugin {

	public static String tagPlugin;
	public static main plugin;
	
	public void onEnable() {
		
		tagPlugin = ChatColor.RESET + "[" + ChatColor.BLUE + getName() + ChatColor.RESET + "] ";
		plugin = this;
		
		this.getCommand("tnt").setExecutor((CommandExecutor)new commands());
		//this.getCommand("say").setExecutor((CommandExecutor)new commands());
		this.getLogger().info("Started!");
		
	}
	
}
