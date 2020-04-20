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
	
	private final int axesExtraLength = 5;
	
	public int graphSize = 30;
	public double zoom = 1;
	public Coordinate origin = new Coordinate(0, 128, 0);
	
	private List<ColoredEquation> curEquations;
	private Set<Coordinate> filledBlocks;
	
	
	public void onEnable() {
		getCommand("graph").setExecutor(new GraphCommand(this));
		getCommand("cleargraph").setExecutor(new ClearGraphCommand(this));
		getCommand("zoom").setExecutor(new ZoomCommand(this));
		getCommand("setgraphsize").setExecutor(new SetGraphSizeCommand(this));
		
		curEquations = new ArrayList<ColoredEquation>();
		filledBlocks = new HashSet<Coordinate>();
		
		drawAxes();
	}
	
	public void onDisable() {
		for(Coordinate coord : getFilledBlocks()) {
			coord.setToMaterial(Material.AIR);
		}
		
		getFilledBlocks().clear();
		getCurEquations().clear();
	}
	
	public Set<Coordinate> getFilledBlocks() {
		return filledBlocks;
	}
	
	public List<ColoredEquation> getCurEquations() {
		return curEquations;
	}
	
	public void reloadGraph(CommandSender sender) {
		
		// clear the graph
		for(Coordinate coord : filledBlocks)
			coord.setToMaterial(Material.AIR);
		
		filledBlocks.clear();
		
		// re-graph each equation
		for(ColoredEquation equation : curEquations) {
			Bukkit.getScheduler().runTask(this, new GraphRunnable(this, equation));
		}
		
	}
	
	public void drawAxes() {
		
		World world = Bukkit.getWorlds().get(0);
		
		// x and z axes
		for(int i = -graphSize-axesExtraLength; i <= graphSize+axesExtraLength; i++) {
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
		for(int i = -graphSize-axesExtraLength; i <= graphSize+axesExtraLength; i++) {
			world.getBlockAt(i, origin.y, origin.z).setType(Material.AIR);
			world.getBlockAt(origin.x, origin.y, i).setType(Material.AIR);
		}
		
		// y axis
		for(int i = 2; i < 256; i++) {
			world.getBlockAt(origin.x, i, origin.z).setType(Material.AIR);
		}
	}

	public String getColorName(Color color) {
		switch(color) {
		case RED: return "red";
		case ORANGE: return "orange";
		case YELLOW: return "yellow";
		case GREEN: return "green";
		case AQUA: return "aqua";
		case BLUE: return "blue";
		case DARK_BLUE: return "dark blue";
		case PURPLE: return "purple";
		case MAGENTA: return "magenta";
		default: return null;
		}
	}
	
	public String getColorPrefix(Color color) {
		switch(color) {
		case RED: return "§4";
		case ORANGE: return "§6";
		case YELLOW: return "§e";
		case GREEN: return "§a";
		case AQUA: return "§b";
		case BLUE: return "§9";
		case DARK_BLUE: return "§1";
		case PURPLE: return "§d";
		case MAGENTA: return "§5";
		default: return null;
		}
	}
	
	public Material getColorConcrete(Color color) {
		switch(color) {
		case RED: return Material.RED_CONCRETE;
		case ORANGE: return Material.ORANGE_CONCRETE;
		case YELLOW: return Material.YELLOW_CONCRETE;
		case GREEN: return Material.LIME_CONCRETE;
		case AQUA: return Material.CYAN_CONCRETE;
		case BLUE: return Material.LIGHT_BLUE_CONCRETE;
		case DARK_BLUE: return Material.BLUE_CONCRETE;
		case PURPLE: return Material.PURPLE_CONCRETE;
		case MAGENTA: return Material.MAGENTA_CONCRETE;
		default: return null;
		}
	}
	
}
