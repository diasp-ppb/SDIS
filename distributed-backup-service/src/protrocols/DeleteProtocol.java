package protrocols;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.util.EnumMap;

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
		String chunksDir = peer.getFs().getChunkDir() + "/" + msg.getFileId();
		
		if(!peer.getFs().directoryExist(chunksDir)) {
			return;
		}
		
		File folder = new File(chunksDir);
		
		for(File file: folder.listFiles()) {
			peer.getDB().removeChunk(file.getName());
	        file.delete();
		}
		if(!folder.delete()) {
			System.err.println("Failed do DELETE all info");
		}
	}

	@Override
	public void run() {
		handlePacket();
	}
}

