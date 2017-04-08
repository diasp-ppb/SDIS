package protrocols;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map.Entry;

import filesystem.FileData;
import filesystem.ChunkData;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class Reclaim implements Runnable {
	Peer peer;
	long maxSize;
	
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
	
	private ArrayList<String> chunksToRemove(long sizeToRemove) {
		long remainingDifference = sizeToRemove;
		ArrayList<String> toRemove = new ArrayList<String>();
		
		HashMap<String, ChunkData> chunks = peer.getDB().getStoredChunks();
		
		for (Entry<String, ChunkData> entry : chunks.entrySet()) {
			ChunkData chunk = entry.getValue();
			if (chunk.getCurrentReplication() > chunk.getMinReplication()) {
				toRemove.add(entry.getKey());
				remainingDifference -= chunk.getChunkSize();
			}
			
			if (remainingDifference <= 0) {
				break;
			}
		}
		
		if (remainingDifference >= 0) {
			// remove chunks with highest replication Degree TODO
		}
		
		return toRemove;		
	}
	
	private void removeChunks(ArrayList<String> toRemove) {
		for (String chunkKey : toRemove) {
			ChunkData chunk = peer.getDB().getChunkInfo(chunkKey));
			String fileId = chunk.getFileId();
			int chunkNo = chunk.getChunkNo();
			
			// Build REMOVED message (fileData.getFileId, chunkNo);
			// Remove Chunk from DB
			// Send removed message
		}	
	}
	
	@Override
	public void run() {
		if (!peer.getDisk().resizeDisk(maxSize)) {
			// Remove files until disk.currSize <= maxSize
			long sizeToRemove = peer.getDisk().getCurrSize() - maxSize;
			ArrayList<String> toRemove = chunksToRemove(sizeToRemove);
			removeChunks(toRemove);
		}
	}
}
