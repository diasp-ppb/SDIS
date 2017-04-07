package filesystem;

public class Disk {

	private long currSize;
	private long maxSize;

	public Disk() {
		maxSize = Integer.MAX_VALUE;
		currSize = 0;
	}

	public Disk(int size) {
		this.maxSize = size;
		this.currSize = 0;
	}

	public boolean reserveSpace(long filesize) {
		if(enoughSpace(filesize)) {
			currSize+=filesize;
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
		if (newSize < currSize) {
			return false;
		}

		maxSize = newSize;
		return true;
	}

	public boolean increaseSize(long increment) {
		if (enoughSpace(increment)) {
			currSize += increment;
			return true;
		}

		return false;
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
