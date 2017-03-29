package protrocols;

import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.util.EnumMap;

import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class RestoreProtocol implements Runnable {
	private Peer peer;
	private DatagramPacket packet;
	
	public RestoreProtocol(Peer peer, DatagramPacket packet) {
		this.packet = packet;
		this.peer = peer;
	}
	
	private byte[] loadChunk(Message msg) {
		String chunkId = msg.getFileId() + msg.getFileId();
		byte[] chunk;
		
		try {
			chunk = peer.getFs().loadChunk(chunkId);
		} catch (FileNotFoundException e) {
			chunk = null;
		}
		
		return chunk;
	}
	
	private Message buildChunkMessage(Message originalMsg, byte[] chunk) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "CHUNK");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, originalMsg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(originalMsg.getChunkNo()));
		
		return new Message(messageHeader, chunk);
	}
	
	private void handlePacket() {
		Message msg = new Message(packet);
		byte[] chunk = loadChunk(msg);
		
		Message chunkMessage = buildChunkMessage(msg, chunk);
	}

	@Override
	public void run() {
		handlePacket();
	}
}
