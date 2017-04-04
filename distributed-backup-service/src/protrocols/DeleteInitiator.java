package protrocols;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;

import chunk.Chunk;
import filesystem.Database;
import filesystem.FileId;
import filesystem.Metadata;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class DeleteInitiator implements Runnable {
	private Peer peer;
	private String filePath;

	public DeleteInitiator(Peer peer, String filePath) {
		this.peer = peer;
		this.filePath = filePath;
	}
	
	
	private Message buildDeleteMessage( String fileId) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "DELETE");
		messageHeader.put(Field.VERSION, peer.getProtocolVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, fileId);		
		
		return new Message(messageHeader);
	}
	
	
	@Override
	public void run() {
		Database DB = peer.getDB();
		
		FileId fileInfo = DB.getFileId(filePath);

		if(fileInfo == null) return;
		
		String fileId =new String (fileInfo.getFileId());
		
		Message delete = buildDeleteMessage(fileId);
		
		this.peer.getControlChannel().sendMessage(delete);
				
		
		DB.removeFile(null, filePath);
		
		System.out.println("Ended");
		
		
	}
}
