package peer;

import java.rmi.*;

public interface RMIservice extends Remote{
	void backup(String path, int replicationDregree) throws RemoteException;
	void restore(String path) throws RemoteException;
	void delete(String path) throws RemoteException;
	void reclaim(int maxSize) throws RemoteException;
	String state() throws RemoteException;
}
