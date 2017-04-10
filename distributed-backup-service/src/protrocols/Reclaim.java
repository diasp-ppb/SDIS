package protrocols;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import filesystem.ChunkData;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class Reclaim implements Runnable {
	Peer peer;
	long maxSize;
	
	public Reclaim(Peer peer, long l) {
		this.peer = peer;
		maxSize = l;
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
	
	public void removeChunks(long sizeToRemove) {
		long remainingDifference = sizeToRemove;
		ArrayList<String> toRemove = new ArrayList<String>();
		
		HashMap<String, ChunkData> chunks = peer.getDB().getStoredChunks();
		
		// Removing Chunks with perceived replication degree > desired replication degree
		for (ChunkData chunk : chunks.values()) {
			if (chunk.getCurrentReplication() > chunk.getMinReplication()) {
				toRemove.add(chunk.getChunkKey());
				remainingDifference -= chunk.getChunkSize();
			}
			
			if (remainingDifference <= 0) {
				break;
			}
		}
		
		for (String chunk : toRemove) {
			removeChunk(chunk);
		}

		// If still needed, remove chunks with the largest perceived replication degree
		if (remainingDifference > 0) {
			ArrayList<ChunkData> orderedChunks = peer.getDB().getChunksOrderedByReplication();
			
			for (int i = 0; i < orderedChunks.size() && remainingDifference > 0; i++) {
				toRemove.add(orderedChunks.get(i).getChunkKey());
				remainingDifference -= orderedChunks.get(i).getChunkSize();
			}

			for (String chunk : toRemove) {
				removeChunk(chunk);
			}
		}	
	}
	
	private void removeChunk(String toRemove) {
		ChunkData chunk = peer.getDB().getChunkInfo(toRemove);
		String fileId = chunk.getFileId();
		int chunkNo = chunk.getChunkNo();

		Message removed = buildRemovedMessage(fileId, chunkNo);
		peer.getDB().removeChunk(toRemove);
		peer.getDisk().releaseSpace(chunk.getChunkSize());
		peer.getControlChannel().sendMessage(removed);
	}
	
	@Override
	public void run() {
		if (!peer.getDisk().resizeDisk(maxSize)) {
			long sizeToRemove = peer.getDisk().getCurrSize() - maxSize;
			removeChunks(sizeToRemove);
			peer.getDisk().resizeDisk(maxSize);
		}
	}
}
