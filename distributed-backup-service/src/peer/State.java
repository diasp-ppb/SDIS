package peer;

import java.util.HashMap;

import filesystem.FileId;
import filesystem.Metadata;

public class State {
	private Peer peer;
	private HashMap<String, FileId> savedFiles;
	private HashMap<String, Metadata> chunksStored;
	private int currentDiskSize;
	private int maxDiskSize;
	
	public State(Peer peer) {
		this.peer = peer;
		savedFiles = peer.getDB().getSavedFiles();
		chunksStored = peer.getDB().getChunksInfo();
		currentDiskSize = peer.getDisk().getCurrSize();
		maxDiskSize = peer.getDisk().getMaxSize();
	}
	
	public String toString() {
		String info = "Peer " + peer.getId() + " state:\n";
		
		info += "The peer has requested the backup of " + savedFiles.size() + " files:\n";
		for (FileId file : savedFiles.values()) {
			info += file.getName() + " with id " + file.getFileId() + "\n";
		}
		
		info += "\n";
		
		info += "Currently it has stored " + chunksStored.size() + " chunks.\n";
		
		info += "The disk has a max size of " + maxDiskSize + " kB of which " + currentDiskSize + " kB are in use.\n";
		
		return info;
	}
}
