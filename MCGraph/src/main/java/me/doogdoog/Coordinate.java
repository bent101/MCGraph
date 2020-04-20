package me.doogdoog;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class Coordinate {
	
	public int x, y, z;
	
	public Coordinate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Block getBlock() {
		return Bukkit.getWorlds().get(0).getBlockAt(x, y, z);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Coordinate) {
			Coordinate c = (Coordinate) o;
			return c.x == x && c.y == y && c.z == z;
		}
		return false;
	}
	
}
