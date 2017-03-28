package filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;

import utils.Utils;

public class FileId {
	private String name;
	private long fileSize;
	private String owner;
	private long lastModification;
	
	public FileId(String name, long fileSize, String owner, long lastModification) {
		super();
		this.name = name;
		this.fileSize = fileSize;
		this.owner = owner;
		this.lastModification = lastModification;
	}
	
	public FileId(File file){
		this.name = file.getName();
		this.fileSize = file.length();
		// Determinate the owner
		Path path = Paths.get(file.getPath());
		try {
			this.owner = Files.getOwner(path, LinkOption.NOFOLLOW_LINKS).getName();
		} catch (IOException e) {
			System.out.println("FileSystem doesnt support  FileOwnerAttributeView owner set to null");
			this.owner = null;
		}
		
		this.lastModification  = file.lastModified();
		
		
        
	}
	
	public String getName() {
		return name;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	public String getOwner() {
		return owner;
	}
	public long getLastModification() {
		return lastModification;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFileSize(int newSize) {
		fileSize = newSize;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setLastModification(long lastModification) {
		this.lastModification = lastModification;
	}
	
	
	public byte[] hash() throws NoSuchAlgorithmException {
		
		return Utils.sha256(name + owner + lastModification + "" + fileSize);
	}
	
	
	
}
