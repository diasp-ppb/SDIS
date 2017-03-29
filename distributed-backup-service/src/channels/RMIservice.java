package channels;

import java.rmi.*;

public interface RMIservice extends Remote{
	void backup(String path, int replicationDregree) throws RemoteException;
	void restore(String path) throws RemoteException;
	void delete() throws RemoteException;
	void reclaim() throws RemoteException;
}
