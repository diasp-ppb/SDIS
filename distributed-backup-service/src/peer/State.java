package peer;

import java.util.HashMap;

import filesystem.FileId;
import filesystem.Metadata;

public class State {
	private Peer peer;
	private HashMap<String, FileId> savedFiles;
	private HashMap<String, Metadata> chunksStored;
	
	public State(Peer peer) {
		this.peer = peer;
		savedFiles = peer.getDB().getSavedFiles();
		chunksStored = peer.getDB().getChunksInfo();
	}
	
	public String toString() {
		String info = "Peer " + peer.getId() + " state:";
		return info;
	}
}
