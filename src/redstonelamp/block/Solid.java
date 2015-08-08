package redstonelamp.block;

public class Solid extends Block {
	
	 public Solid(int id, short metadata, int count) {
        super(id, metadata, count);
    }
	
	public final boolean isSolid(){
		return true;
	}
	
}