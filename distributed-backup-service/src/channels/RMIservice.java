package channels;

import java.rmi.*;

public interface RMIservice extends Remote{
	String sayHello() throws RemoteException;
	
	
	void backup() throws RemoteException;
	
	void restore() throws RemoteException;
	
	void delete() throws RemoteException;
	
	void reclaim() throws RemoteException;
}
