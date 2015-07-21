package redstonelamp.level;

import redstonelamp.math.Vector3;

public class Position extends Vector3 {
	public Level level = null;
	
	public Position() {}
	
	public Position(int x, int y, int z, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.level = level;
	}
	
	public static Position fromObject(Vector3 pos) {
		return fromObject(pos, null);
	}
	
	public static Position fromObject(Vector3 pos, Level level) {
		return new Position(pos.x, pos.y, pos.z, level);
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Position setLevel(Level level) {
		this.level = level;
		return this;
	}
	
	public boolean isValid() {
		return this.level != null;
	}
	
	public boolean getStrong() {
		return false;
	}
	
	public boolean setWeak() {
		return false;
	}
	
	public Position getSide(int side, int step){
		if(!isValid())
			return null;
		return Position.fromObject(getSide(side, step), level);
	}
	
	public Position setComponents(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public String toString() {
		return "Position(X=" + x + ", Y=" + y + ", Z=" + z + ")";
	}
}
