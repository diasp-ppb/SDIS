package chunk;


//TODO COMO IDENTIFICAR OS PACOTES
public class Chunck {
		
	private final static int MAX_LENGHT = 64000; // 64Kbytes
	
	private  int replicationDegree;
	
	private byte[] fileData;
	
	private ChunckID id;
	
	public Chunck (int replicationDegree, byte[] data){
		
		this.replicationDegree = replicationDegree;
		this.fileData = data;
		this.id = new ChunckID(); //TODO 
	}
	
	
	
	public byte[] getFileData(){
		return fileData;
	}
	
	public int getReplicationDegree(){
		return replicationDegree;
	}
	
	public ChunckID getID(){
		return id;
	}
}
