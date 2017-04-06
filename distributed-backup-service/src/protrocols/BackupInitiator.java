package protrocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;

import chunk.Chunk;
import filesystem.FileId;
import filesystem.Metadata;
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
				FileId info = new FileId(load, replicationDegree);
				String fileid = new String(info.getFileId());
				
				ArrayList<Chunk> splitted = peer.getFs().splitFile(load, info.getFileId(), replicationDegree);
				
				info.setChunkNo(splitted.size());

				EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
				messageHeader.put(Field.MESSAGE_TYPE, "PUTCHUNK");
				messageHeader.put(Field.VERSION, peer.getProtocolVersion());
				messageHeader.put(Field.SENDER_ID, peer.getId());
				messageHeader.put(Field.FILE_ID, fileid);
				messageHeader.put(Field.REPLICATION_DEGREE, Integer.toString(replicationDegree));

				for(int i = 0; i < splitted.size(); i++ ) {
					messageHeader.put(Field.CHUNK_NO, Integer.toString(i));
					Message putchunk = new Message(messageHeader, splitted.get(i).getFileData());

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
		String chunkKey = putchunk.getFileId() + putchunk.getFileId();

		peer.getDB().saveChunkInfo(chunkKey, new Metadata(0, putchunk.getReplicationDeg(), putchunk.getData().length));

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
