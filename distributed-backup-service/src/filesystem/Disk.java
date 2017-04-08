package filesystem;

public class Disk {

	private long currSize;
	private long maxSize;

	public Disk() {
		maxSize = 10000000;
		currSize = 0;
	}

	public Disk(int size) {
		this.maxSize = size;
		this.currSize = 0;
	}

	public boolean reserveSpace(long filesize) {
		if (enoughSpace(filesize)) {
			currSize += filesize;
			return true;
		}
		
		return false;
	}
	
	public void releaseSpace(long filesize) {
		currSize -= filesize;
	}

	public boolean enoughSpace(long filesize) {
		return currSize + filesize <= maxSize;
	}

	public boolean resizeDisk(long newSize) {
		System.out.println(newSize);
		System.out.println(currSize);
		if (newSize < currSize) {
			return false;
		}

		maxSize = newSize;
		System.out.println(maxSize);
		return true;
	}

	public long getCurrSize() {
		return currSize;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public String toString() {
		return "The disk has a max size of " + maxSize + " kB of which " + currSize/1000 + " kB are in use.\n";
	}
}
