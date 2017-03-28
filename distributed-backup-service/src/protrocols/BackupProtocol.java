package protrocols;

import java.net.DatagramPacket;
import java.util.EnumMap;

import chunk.Chunk;
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
	
	private Message buildStoredMessage(Message originalMsg) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "STORED");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, originalMsg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(originalMsg.getChunkNo()));
		
		return new Message(messageHeader);
	}
	
	private void handlePacket() {
		Message msg = new Message(packet);
		
		System.out.println(msg.getType());
		if (msg.getType().equals("PUTCHUNK")) {
			saveChunk(msg);
			Message response = buildStoredMessage(msg);
			System.out.println("OUT" + response.toString());
			peer.getControlChannel().sendMessage(response);
		}
		System.out.println("active");
	}

	@Override
	public void run() {
		handlePacket();
	}
}
