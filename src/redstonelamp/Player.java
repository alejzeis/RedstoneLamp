package redstonelamp;

public class Player {
	public void sendMessage(String message) {
		//TODO: Send message to player
	}
	
	public String getName() {
		return null;
	}
	
	public void setHealth(int health){
		if(health > this.getMaxHealth()){
			return false;
		}
		if(health =< 0){
			this.kill();
			return false;
		}
		//Send player health packet
		//TODO
	}
	
	public int getMaxHealth(){
		return this.maxhealth;
	}
	
	public void kill(){
		//Send player kill packet
		this.sendMessage("You died.");
	}
	
	public int getHealth(){
		return this.health;
	}
	
	
}
