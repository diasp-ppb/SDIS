package filesystem;

import java.util.HashMap;

public class Database {

	HashMap<String,Metadata> chunksInfo;
	HashMap<String,FileId>   fileInfo; 
	public Database() {
		
		chunksInfo = new HashMap<String,Metadata> ();
	}
	
	
	public Metadata getChunkInfo (String key){
		return chunksInfo.get(key);
	}
	
	
	public void saveChunkInfo (String key, Metadata info){
		chunksInfo.put(key,info);
	}
	
	public boolean chunkOnDB(String key){
		return chunksInfo.containsKey(key);
	}
	
	
	public FileId  getFileInfo(String key){
		return fileInfo.get(key);
	}
	
	public void saveFileInfo (String key, FileId info){
		fileInfo.put(key, info);
	}
	
	public boolean fileOnDB(String key){
		return fileInfo.containsKey(key);
	}
	
	public boolean desiredReplication(String key){
		if(!chunkOnDB(key)) 
			return false;
		
		Metadata info = getChunkInfo(key);
		if(info.getCurrentReplication() >= info.getCurrentReplication())
			return true;
		
		return false;
	}
	
	public void update(int replication, String key ) {
		Metadata old = getChunkInfo(key);
		saveChunkInfo(key, new Metadata(old.getCurrentReplication() + replication,old.getMinReplication(), old.getStored()));
	}
}



