package protrocols;

import peer.Peer;
import utils.Message;

public class DeleteEnhacement implements Runnable {
	
	Peer peer;
	
	public DeleteEnhacement(Peer peer) {
		this.peer = peer;
	}	

	@Override
	public void run() {
		for (Message delete : peer.getDB().getDeleteMessages()) {
			// Send DELETE thrice - 1 second interval
			for (int j = 0; j < 3; j++) {
				this.peer.getControlChannel().sendMessage(delete);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
