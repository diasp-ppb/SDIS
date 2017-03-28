package protrocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import chunk.Chunk;
import filesystem.FileId;
import peer.Peer;

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
				//criar fileInfo
				FileId  info = new FileId(load);
				// inseri lo na DB
				ArrayList<Chunk> splited = peer.getFs().splitFile(filePath,info.hash(), replicationDegree);
				// enviar chunks
				// confirmar replication degree?? os stores? 
				
			} catch (IOException | NoSuchAlgorithmException e) {
				System.out.println("BackupInitiator: unable to load file");
			}
		
		}
		
	}

}