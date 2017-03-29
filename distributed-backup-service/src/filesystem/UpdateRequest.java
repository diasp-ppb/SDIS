package filesystem;

import peer.Peer;
import utils.Message;

public class UpdateRequest implements Runnable{
	
	private Peer peer;
	private Message msg;
	public  UpdateRequest(Peer peer, Message msg){
		this.peer = peer;
		this.msg = msg;
	}
	@Override
	public void run() {
		
		Database db = peer.getDB();
		
		String chunkKey = msg.getFileId() + msg.getChunkNo();
		if(db.chunkOnDB(chunkKey)){
			db.update(1, chunkKey);
		}
		else {
			db.saveChunkInfo(chunkKey, new Metadata(1,1,false));
		}
		
	}

}