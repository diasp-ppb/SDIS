package protrocols;

import java.net.DatagramPacket;
import java.util.EnumMap;
import filesystem.Database;
import filesystem.ChunkData;
import peer.Peer;
import utils.Message;
import utils.Message.Field;

public class BackupProtocol implements Runnable {
	private Peer peer;
	private DatagramPacket packet;
	
	public BackupProtocol(Peer peer, DatagramPacket packet) {
		this.packet = packet;
		this.peer = peer;
	}
	
	private void saveChunk(Message msg) {
		
		int storageId  = peer.getDB().getFileStorageId(msg.getFileId());
		
		peer.getFs().saveChunk(storageId,msg.getChunkNo(),msg.getData());
	}
	
	private Message buildStoredMessage(Message originalMsg) {
		EnumMap<Field, String> messageHeader = new EnumMap<Field, String>(Field.class);
		
		messageHeader.put(Field.MESSAGE_TYPE, "STORED");
		messageHeader.put(Field.VERSION, originalMsg.getVersion());
		messageHeader.put(Field.SENDER_ID, peer.getId());
		messageHeader.put(Field.FILE_ID, originalMsg.getFileId());
		messageHeader.put(Field.CHUNK_NO, Integer.toString(originalMsg.getChunkNo()));
		
		return new Message(messageHeader);
	}
	
	private void handlePacket() {
		Message msg = new Message(packet);
		System.out.println("Received PUTCHUNK with size " + msg.getMsg().length + " and body size:" + msg.getData().length);
		System.out.println("ChunkNo:" + msg.getChunkNo());
		if (msg.getType().equals("PUTCHUNK")) {
			if (updateDB(msg)) {
				
				saveChunk(msg);
				Message response = buildStoredMessage(msg);
				System.out.println("OUT " + response.toString());
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				peer.getControlChannel().sendMessage(response);
			}
		}	
	}
	
	
	private boolean updateDB(Message msg) {
		
		String chunkKey = msg.getFileId()+msg.getChunkNo();
		
		Database db = peer.getDB();
		
		
		//TODO remover 2º condiçao 
		if(db.chunkOnDB(chunkKey) && db.getChunkInfo(chunkKey).getCurrentReplication() != 0) {
				return false;
		}
		else {
			
			if(!peer.getDisk().reserveSpace(msg.getData().length)) {
				   System.out.println("Not enough space in disk");
				   return false;
				}
			
			db.saveChunkInfo(chunkKey,new ChunkData(1, msg.getReplicationDeg(), msg.getData().length, msg.getFileId(), msg.getChunkNo()));
		}
		
		if(! db.FileStored(msg.getFileId())) {
			 db.addFileStored(msg.getFileId());
		}
		
		return true;
	}
	
	

	@Override
	public void run() {
		handlePacket();
	}
}
