package me.doogdoog;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ZoomCommand implements CommandExecutor {
	
	private final DecimalFormat df = new DecimalFormat("#.##");
	private final double zoomAmount = 1.5;
	
	private MCGraphPlugin plugin;
	
	public ZoomCommand(MCGraphPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 1 || (!args[0].equals("in") && !args[0].equals("out"))) {
			sender.sendMessage("§c§lUsage: \"/zoom in\" or \"/zoom out\"");
			return false;
		}
		
		String zoomDir = args[0].toLowerCase();
		
		if(zoomDir.equals("in"))
			plugin.zoom *= zoomAmount;
		else
			plugin.zoom /= zoomAmount;
		
		Bukkit.broadcastMessage("§aZooming " + zoomDir + " to §lx" + df.format(plugin.zoom) + "§r§a...");
		plugin.reloadGraph(sender);
		Bukkit.broadcastMessage("§aDone");
		
		return true;
	}

}
