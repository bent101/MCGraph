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
	
	private final int[] dirs = new int[] {0, 1, 0, -1, 0};
	
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
		
		Expression expr = new Expression(equation.substring(2));
		
		int[][] yVals = new int[2 * plugin.graphSize + 1][2 * plugin.graphSize + 1];
		
		// fill in one block per x/z coordinate
		for(int x = plugin.origin.x - plugin.graphSize; x <= plugin.origin.x + plugin.graphSize; x++) {
			for(int z = plugin.origin.z - plugin.graphSize; z <= plugin.origin.z + plugin.graphSize; z++) {
				int r = x + plugin.graphSize, c = z + plugin.graphSize;
				
				yVals[r][c] = -1;
				
				float yFloat = 0;
				
				try {
					yFloat = expr
							.with("x", Double.toString(x / plugin.zoom))
							.with("z", Double.toString(z / plugin.zoom))
							.eval()
							.floatValue();
				} catch(Exception e) {
					continue;
				}
				
				int y = (int) (yFloat * plugin.zoom) + 128;
				
				int sum = 0;
				if(x == plugin.origin.x) sum++;
				if(y == plugin.origin.y) sum++;
				if(z == plugin.origin.y) sum++;
				boolean onAxis = sum > 1;
				
				if(y > 255 || y < 2 || onAxis) continue;
				
				yVals[r][c] = y;
				Coordinate coord = new Coordinate(x, y, z);
				coord.getBlock().setType(material);
				plugin.getFilledBlocks().add(coord);
			}
		}
		
		// fill in vertical gaps
		for(int x = plugin.origin.x - plugin.graphSize; x <= plugin.origin.x + plugin.graphSize; x++) {
			for(int z = plugin.origin.z - plugin.graphSize; z <= plugin.origin.z + plugin.graphSize; z++) {
				int r = x + plugin.graphSize, c = z + plugin.graphSize;
				
				if(yVals[r][c] == -1) continue;
				
				int ycenter = yVals[r][c];
				int ymin = ycenter, ymax = ycenter;
				
				for(int i = 0; i < 4; i++) {
					int radj = r + dirs[i], cadj = c + dirs[i+1];
					if(radj < 0 || radj > 2 * plugin.graphSize || cadj < 0 || cadj > 2 * plugin.graphSize)
						continue;
					int yadj = yVals[radj][cadj];
					if(yadj == -1) continue;
					
					ymin = Math.min(ymin, yadj);
					ymax = Math.max(ymax, yadj);
				}
				
				for(int y = ymin+1; y <= ymax-1; y++) {
					Coordinate coord = new Coordinate(x, y, z);
					coord.getBlock().setType(material);
					plugin.getFilledBlocks().add(coord);
				}
				
			}
		}
		
			
		Bukkit.broadcastMessage("§aDone");
		return true;
	}
	
	

}
