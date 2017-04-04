package protrocols;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.util.EnumMap;

import filesystem.Database;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class DeleteProtocol implements Runnable {
	private Peer peer;
	private Message msg;
	
	public DeleteProtocol(Peer peer, Message msg) {
		this.msg = msg;
		this.peer = peer;
	}
	
	private void handlePacket() {
		
		System.out.println("delete protocol");
		Database DB = peer.getDB();
		
		int storageId = DB.getFileStorageId(msg.getFileId());
		String chunksDir = peer.getFs().getChunkDir() + storageId;
		
		if(!peer.getFs().directoryExist(chunksDir)) {
			return;
		}
		
		File folder = new File(chunksDir);
		
		for(File file: folder.listFiles()) {
			DB.removeChunk(msg.getFileId()+file.getName());
	        file.delete();
		}
		
		DB.removeFile(msg.getFileId(), null);
		
		if(!folder.delete()) {
			System.err.println("Failed do DELETE all info");
		}
		
		System.out.println("DELETED");
	}

	@Override
	public void run() {
		handlePacket();
	}
}

