package chunk;


//TODO COMO IDENTIFICAR OS PACOTES
public class Chunk {
		
	private final static int MAX_LENGTH = 64000; // 64Kbytes
	
	private int replicationDegree;
	
	private byte[] fileData;
	
	private int chunkNo;
	private String fileId;
	
	public Chunk (int chunkNo, String fileId, int replicationDegree, byte[] data) {
		
		this.replicationDegree = replicationDegree;
		this.fileData = data;
		this.chunkNo = chunkNo;
		this.fileId = fileId;
	}
	
	public byte[] getFileData() {
		return this.fileData;
	}
	
	public int getReplicationDegree() {
		return this.replicationDegree;
	}
	
	public int getChunkNo() {
		return this.chunkNo;
	}
	
	public String getFileId() {
		return this.fileId;
	}
}
