package protrocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;

import chunk.Chunk;
import filesystem.FileId;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class BackupInitiator implements Runnable {
	private Peer peer;
	private String filePath;
	private int replicationDegree;
	
	public BackupInitiator(Peer peer, String filePath, int replicationdegree) {
		this.peer = peer;
		this.filePath = filePath;
		this.replicationDegree = replicationdegree;
	}

	@Override
	public void run() {
		if(peer.getFs().fileExist(filePath)){
			try {
				File load = new File(filePath);
				FileId  info = new FileId(load);
				// inseri lo na DB
				
				ArrayList<Chunk> splited = peer.getFs().splitFile(load, info.hash(), replicationDegree);
				// enviar chunks
				// confirmar replication degree?? os stores? 
				
				
				EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
				messageHeader.put(Field.MESSAGE_TYPE, "PUTCHUNK");
				messageHeader.put(Field.VERSION, peer.getProtocolVersion());
				messageHeader.put(Field.SENDER_ID, peer.getId());
				messageHeader.put(Field.FILE_ID, new String(info.hash()));
				
				
				for(int i = 0; i < splited.size(); i++ ){
					messageHeader.put(Field.CHUNK_NO, Integer.toString(splited.get(i).getChunkNo()));
					Message putchunk = new Message(messageHeader, splited.get(i).getFileData());
					peer.getBackupChannel().sendMessage(putchunk);
					
					System.out.println(putchunk.toString());
				}	
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("BackupInitiator: unable to load file");
			}
		
		}
		else{
			System.out.println("Backup Initiator: file doesn't exists ");
		}
		
	}

}