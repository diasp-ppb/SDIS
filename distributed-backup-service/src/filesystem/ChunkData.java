package filesystem;

public class ChunkData {
	
	private int currentReplication;
	private int minReplication;
	private int chunkSize;
	private String fileId;
	private int chunkNo;
	
	public ChunkData(int currentReplication, int minReplication, int chunkSize, String fileId, int chunkNo) {
		this.currentReplication = currentReplication;
		this.minReplication = minReplication;
		this.chunkSize = chunkSize;
		this.fileId = fileId;
		this.chunkNo = chunkNo;
	}

	public int getCurrentReplication() {
		return currentReplication;
	}

	public int getMinReplication() {
		return minReplication;
	}
	
	public int getChunkSize () {
		return chunkSize;
	}
	
	public void setCurrentReplication(int currentReplication) {
		this.currentReplication = currentReplication;
	}

	public void setMinReplication(int minReplication) {
		this.minReplication = minReplication;
	}
	
	public void updateReplicationDegree(int change) {
		this.currentReplication += change;
	}
	
}
