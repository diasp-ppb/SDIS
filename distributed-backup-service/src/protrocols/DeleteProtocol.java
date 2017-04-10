package protrocols;

import java.io.File;
import filesystem.Database;
import peer.Peer;
import utils.Message;

public class DeleteProtocol implements Runnable {
	private Peer peer;
	private Message msg;
	
	public DeleteProtocol(Peer peer, Message msg) {
		this.msg = msg;
		this.peer = peer;
	}
	
	private void handlePacket() {
		System.out.println("delete protocol");
		
		peer.getDB().addDeleteMessage(msg);
		
		String storageId = msg.getFileId();
		
		String chunksDir = peer.getFs().getChunkDir() + storageId;
		
		if(!peer.getFs().directoryExist(chunksDir)) {
			return;
		}
		
		File folder = new File(chunksDir);
		
		System.out.println(folder.listFiles().length);
		for(File file: folder.listFiles()) {
			peer.getDB().removeChunk(msg.getFileId()+file.getName());
			peer.getDisk().releaseSpace(file.length());
	        file.delete();
		}
		
		
		folder.delete();
		
		peer.saveDB(); //DB
		
		System.out.println("DELETED");
	}

	@Override
	public void run() {
		handlePacket();
	}
}

