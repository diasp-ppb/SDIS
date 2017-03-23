package filesystem;

import java.util.HashMap;

public class Database {

	HashMap<String,Metadata> chunksInfo;
	
	public Database() {
		
		chunksInfo = new HashMap<String,Metadata> ();
	}
	
	
	Metadata getChunkInfo (String key){
		return chunksInfo.get(key);
	}
	
	
	void saveChunkInfo (String key, Metadata info){
		chunksInfo.put(key,info);
	}
	
	boolean chunkOnDB(String key){
		return chunksInfo.containsKey(key);
	}
		
}



