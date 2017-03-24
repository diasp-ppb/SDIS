package filesystem;

public class Metadata {
	
	private int num;
	private int currentReplication;
	private int minReplication;
	private int size;
	private String filename;
	private int filesize;
	
	public Metadata(int num, int currentReplication, int minReplication, int size, String filename, int filesize) {
		this.num = num;
		this.currentReplication = currentReplication;
		this.minReplication= minReplication;
		this.size = size;
		this.filename = filename;
		this.filesize = filesize;
	}

	public int getNum() {
		return num;
	}

	public int getCurrentReplication() {
		return currentReplication;
	}

	public int getMinReplication() {
		return minReplication;
	}

	public int getSize() {
		return size;
	}

	public String getFilename() {
		return filename;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setCurrentReplication(int currentReplication) {
		this.currentReplication = currentReplication;
	}

	public void setMinReplication(int minReplication) {
		this.minReplication = minReplication;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}
}
