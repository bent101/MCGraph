package me.doogdoog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.udojava.evalex.Expression;


public class GraphCommand implements CommandExecutor {
	
	private MCGraphPlugin plugin;
	
	private final List<Material> possibleMaterials = new ArrayList<Material>(Arrays.asList(
			Material.RED_CONCRETE,
			Material.ORANGE_CONCRETE,
			Material.YELLOW_CONCRETE,
			Material.LIME_CONCRETE,
			Material.CYAN_CONCRETE,
			Material.LIGHT_BLUE_CONCRETE,
			Material.BLUE_CONCRETE,
			Material.PURPLE_CONCRETE,
			Material.MAGENTA_CONCRETE
	));
	int curMaterialIndex = 0;
	
	public GraphCommand(MCGraphPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Material material = possibleMaterials.get(curMaterialIndex);
		curMaterialIndex = (curMaterialIndex + 1) % possibleMaterials.size();
		
		String equation = String.join("", args).toLowerCase();
		if(equation.length() < 3) {
			sender.sendMessage("§c§lThe equation isn't long enough!");
			return false;
		}
		if(!equation.startsWith("y=")) {
			sender.sendMessage("§c§lThe equation must start with \"y =\"!");
			return false;
		}
		
		plugin.getCurEquations().add(equation);
		
		Bukkit.broadcastMessage("§aGraphing §l" + equation + "§r§a ...");
		
		Thread t = new Thread(new GraphRunnable(plugin, equation, material));
		t.start();
			
		Bukkit.broadcastMessage("§aDone");
		return true;
	}
	
	

}
