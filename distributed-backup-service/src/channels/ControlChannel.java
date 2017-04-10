package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;

import filesystem.UpdateRequest;
import peer.Peer;
import protrocols.BackupInitiator;
import protrocols.DeleteEnhacement;
import protrocols.DeleteProtocol;
import protrocols.RestoreProtocol;
import utils.Message;

public class ControlChannel extends Channel{
	
	private HashMap<String, BackupInitiator> backupInitiators;
	
	
	public ControlChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
		backupInitiators = new HashMap<String, BackupInitiator>();
	}

	@Override
	public void run() {
		byte[] buf = new byte[65000];

		while (true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				this.socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			Message received = new Message(packet);

			if(!received.getSenderId().equals(peer.getId())) {
				switch (received.getType()) {
				case "STORED":
					System.out.println(received.toString());
					String key =  received.getFileId()+received.getChunkNo();
					if(backupInitiators.containsKey(key)){
						backupInitiators.get(key).increaseReplicationDegree();
					}
					new Thread(new UpdateRequest(peer, received)).start();
					break;
				case "GETCHUNK":
					new Thread(new RestoreProtocol(peer, received)).start();
					break;
				case "DELETE":
					new Thread(new DeleteProtocol(peer,received)).start();
					break;
				case "REMOVED":
					new Thread(new UpdateRequest(peer, received)).start();
					break;
				case "CHECKDELETED":
					if (peer.getProtocolVersion().equals("2.0")) {
						new Thread(new DeleteEnhacement(peer)).start();
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	public void addBackupInitiator(String chunkKey, BackupInitiator backup) {
		backupInitiators.put(chunkKey, backup);
    }
    
    public void removeBackupInitiator(String chunkKey){
    	backupInitiators.remove(chunkKey);
    }
}
