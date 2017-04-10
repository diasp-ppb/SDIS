package filesystem;

import peer.Peer;
import protrocols.BackupInitiator;
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
	
	private void storedHandlerV2() {
		Database db = peer.getDB();
		
		if(msg.getSenderId().equals(peer.getId()))
			return ;
		
		String chunkKey = msg.getFileId() + msg.getChunkNo();
				
		if (db.chunkOnDB(chunkKey)) {
			db.updateReplicationDegree(1, chunkKey);
		}
		else {
			db.saveChunkInfo(chunkKey, new ChunkData(chunkKey, 1, -1, -1, msg.getFileId(), msg.getChunkNo()));
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
				
				new Thread(new BackupInitiator(peer, peer.getDB().getChunkInfo(chunkKey))).start();
				
			}
		}
	}
	
	private void handleMessage() {
		switch (msg.getType()) {
		case "STORED":
			if(peer.getProtocolVersion().equals("1.0")) {
				storedHandler();
			}
			else if( peer.getProtocolVersion().equals("2.0")) {
				storedHandlerV2();
			}
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
