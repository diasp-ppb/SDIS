package protrocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;
import filesystem.FileData;
import filesystem.ChunkData;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class BackupInitiator implements Runnable {
	private Peer peer;
	private String filePath;
	private int replicationDegree;
	private static final int MAX_TRIES = 5;

	public BackupInitiator(Peer peer, String filePath, int replicationdegree) {
		this.peer = peer;
		this.filePath = filePath;
		this.replicationDegree = replicationdegree;
	}

	@Override
	public void run() {
		if (peer.getFs().fileExist(filePath)) {
			try {
				File load = new File(filePath);
				FileData info = new FileData(load, replicationDegree);
				String fileid = info.getFileId();
				
				
				ArrayList<byte[]> splitted = peer.getFs().splitFile(load);
				
				info.setChunkNo(splitted.size());
					
				
				System.out.println("SPITTED" + splitted.size());
				
				EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
				messageHeader.put(Field.MESSAGE_TYPE, "PUTCHUNK");
				messageHeader.put(Field.VERSION, peer.getProtocolVersion());
				messageHeader.put(Field.SENDER_ID, peer.getId());
				messageHeader.put(Field.FILE_ID, fileid);
				messageHeader.put(Field.REPLICATION_DEGREE, Integer.toString(replicationDegree));
				
				System.out.println("Splited size " + splitted.size());
				for(int i = 0; i < splitted.size(); i++ ) {
					messageHeader.put(Field.CHUNK_NO, Integer.toString(i));
					Message putchunk = new Message(messageHeader, splitted.get(i));

					sendPackage(putchunk);

					System.out.println(putchunk.toString());
				}
				
				peer.getDB().saveStoredFile(filePath, info);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("BackupInitiator: unable to load file");
			}

		}
		else {
			System.out.println("Backup Initiator: file doesn't exist");
		}

	}


	private void sendPackage(Message putchunk) {
		int attempts = 0;
		String chunkKey = putchunk.getFileId() + putchunk.getChunkNo();
		
		System.out.println(putchunk.getFileId());
		
		
		peer.getDB().saveChunkInfo(chunkKey, new ChunkData(0, putchunk.getReplicationDeg(), putchunk.getData().length, putchunk.getFileId(), putchunk.getChunkNo()));

		while (attempts <= MAX_TRIES) {
			peer.getBackupChannel().sendMessage(putchunk);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (peer.getDB().desiredReplication(chunkKey)) {
				return;
			}

			attempts++;
		}
	}

}
