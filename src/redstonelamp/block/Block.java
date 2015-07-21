package redstonelamp.block;

import redstonelamp.level.Position;
import redstonelamp.metadata.Metadatable;

public class Block extends Position implements Metadatable {
	private int id;
	private int metadata;
	
	public Block(int id, int metadata) {
		this.id = id;
		this.metadata = metadata;
	}
	
	//TODO: Block methods
}
