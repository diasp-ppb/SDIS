package filesystem;

import peer.Peer;
import utils.Message;
import utils.Utils;

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
				
		if (db.chunkOnDB(chunkKey)) {
			
			db.updateReplicationDegree(1, chunkKey);
		} 
	}
	
	private void chunkHandler() {
		String chunkKey = msg.getFileId() + msg.getChunkNo();
		peer.getDB().chunkAlreadySent(chunkKey);
	}
	
	private void removedHandler() {
		String chunkKey = msg.getFileId() + msg.getChunkNo();
		if (peer.getDB().chunkOnDB(chunkKey)) {
			peer.getDB().updateReplicationDegree(-1, chunkKey);
			
			if (!peer.getDB().desiredReplication(chunkKey)) {
				try {
					Thread.sleep(Utils.randomNumber(0, 400));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// Initiate Chunk backup subprotocol
				// Listen for putchunk
				
			}
		}
	}
	
	private void handleMessage() {
		switch (msg.getType()) {
		case "STORED":
			storedHandler();
			break;
		case "CHUNK":
			chunkHandler();
			break;
		case "REMOVED":
			removedHandler();
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
