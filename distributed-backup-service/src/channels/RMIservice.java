package channels;

import java.io.File;
import java.rmi.*;

public interface RMIservice extends Remote{
	String sayHello() throws RemoteException;
	
	
	void backup(String path,int replicationDregree) throws RemoteException;
	
	void restore() throws RemoteException;
	
	void delete() throws RemoteException;
	
	void reclaim() throws RemoteException;

}
