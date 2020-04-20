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
	
	private static final Color[] colors = new Color[] {
			Color.RED,
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			Color.AQUA,
			Color.BLUE,
			Color.DARK_BLUE,
			Color.PURPLE,
			Color.MAGENTA
	};
	
	private int curColorIx = 0;
	
	public GraphCommand(MCGraphPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String equation = String.join(" ", args).toLowerCase();
		Color equationColor = null;
		
		if(!(equation.startsWith("y=") || equation.startsWith("y ="))) {
			sender.sendMessage("§cThe equation must start with \"y =\"!");
			return true;
		}
		
		for(Color color : colors) {
			if(equation.endsWith(plugin.getColorName(color))) {
				equationColor = color;
				equation = equation.replace(plugin.getColorName(color), "");
				break;
			}
		}
		
		if(equationColor == null) {
			equationColor = colors[curColorIx++];
			curColorIx %= colors.length;
		}
		
		ColoredEquation coloredEquation = new ColoredEquation(equationColor, equation);
		
		plugin.getCurEquations().add(coloredEquation);
		
		Bukkit.broadcastMessage("§aGraphing §l" + plugin.getColorPrefix(equationColor) + equation + "§r§a ...");
		
		Bukkit.getScheduler().runTask(plugin, new GraphRunnable(plugin, coloredEquation));
			
		Bukkit.broadcastMessage("§aDone");
		return true;
	}
	
	

}
