package protrocols;

import java.util.EnumMap;

import filesystem.FileId;
import filesystem.Metadata;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class RestoreInitiator implements Runnable {
	private Peer peer;
	private String filePath;
	private FileId fileInfo;
	private String fileId;
	
	private int numberChunks;
	private int[] chunks;
	
	private static final int MAX_TRIES = 5;
	
	public RestoreInitiator(Peer peer, String filePath) {
		this.peer = peer;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		fileInfo = peer.getDB().getFileId(filePath);
		
		if (fileInfo == null) {
			System.out.println("The request file: " + filePath + " doesn't exist in the database.");
			return;
		}
		
		fileId = new String(fileInfo.getFileId());
		
		numberChunks = fileInfo.getChunkNo();
		chunks = new int[numberChunks];
		
		for (int i = 0; i < numberChunks; i++) {
			Message getChunk = buildGetChunkMessage(i);
			sendPackage(getChunk);
		}
	}
	
	private Message buildGetChunkMessage(int chunkNo) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "GETCHUNK");
		messageHeader.put(Field.VERSION, peer.getProtocolVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, fileId);
		messageHeader.put(Field.CHUNK_NO, Integer.toString(chunkNo));
		
		return new Message(messageHeader);
	}

	void sendPackage(Message getchunk) {
		int attempts = 0;
		
		while(attempts <= MAX_TRIES) {
			peer.getControlChannel().sendMessage(getchunk);
			attempts++;
		}
	}

}
