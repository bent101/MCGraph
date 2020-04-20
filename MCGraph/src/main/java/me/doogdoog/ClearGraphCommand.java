package me.doogdoog;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearGraphCommand implements CommandExecutor {
	
	private MCGraphPlugin plugin;
	
	public ClearGraphCommand(MCGraphPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		Bukkit.broadcastMessage("§aClearing the graph...");
		
		for(Coordinate coord : plugin.getFilledBlocks()) {
			coord.getBlock().setType(Material.AIR);
		}
		
		plugin.getFilledBlocks().clear();
		plugin.getCurEquations().clear();
		
		Bukkit.broadcastMessage("§a§lCleared the graph.");
		return true;
		
	}

}
