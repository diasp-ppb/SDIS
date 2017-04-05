package protrocols;

import java.util.EnumMap;

import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class Reclaim implements Runnable {
	Peer peer;
	int maxSize;
	
	public Reclaim(Peer peer, int maxDiskSize) {
		this.peer = peer;
		maxSize = maxDiskSize;
	}
	
	private Message buildRemovedMessage(String fileId, int chunkNo) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "REMOVED");
		messageHeader.put(Field.VERSION, peer.getProtocolVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, fileId);
		messageHeader.put(Field.CHUNK_NO, Integer.toString(chunkNo));
		
		return new Message(messageHeader);
	}

	@Override
	public void run() {
		if (!peer.getDisk().resizeDisk(maxSize)) {
			// Remove files until disk.currSize <= maxSize
		}
	}
}
