package me.doogdoog;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Coordinate {
	
	public int x, y, z;
	
	public Coordinate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setToMaterial(Material material) {
		Bukkit.getWorlds().get(0).getBlockAt(x, y, z).setType(material);
	}
	
	public boolean isOnAxis() {
		int numZeroes = 0;
		if(x == 0) numZeroes++;
		if(y == 0) numZeroes++;
		if(z == 0) numZeroes++;
		
		return numZeroes > 1;
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
