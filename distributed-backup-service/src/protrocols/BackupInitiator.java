package protrocols;

import peer.Peer;

public class BackupInitiator implements Runnable {
	private Peer peer;
	
	public BackupInitiator(Peer peer) {
		this.peer = peer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
}