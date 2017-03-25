package protrocols;

import chunk.Chunk;
import filesystem.FileSystem;
import utils.Message;

public class BackupProtocol {
	private FileSystem fs;
	
	public BackupProtocol() {
		this.fs = new FileSystem();
	}
	
	public void saveChunk(Message msg) {
		Chunk chunk = new Chunk(msg.getChunkNo(), msg.getFileId(), msg.getReplicationDeg(), msg.getData());
		
		fs.saveChunk(chunk);
	}
}
