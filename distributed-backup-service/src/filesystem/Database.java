package filesystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Database {

	private HashMap<String, ChunkData> storedChunks;
	private HashMap<String, FileData> sentFiles;
	private HashMap<String, Boolean> chunkSent;
	private HashMap<String, Integer>  fileSystemId;

	private static int UNIQUE_ID = 0;

	public Database() {
		storedChunks = new HashMap<String, ChunkData>();
		sentFiles = new HashMap<String, FileData>();
		chunkSent = new HashMap<String, Boolean>();
		fileSystemId = new HashMap<String, Integer>();
	}
	
	// Methods related to chunks stored by peer
	public HashMap<String, ChunkData> getStoredChunks() {
		return storedChunks;
	}

	public ChunkData getChunkInfo(String key) {
		return storedChunks.get(key);
	}

	public void saveChunkInfo(String key, ChunkData info) {
		storedChunks.put(key, info);
	}

	public boolean chunkOnDB(String key) {
		return storedChunks.containsKey(key);
	}

	public boolean desiredReplication(String key) {
		if (!chunkOnDB(key)) {
			return false;
		}

		ChunkData chunk = getChunkInfo(key);
		
		return chunk.getCurrentReplication() >= chunk.getMinReplication();
	}
	
	public void updateReplicationDegree(int change, String key) {
		getChunkInfo(key).updateReplicationDegree(change);
	}
	
	public ArrayList<ChunkData> getChunksOrderedByReplication() {
		ArrayList<ChunkData> chunkList = new ArrayList<ChunkData>();
		
		chunkList.addAll(storedChunks.values());
		chunkList.sort(Comparator.comparing(ChunkData::getChunkKey));
		
		for (ChunkData chunk : chunkList) {
			System.out.println(chunk.getCurrentReplication());
		}
		
		return chunkList;
	}
	
	// Methods related to files sent by peer
	public HashMap<String, FileData> getSentFiles() {
		return sentFiles;
	}

	public FileData getFileData(String filepath) {
		return sentFiles.get(filepath);
	}
	public void saveStoredFile(String filepath, FileData fileinfo) {
		sentFiles.put(filepath, fileinfo);
	}
	
	
	public void registerChunkSent(String chunkId) {
		chunkSent.put(chunkId, true);
	}
	
	public void clearChunkSent(String chunkId) {
		chunkSent.put(chunkId, false); 
		// TODO  OU DELETE?
	}
	
	public boolean chunkAlreadySent(String chunkId) {
		if (chunkSent.get(chunkId) != null)
			return chunkSent.containsKey(chunkId);
		return false;
	}
	
	public void removeChunk(String chunkId) {
		chunkSent.remove(chunkId);
		storedChunks.remove(chunkId);
	}
	
	public void addFileStored(String fileId){
		fileSystemId.put(fileId,++UNIQUE_ID);
	}
	
	public boolean FileStored(String fileId) {
		
		return fileSystemId.containsKey(fileId);
	}
	
	public int getFileStorageId(String  fileId) {
		
		return fileSystemId.get(fileId);
	}
	
	public void removeFile(String fileId, String path) {
		if(fileId != null)
		fileSystemId.remove(fileId);
		if(path != null)
			sentFiles.remove(path);
	}
	
	// List Chunk Information
	public String ListChunks() {
		
		String out = "";
		
		for(String key : storedChunks.keySet()) {
			ChunkData value = storedChunks.get(key);
			out +=key + " with size " + value.getChunkSize() +  " bytes and replication "+ value.getCurrentReplication();
			out +="\n";
		}
		
		return out;
	}
}



