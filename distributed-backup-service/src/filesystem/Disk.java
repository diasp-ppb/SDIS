package filesystem;

public class Disk {
	
	private int size;
	private int  freeSpace;

	public Disk(int size){
		this.size = size;
		this.freeSpace = 0;
	}
	
	
	public  boolean enoughtSpace(int filesize) {
		return freeSpace >= filesize;
	}
	
	
	public  boolean resizeDisk(int newSize) {
		if(newSize <  freeSpace){
			return false;
		}
		size = newSize;
		return true;
	}
	
	public 	boolean reserveSpace(int filesize){
		if( enoughtSpace(filesize)){
			freeSpace -= filesize;
			return true;
		}
		return false;
	}
	
	
}
