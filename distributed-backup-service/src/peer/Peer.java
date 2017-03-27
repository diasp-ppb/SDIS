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
import filesystem.FileSystem;

public class Peer implements RMIservice {

	private String RMIobjName;

	private int protocolVersion;
	private String serverId;
	private String accessPoint;

	private InetAddress mcAddress;
	private int mcPort;
	private InetAddress mdbAddress;
	private int mdbPort;
	private InetAddress mdrAddress;
	private int mdrPort;

	private ControlChannel mcChannel;
	private BackupChannel mdbChannel;
	private RestoreChannel mdrChannel;
	
	private FileSystem fs;

	public static void main(String [] args) throws UnknownHostException {
		try {
			Peer peer = new Peer(args);
			peer.initChannels();
			peer.startRMIservice();
		} catch (InvalidArgumentsException e) {
			System.out.println("USAGE: <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>");
		}
	}

	public Peer(String[] args) throws UnknownHostException, InvalidArgumentsException {
		if (!validateArgs(args)) {
			throw new InvalidArgumentsException("Invalid peer arguments.");
		}
	}

	public boolean validateArgs(String [] args) throws UnknownHostException {
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

	public void initChannels() {
		mcChannel = new ControlChannel(this, mcAddress, mcPort);
		mdbChannel = new BackupChannel(this, mdbAddress, mdbPort);
		mdrChannel = new RestoreChannel(this, mdrAddress, mdrPort);

		new Thread(mcChannel).start();
		new Thread(mdbChannel).start();
		new Thread(mdrChannel).start();
	}

	public void startRMIservice() {
		boolean listening = true;

		while (listening) {
			try {
				RMIservice stub = (RMIservice) UnicastRemoteObject.exportObject(this, 0);
				Registry registry = LocateRegistry.getRegistry();
				registry.rebind("HelloServer1", stub);
			} catch (Exception e) {
				e.printStackTrace();
				listening = false;
			}
		}
	}
	
	public String getId() {
		return serverId;
	}
	
	public FileSystem getFs() {
		return fs;
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
