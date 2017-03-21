package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import channels.BackupChannel;
import channels.ControlChannel;
import channels.RMIservice;
import channels.RestoreChannel;

public class Peer implements RMIservice {

	private static String RMIobjName;

	private static int protocolVersion;
	private static String serverId;
	private static String accessPoint;

	private static InetAddress mcAddress;
	private static int mcPort;
	private static InetAddress mdbAddress;
	private static int mdbPort;
	private static InetAddress mdrAddress;
	private static int mdrPort;

	private static ControlChannel mcChannel;
	private static BackupChannel mdbChannel;
	private static RestoreChannel mdrChannel;

	public static void main(String [] args) throws UnknownHostException {
		if (!validateArgs(args)) {
			System.out.println("USAGE: <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
			return;
		}

		initChannels();

		startRMIservice();
	}

	public static boolean validateArgs(String [] args) throws UnknownHostException {
		// args_order -> MC MDB MDR PROTOCOL_VERSION SERVER_ID SERVICE_ACCESS_POINT
		//                                                     ^-> ver section 5.2 remote obj name?
		if (args.length != 6) {
			return false; 
		}

		protocolVersion = Integer.parseInt(args[0]);
		serverId = args[1];
		accessPoint = args[2];
		mcAddress = InetAddress.getByName(args[3]);
		mcPort = Integer.parseInt(args[4]);
		mdbAddress = InetAddress.getByName(args[5]);
		mdbPort = Integer.parseInt(args[6]);
		mdrAddress = InetAddress.getByName(args[7]);
		mdrPort = Integer.parseInt(args[8]);

		return true;
	}

	public static void initChannels() {
		mcChannel = new ControlChannel(mcAddress, mcPort);
		mdbChannel = new BackupChannel(mdbAddress, mdbPort);
		mdrChannel = new RestoreChannel(mdrAddress, mdrPort);
	}

	public static void startRMIservice() {
		try {
			Peer peer = new Peer();
			RMIservice stub = (RMIservice) UnicastRemoteObject.exportObject(peer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("HelloServer1", stub);
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
