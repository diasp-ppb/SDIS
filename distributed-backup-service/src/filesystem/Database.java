package filesystem;

import java.util.HashMap;

public class Database {

	private HashMap<String, Metadata> chunksInfo;
	private HashMap<String, String> savedFiles;

	public Database() {
		chunksInfo = new HashMap<String, Metadata>();
		savedFiles = new HashMap<String, String>();
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

	public String getFileId(String filepath) {
		return savedFiles.get(filepath);
	}
	
	public void saveStoredFile(String filepath, String fileid) {
		savedFiles.put(filepath, fileid);
	}

	public boolean desiredReplication(String key) {
		if(!chunkOnDB(key))
			return false;

		Metadata info = getChunkInfo(key);
		
		if(info.getCurrentReplication() >= info.getCurrentReplication())
			return true;

		return false;
	}

	public void update(int replication, String key) {
		Metadata old = getChunkInfo(key);
		saveChunkInfo(key, new Metadata(old.getCurrentReplication() + replication, old.getMinReplication(), old.getStored()));
	}
}



