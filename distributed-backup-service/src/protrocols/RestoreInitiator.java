package protrocols;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import filesystem.FileData;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class RestoreInitiator implements Runnable {
	private Peer peer;
	private String filePath;
	private FileData fileInfo;
	private String fileId;

	private int numberChunks;


	private Message[] data;


	private static final int MAX_TRIES = 5;


	public RestoreInitiator(Peer peer, String filePath) {
		this.peer = peer;
		this.filePath = filePath;
		
	}


	private Message buildGetChunkMessage(int chunkNo) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);

		messageHeader.put(Field.MESSAGE_TYPE, "GETCHUNK");
		messageHeader.put(Field.VERSION, peer.getProtocolVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, fileId);
		messageHeader.put(Field.CHUNK_NO, Integer.toString(chunkNo));
		
		System.out.println("CHUNK NO " + messageHeader.get(Field.CHUNK_NO));
		return new Message(messageHeader);
	}

	private void sendPackage(int chunkNo) {
		Message getChunk = buildGetChunkMessage(chunkNo);
		peer.getControlChannel().sendMessage(getChunk);
	}

	@Override
	public void run() {
		fileInfo = peer.getDB().getFileData(filePath);
		
		fileId = new String( fileInfo.getFileId());
		
		
		if (fileInfo == null) {
			System.out.println("The request file: " + filePath + " doesn't exist in the database.");
			return;
		}
		
		numberChunks = fileInfo.getChunkNo();
		
		data = new Message[numberChunks];
		
		//TODO
		peer.getRestoreChannel().addRestoreInitiator(new String(fileInfo.getFileId()), this);
		
		
		for (int i = 0; i < numberChunks; i++) {

			for(int attempts = 0; attempts < MAX_TRIES; attempts++) {
				if(data[i] == null){
					sendPackage(i);

					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				else break;
			}

			if(data[i] == null) {
				System.out.println("Recover fail: Not all parts are avaiable");
				return;
			}
		}		
		
		Path p = Paths.get(filePath);
		String file = p.getFileName().toString();		
		peer.getFs().restoreFile(data, file);
		

		peer.getRestoreChannel().removerestoreInitiator(fileInfo.getFileId()); //TODO
		System.out.println("Restore " + file + " completed");
	}

	public void putMessage(Message msg) {
		int index = msg.getChunkNo();
		if(index < numberChunks) {
			if(data[index] != null ) {
				return;
			}
			else {
				data[index] = msg;
			}
		}

	}
}
