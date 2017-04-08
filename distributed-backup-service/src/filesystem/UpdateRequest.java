package filesystem;

import peer.Peer;
import utils.Message;

public class UpdateRequest implements Runnable {
	private Peer peer;
	private Message msg;

	public UpdateRequest(Peer peer, Message msg) {
		this.peer = peer;
		this.msg = msg;
	}

	private void storedHandler() {
		Database db = peer.getDB();
		
		String chunkKey = msg.getFileId() + msg.getChunkNo();
		
		System.out.println("STORED UPDATE");
		
		if (db.chunkOnDB(chunkKey)) {
			
			db.updateReplicationDegree(1, chunkKey);
		} 
	}
	
	private void chunkHandler() {
		String chunkKey = msg.getFileId() + msg.getChunkNo();
		peer.getDB().chunkAlreadySent(chunkKey);
	}
	
	private void handleMessage() {
		switch (msg.getType()) {
		case "STORED":
			storedHandler();
			break;
		case "CHUNK":
			chunkHandler();
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {
		handleMessage();
	}
}
