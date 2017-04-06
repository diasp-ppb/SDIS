package filesystem;

public class Metadata {
	
	private int currentReplication;
	private int minReplication;
	private int chunkSize;
	
	public Metadata( int currentReplication, int minReplication, int chunkSize) {
		this.currentReplication = currentReplication;
		this.minReplication= minReplication;
		this.chunkSize = chunkSize;
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

	public int getChunkSize () {
		return chunkSize;
	}
}
