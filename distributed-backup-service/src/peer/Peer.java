package peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import channels.BackupChannel;
import channels.ControlChannel;
import channels.RestoreChannel;
import filesystem.Database;
import filesystem.FileSystem;
import protrocols.BackupInitiator;
import protrocols.DeleteInitiator;
import protrocols.RestoreInitiator;
import utils.Message;

public class Peer implements RMIservice {
	private String protocolVersion;
	private String peerId;
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
	private Database db; //TODO INICIALIZAR E CRIAR FUNÇOES DE INSERÇAO/UPDATE

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
		fs = new FileSystem();
		db = new Database();
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}
	
	public boolean validateArgs(String [] args) throws UnknownHostException {
		if (args.length != 9) {
			return false;
		}

		protocolVersion = args[0];
		peerId = args[1];
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
		try {
			RMIservice stub = (RMIservice) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(accessPoint, stub);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return peerId;
	}
	
	public FileSystem getFs() {
		return fs;
	}
	
	public BackupChannel getBackupChannel() {
		return mdbChannel;
	}
	
	public RestoreChannel getRestoreChannel() {
		return mdrChannel;
	}
	
	public ControlChannel getControlChannel() {
		return mcChannel;
	}
	
	public Database getDB() {
		return db;
	}
	
	@Override
	public void backup(String path, int replicationDegree) throws RemoteException {
		System.out.println("Called backup with path: "  + path + " and repDegree: " + replicationDegree + ".");
		new Thread(new BackupInitiator(this, path, replicationDegree)).start();
	}

	@Override
	public void restore(String path) throws RemoteException {
		System.out.println("Called restore with path: "  + path + ".");
		new Thread(new RestoreInitiator(this, path)).start();

	}

	@Override
	public void delete(String path ) throws RemoteException {
		System.out.println("Called delete with path: "  + path + ".");
		new Thread(new DeleteInitiator(this,path)).start();

	}

	@Override
	public void reclaim() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String state() throws RemoteException {
		return new State(this).toString();
	}
}