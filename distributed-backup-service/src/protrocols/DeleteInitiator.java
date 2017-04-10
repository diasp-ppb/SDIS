package protrocols;

import java.util.EnumMap;
import filesystem.Database;
import filesystem.FileData;
import peer.Peer;
import utils.Message;
import utils.Utils;
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
		FileData fileInfo = DB.getFileData(filePath);
		
		if (fileInfo == null) {
			return;
		}
		
		System.out.println("HEY");
		
		String fileId = new String (fileInfo.getFileId());
		Message delete = buildDeleteMessage(fileId);
		
		// Send DELETE thrice - 1 second interval
		for (int i = 0; i < 3; i++) {
			this.peer.getControlChannel().sendMessage(delete);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		DB.removeFile(filePath);
		
		peer.saveDB(); //DB
		
		System.out.println("Ended");
	}
}
