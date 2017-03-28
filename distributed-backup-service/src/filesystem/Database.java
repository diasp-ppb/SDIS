package filesystem;

import java.util.HashMap;

public class Database {

	HashMap<String,Metadata> chunksInfo;
	HashMap<String,FileId>   fileInfo; 
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
	
	
	FileId  getFileInfo(String key){
		return fileInfo.get(key);
	}
	
	void saveFileInfo (String key, FileId info){
		fileInfo.put(key, info);
	}
	
	boolean fileOnDB(String key){
		return fileInfo.containsKey(key);
	}
}



