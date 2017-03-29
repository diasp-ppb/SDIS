package filesystem;

public class Metadata {
	
	private int currentReplication;
	private int minReplication;
	private boolean stored;
	
	public Metadata( int currentReplication, int minReplication, boolean stored) {
		this.currentReplication = currentReplication;
		this.minReplication= minReplication;
		this.stored = stored;
	}

	public int getCurrentReplication() {
		return currentReplication;
	}

	public int getMinReplication() {
		return minReplication;
	}


	public void setCurrentReplication(int currentReplication) {
		this.currentReplication = currentReplication;
	}

	public void setMinReplication(int minReplication) {
		this.minReplication = minReplication;
	}
	
	public void setStored(boolean bool) {
		stored = bool;
	}
	
	public boolean getStored() {
		return stored;
	}
}
