package peer;

import java.util.HashMap;

import filesystem.Database;
import filesystem.FileData;

public class State {
	private Peer peer;
	private Database DB;
	private HashMap<String, FileData> savedFiles;
	
	public State(Peer peer) {
		this.peer = peer;
		this.DB = peer.getDB();
		savedFiles = peer.getDB().getSentFiles();
		
	}
	
	public String toString() {
		String info = "Peer " + peer.getId() + " state:\n";
		
		info += "The peer has requested the backup of " + savedFiles.size() + " files:\n";
		for (FileData file : savedFiles.values()) {
			info += file.toString() + "\n";
		}
		
		info += "\n";
		
		info += "Currently it has stored " + DB.getStoredChunks().size()+ " chunks.\n";
		
		info += DB.ListChunks();
		
		info += peer.getDisk().toString();
		
		return info;
	}
}
