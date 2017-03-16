package peer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import channels.RMIservice;

public class Peer implements RMIservice {
	
	private static String RMIobjName;
	
	public static void main( String [] args){
		
		
		/*if(!validArgs(args)) {
			System.out.println("Invalid Arguments");
			return;
		}*/
		
		startRMIservice();
	}
	
	
	public static boolean validArgs(String [] args) {
		
		// args_order -> MC MDB MDR PROTOCOL_VERSION SERVER_ID SERVICE_ACCESS_POINT
		//                                                     ^-> ver section 5.2 remote obj name?
		if(args.length  != 6)
		{
			System.out.println("");
		}
		return false; //TODO
	}
	
	
	public static void startRMIservice() {
		try {
			Peer peer = new Peer();
			
			RMIservice stub = (RMIservice) UnicastRemoteObject.exportObject(peer,0);
			
			Registry registry = LocateRegistry.getRegistry(8787);
			
			registry.bind("HelloServer1", stub);
			
		} catch (Exception e) {
        e.printStackTrace();
		}
	}


	@Override
	public String sayHello() throws RemoteException {
		return  "Hello, world!";
	}


	@Override
	public void backup() throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void restore() throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delete() throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reclaim() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
