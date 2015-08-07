package redstonelamp.block;

import redstonelamp.item.Item;
import redstonelamp.metadata.Metadatable;

public class Block extends Item implements Metadatable, BlockValues {

	public Block(int id, short metadata, int count) {
		super(id, metadata, count);
	}
	
	 public boolean isBreakable(){
		 return true;
	 }
	 
	 public boolean isSolid(){
		 return true;
	 }
	 
	 public boolean canBePlaced(){
		 return true;
	 }
	 
	 public boolean canBeReplaced(){
		 return false;
	 }
	 
	 public boolean isTransparent(){
		 return false;
	 }
	 
	 public boolean canPassThrough(){
		 return false;
	 }
	 
	 public String getName(){
		 return "Unknown";
	 }
	 
	 public int getHardness(){
		 return 10;
	 }
	 
	 public int getLightLevel(){
		 return 0;
	 }
	 
}