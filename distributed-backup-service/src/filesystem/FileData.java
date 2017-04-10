package filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import utils.Utils;

public class FileData {
	private String name;
	private long fileSize;
	private String owner;
	private long lastModification;
	
	private int chunkNo;
	private int replicationDegree;
	
	private String fileId;
	
	
		
	public FileData(String name, long fileSize, String owner, long lastModification, int chunkNo, int replicationDegree,
			String fileId) {
		super();
		this.name = name;
		this.fileSize = fileSize;
		this.owner = owner;
		this.lastModification = lastModification;
		this.chunkNo = chunkNo;
		this.replicationDegree = replicationDegree;
		this.fileId = fileId;
	}

	public FileData(File file, int replicationDegree) {
		this.name = file.getName();
		this.fileSize = file.length();
		
		Path path = Paths.get(file.getPath());
		
		try {
			this.owner = Files.getOwner(path, LinkOption.NOFOLLOW_LINKS).getName();
		} catch (IOException e) {
			System.out.println("FileSystem doesnt support FileOwnerAttributeView, owner set to null");
			this.owner = null;
		}
		
		this.lastModification  = file.lastModified();
		
		this.chunkNo = 0;
		
		this.replicationDegree = replicationDegree;
		
		try {
			hash();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private void hash() throws NoSuchAlgorithmException {
		fileId = DatatypeConverter.printBase64Binary(Utils.sha256(name + owner + lastModification + fileSize));
	}
	
	public String getName() {
		return name;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public void setChunkNo(int chunkNo) {
		this.chunkNo = chunkNo;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
	
	public int getReplicationDegree() {
		return replicationDegree;
	}
	
	public String getOwner() {
		return owner;
	}

	public long getLastModification() {
		return lastModification;
	}

	public String toString() {
		return name + " with id " + fileId + " and replication degree of " + replicationDegree; 
	}
	
}
