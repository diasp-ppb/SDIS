package filesystem;

public class Disk {
	
	private int currSize;
	private int maxSize;
	
	public Disk() {
		maxSize = Integer.MAX_VALUE;
		currSize = 0;
	}
	
	public Disk(int size) {
		this.maxSize = size;
		this.currSize = 0;
	}
	
	public boolean enoughSpace(int filesize) {
		return currSize + filesize >= maxSize;
	}
	
	public boolean resizeDisk(int newSize) {
		if (newSize < currSize) {
			return false;
		}
		
		maxSize = newSize;
		return true;
	}
	
	public boolean increaseSize(int increment) {
		if (enoughSpace(increment)) {
			currSize += increment;
			return true;
		}
		
		return false;
	}
}
