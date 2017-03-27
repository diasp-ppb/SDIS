package protrocols;

import java.io.File;

import peer.Peer;

public class BackupInitiator implements Runnable {
	private Peer peer;
	
	public BackupInitiator(Peer peer, String file, int replicationdegree) {
		this.peer = peer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
}