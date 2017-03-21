package database;

import java.util.HashMap;

public class Database {

	HashMap<String,String> chunksInfo;
	
	public Database() {
		
		chunksInfo = new HashMap<String,String> ();
	}
	
	
	String getChunkInfo (String key){
		return chunksInfo.get(key);
	}
	
	
	void saveChunkInfo (String key, String info){
		chunksInfo.put(key,info);
	}
	
	boolean chunkOnDB(String key){
		return chunksInfo.containsKey(key);
	}
	
}
