package protrocols;

import peer.Peer;

public class Reclaim implements Runnable {
	Peer peer;
	int maxSize;
	
	public Reclaim(Peer peer, int maxDiskSize) {
		this.peer = peer;
		maxSize = maxDiskSize;
	}

	@Override
	public void run() {
		peer.getDisk().resizeDisk(maxSize);
	}
}
