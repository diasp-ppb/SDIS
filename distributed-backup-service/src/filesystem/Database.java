package filesystem;

import java.util.HashMap;

public class Database {

	private HashMap<String, Metadata> chunksInfo;
	private HashMap<String, FileId> savedFiles;
	private HashMap<String, Boolean> chunkSended;

	public Database() {
		chunksInfo = new HashMap<String, Metadata>();
		savedFiles = new HashMap<String, FileId>();
		chunkSended = new HashMap<String, Boolean> ();
	}

	public Metadata getChunkInfo(String key) {
		return chunksInfo.get(key);
	}

	public void saveChunkInfo(String key, Metadata info) {
		chunksInfo.put(key, info);
	}

	public boolean chunkOnDB(String key) {
		return chunksInfo.containsKey(key);
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
	
	public void registerChunkSended(String chunkId) {
		chunkSended.put(chunkId, true);
	}
	
	public void clearChunkSended(String chunkId) {
		chunkSended.put(chunkId, false); 
		// TODO  OU DELETE?
	}
	
	public boolean chunkAlreadySended(String chunkId) {
		if( chunkSended.get(chunkId) != null)
			return chunkSended.containsKey(chunkId);
		return false;
	}
	
	public void removeChunk(String chunkId) {
		chunkSended.remove(chunkId);
		chunksInfo.remove(chunkId);
	}
	
}



