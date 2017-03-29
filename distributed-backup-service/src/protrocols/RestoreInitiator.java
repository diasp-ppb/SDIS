package protrocols;

import java.util.EnumMap;

import filesystem.FileId;
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
			sendPackage(i);
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

	private void sendPackage(int chunkNo) {
		Message getChunk = buildGetChunkMessage(chunkNo);
		peer.getControlChannel().sendMessage(getChunk);
	}

}
