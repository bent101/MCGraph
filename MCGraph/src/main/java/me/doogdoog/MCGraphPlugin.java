package me.doogdoog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MCGraphPlugin extends JavaPlugin {
	
	public int graphSize = 30;
	public double zoom = 1;
	public Coordinate origin = new Coordinate(0, 128, 0);
	
	private List<String> curEquations;
	private Set<Coordinate> filledBlocks;
	
	
	public void onEnable() {
		getCommand("graph").setExecutor(new GraphCommand(this));
		getCommand("cleargraph").setExecutor(new ClearGraphCommand(this));
		getCommand("zoom").setExecutor(new ZoomCommand(this));
		getCommand("setgraphsize").setExecutor(new SetGraphSizeCommand(this));
		
		curEquations = new ArrayList<String>();
		filledBlocks = new HashSet<Coordinate>();
		
		drawAxes();
	}
	
	public void onDisable() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cleargraph");
	}
	
	public void addGraph(String equation) {
		curEquations.add(equation);
	}
	
	public Set<Coordinate> getFilledBlocks() {
		return filledBlocks;
	}
	
	public List<String> getCurEquations() {
		return curEquations;
	}
	
	public void reloadGraph(CommandSender sender) {
		
		// clear the graph
		for(Coordinate coord : filledBlocks)
			coord.getBlock().setType(Material.AIR);
		
		filledBlocks.clear();
		
		// re-graph each equation
		for(String equation : curEquations) {
			Bukkit.dispatchCommand(sender, "graph " + equation);
		}
		
		
		
	}
	
	public void drawAxes() {
		
		World world = Bukkit.getWorlds().get(0);
		
		// x and z axes
		for(int i = -graphSize; i <= graphSize; i++) {
			world.getBlockAt(i, origin.y, origin.z).setType(Material.BLACK_CONCRETE);
			world.getBlockAt(origin.x, origin.y, i).setType(Material.BLACK_CONCRETE);
		}
		
		// y axis
		for(int i = 2; i < 256; i++) {
			world.getBlockAt(origin.x, i, origin.z).setType(Material.BLACK_CONCRETE);
		}
	}
	
	public void eraseAxes() {
		
		World world = Bukkit.getWorlds().get(0);
		
		// x and z axes
		for(int i = -graphSize; i <= graphSize; i++) {
			world.getBlockAt(i, origin.y, origin.z).setType(Material.AIR);
			world.getBlockAt(origin.x, origin.y, i).setType(Material.AIR);
		}
		
		// y axis
		for(int i = 2; i < 256; i++) {
			world.getBlockAt(origin.x, i, origin.z).setType(Material.AIR);
		}
	}
}
