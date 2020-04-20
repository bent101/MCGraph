package me.doogdoog;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetGraphSizeCommand implements CommandExecutor {
	
	private MCGraphPlugin plugin;
	
	public SetGraphSizeCommand(MCGraphPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 1) {
			sender.sendMessage("§c§lUsage: /setgraphsize <size in blocks>");
			return false;
		}
		
		// checks if the given size is a positive integer
		for(char c : args[0].toCharArray()) {
			if(!Character.isDigit(c)) {
				sender.sendMessage("§c§lThe size must be a positive whole number.");
				return false;
			}
		}
		
		Bukkit.broadcastMessage("§aSetting the graph size to §l" + args[0] + "§r§a...");
		
		plugin.eraseAxes();
		plugin.graphSize = Integer.parseInt(args[0]);
		plugin.drawAxes();
		
		plugin.reloadGraph(sender);
		
		Bukkit.broadcastMessage("§aDone");
		
		return true;
		
	}

}
