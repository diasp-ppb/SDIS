package protrocols;

import java.net.DatagramPacket;
import java.util.EnumMap;

import chunk.Chunk;
import filesystem.FileSystem;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class BackupProtocol implements Runnable {
	private Peer peer;
	private DatagramPacket packet;
	
	public BackupProtocol(Peer peer, DatagramPacket packet) {	
		this.packet = packet;
	}
	
	private void saveChunk(Message msg) {
		Chunk chunk = new Chunk(msg.getChunkNo(), msg.getFileId(), msg.getReplicationDeg(), msg.getData());
		
		peer.getFs().saveChunk(chunk);
	}
	
	private void sendStoredMessage(Message originalMsg) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "STORED");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, originalMsg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(originalMsg.getChunkNo()));
		
		Message msg = new Message(messageHeader);
	}
	
	private void handlePacket() {
		Message msg = new Message(packet);
		
		if (msg.getType().equals("PUTCHUNK")) {
			saveChunk(msg);
		}
	}

	@Override
	public void run() {
		handlePacket();
	}
}
