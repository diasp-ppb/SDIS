package filesystem;

import java.util.HashMap;

public class Database {

	private HashMap<String, Metadata> chunksInfo;
	private HashMap<String, FileId> savedFiles;
	private HashMap<String, Boolean> chunkSent;

	public Database() {
		chunksInfo = new HashMap<String, Metadata>();
		savedFiles = new HashMap<String, FileId>();
		chunkSent = new HashMap<String, Boolean>();
	}

	public Metadata getChunkInfo(String key) {
		return getChunksInfo().get(key);
	}

	public void saveChunkInfo(String key, Metadata info) {
		getChunksInfo().put(key, info);
	}

	public boolean chunkOnDB(String key) {
		return getChunksInfo().containsKey(key);
	}

	public FileId getFileId(String filepath) {
		return savedFiles.get(filepath);
	}
	
	public void saveStoredFile(String filepath, FileId fileinfo) {
		savedFiles.put(filepath, fileinfo);
	}

	public boolean desiredReplication(String key) {
		if (!chunkOnDB(key))
			return false;

		Metadata info = getChunkInfo(key);
		
		if (info.getCurrentReplication() >= info.getCurrentReplication())
			return true;

		return false;
	}

	public void update(int replication, String key) {
		Metadata old = getChunkInfo(key);
		saveChunkInfo(key, new Metadata(old.getCurrentReplication() + replication, old.getMinReplication(), old.getStored()));
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
		getChunksInfo().remove(chunkId);
	}

	public HashMap<String, Metadata> getChunksInfo() {
		return chunksInfo;
	}

	public HashMap<String, FileId> getSavedFiles() {
		return savedFiles;
	}
	
}



