package filesystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class Database {

	private HashMap<String, ChunkData> storedChunks;
	
	private HashMap<String, FileData> sentFiles;
	private HashSet<String> sentFileId;
	
	private HashMap<String, Boolean> chunkSent;
	private HashMap<String, Boolean> putChunkSent;


	public Database() {
		storedChunks = new HashMap<String, ChunkData>();
		
		sentFiles = new HashMap<String, FileData>();
		sentFileId = new HashSet<String>();
		
		chunkSent = new HashMap<String, Boolean>();
		putChunkSent = new HashMap<String, Boolean>();
	}
	
	// Methods related to chunks stored by peer
	public synchronized HashMap<String, ChunkData> getStoredChunks() {
		notify();
		return storedChunks;
	}

	public synchronized ChunkData getChunkInfo(String key) {
		ChunkData out = storedChunks.get(key);
		notify();
		return out;
	}

	public synchronized void saveChunkInfo(String key, ChunkData info) {
		storedChunks.put(key, info);
		notify();
	}

	public synchronized boolean chunkOnDB(String key) {
		boolean  out = storedChunks.containsKey(key);
		notify();
		return out;
	}

	public synchronized boolean desiredReplication(String key) {
		if (!chunkOnDB(key)) {
			notify();
			return false;
		}

		ChunkData chunk = getChunkInfo(key);
		notify();
		return chunk.getCurrentReplication() >= chunk.getMinReplication();
	}
	
	public synchronized void updateReplicationDegree(int change, String key) {
		storedChunks.get(key).updateReplicationDegree(change);
		notify();
	}
	
	public synchronized ArrayList<ChunkData> getChunksOrderedByReplication() {
		ArrayList<ChunkData> chunkList = new ArrayList<ChunkData>();
		
		chunkList.addAll(storedChunks.values());
		chunkList.sort(Comparator.comparing(ChunkData::getChunkKey));
		notify();
		return chunkList;
	}
	
	// Returns a list of chunkId for chunks with perceived replication degree higher than desired	
	public synchronized ArrayList<ChunkData> getChunksHigherReplication() {
		ArrayList<ChunkData> chunkList = new ArrayList<ChunkData>();
		
		for (ChunkData chunk : storedChunks.values()) {
			if (chunk.getCurrentReplication() > chunk.getMinReplication()) {
				chunkList.add(chunk);
			}
		}
		
		notify();
		return chunkList;
	}
	
	// Methods related to files sent by peer
	public synchronized HashMap<String, FileData> getSentFiles() {
		HashMap<String, FileData> out = sentFiles;
		notify();
		return out;
	}

	public synchronized FileData getFileData(String filepath) {
		FileData out = sentFiles.get(filepath);
		notify();
		return out;
	}
	public synchronized void saveStoredFile(String filepath, FileData fileinfo) {
		sentFileId.add(fileinfo.getFileId());
		sentFiles.put(filepath, fileinfo);
		notify();
	}
	
	public synchronized boolean sentFileId(String fileid) {
		boolean out  = sentFileId.contains(fileid);
		notify();
		return out;
	}
	
	public synchronized void registerChunkSent(String chunkId) {
		chunkSent.put(chunkId, true);
		notify();
	}
	
	public synchronized void clearChunkSent(String chunkId) {
		chunkSent.put(chunkId, false); 
		notify();
	}
	
	public synchronized boolean chunkAlreadySent(String chunkId) {
		if (chunkSent.get(chunkId) != null){
			boolean out = chunkSent.containsKey(chunkId);
			notify();
			return out;
		}
		
		notify();
		return false;
	}
	
	public synchronized void removeChunk(String chunkId) {
		chunkSent.remove(chunkId);
		storedChunks.remove(chunkId);
		notify();
	}
	
	
	public synchronized void removeFile(String path) {
		if(path != null)
			sentFiles.remove(path);
		notify();
	}
	
	public synchronized void listenPutChunkFlag(String key) {
		putChunkSent.put(key, false);
		notify();
	}
	
	public synchronized void removePutChunkFlag(String key) {
		putChunkSent.remove(key);
		notify();
	}
	
	public synchronized void markPutChunkSent(String key) {
		if (putChunkSent.get(key) != null) {
			putChunkSent.put(key, true);
		}
		notify();
	}
	
	public synchronized boolean getPutChunkSent(String key) {
		Boolean response = putChunkSent.get(key);
		
		if (response != null) {
			notify();
			return response;
		}
		
		return false;
	}
	
	// List Chunk Information
	public synchronized String ListChunks() {
		
		String out = "";
		
		for(String key : storedChunks.keySet()) {
			ChunkData value = storedChunks.get(key);
			out +=key + " with size " + value.getChunkSize() +  " bytes and replication "+ value.getCurrentReplication();
			out +="\n";
		}
		notify();
		return out;
	}
}



