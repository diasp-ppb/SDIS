package protrocols;

import java.net.DatagramPacket;
import java.util.EnumMap;

import chunk.Chunk;
import filesystem.FileSystem;
import utils.Message;
import utils.Message.Field;

public class BackupProtocol implements Runnable {
	private FileSystem fs;
	private DatagramPacket packet;
	
	public BackupProtocol(DatagramPacket packet) {	
		this.packet = packet;
		fs = new FileSystem();
	}
	
	private void saveChunk(Message msg) {
		Chunk chunk = new Chunk(msg.getChunkNo(), msg.getFileId(), msg.getReplicationDeg(), msg.getData());
		
		fs.saveChunk(chunk);
	}
	
	private void sendStoredMessage(Message originalMsg) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "STORED");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		// messageHeader.put(Field.SENDER_ID, TODO GET SENDER ID);
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
