

package protrocols;

import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.util.EnumMap;

import chunk.Chunk;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class  RestoreProtocol  implements Runnable {
	private Peer peer;
	private Message msg;
	
	public RestoreProtocol(Peer peer, Message msg) {	
		this.msg = msg;
	}
	
	private void saveChunk(Message msg) {
		Chunk chunk = new Chunk(msg.getChunkNo(), msg.getFileId(), msg.getReplicationDeg(), msg.getData());
		
		peer.getFs().saveChunk(chunk);
	}
	
	private Message buildStoredMessage(Message originalMsg) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "CHUNK");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, originalMsg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(originalMsg.getChunkNo()));
		
		return new Message(messageHeader);
	}
	
	private void handlePacket() {
		
		//TODO CHUNK ID
		try {
			byte[] chunkdata = peer.getFs().loadChunk("");
			Message response = buildStoredMessage(msg);
			peer.getControlChannel().sendMessage(response);
		} catch (FileNotFoundException e) {
			return;
		}/* TODO */
	}

	@Override
	public void run() {
		handlePacket();
	}
}
