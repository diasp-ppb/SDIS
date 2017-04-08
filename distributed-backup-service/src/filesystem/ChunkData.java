package filesystem;

public class ChunkData {
	private String chunkKey;
	private int currentReplication;
	private int minReplication;
	private int chunkSize;
	private String fileId;
	private int chunkNo;
	
	public ChunkData(String chunkKey, int currentReplication, int minReplication, int chunkSize, String fileId, int chunkNo) {
		this.chunkKey = chunkKey;
		this.currentReplication = currentReplication;
		this.minReplication = minReplication;
		this.chunkSize = chunkSize;
		this.fileId = fileId;
		this.chunkNo = chunkNo;
	}
	
	public String getChunkKey() {
		return chunkKey;
	}

	public int getCurrentReplication() {
		return currentReplication;
	}

	public int getMinReplication() {
		return minReplication;
	}
	
	public int getChunkSize() {
		return chunkSize;
	}
	
	public void updateReplicationDegree(int change) {
		this.currentReplication += change;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
	
}
