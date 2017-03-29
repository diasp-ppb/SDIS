package protrocols;

import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.util.EnumMap;

import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class RestoreProtocol implements Runnable {
	private Peer peer;
	private Message msg;
	
	public RestoreProtocol(Peer peer, Message msg) {
		this.msg = msg;
		this.peer = peer;
	}
	
	private byte[] loadChunk() {
		String chunkId = msg.getFileId() + msg.getChunkNo();
		byte[] chunk;
		
		try {
			chunk = peer.getFs().loadChunk(chunkId);
		} catch (FileNotFoundException e) {
			chunk = null;
		}
		
		return chunk;
	}
	
	private Message buildChunkMessage(byte[] chunk) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "CHUNK");
		messageHeader.put(Field.VERSION, msg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, msg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(msg.getChunkNo()));
		
		return new Message(messageHeader, chunk);
	}
	
	private void handlePacket() {
		byte[] chunk = loadChunk();
		
		if (chunk == null) {
			return;
		}

		Message chunkMessage = buildChunkMessage(chunk);
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void run() {
		handlePacket();
	}
}
