package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import filesystem.UpdateRequest;
import peer.Peer;
import protrocols.RestoreInitiator;
import utils.Message;

public class RestoreChannel  extends Channel{
	
	private HashMap<String, RestoreInitiator> restoreInitiators;

	public RestoreChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
		restoreInitiators = new HashMap<String, RestoreInitiator>();
	}

	@Override
	public void run() {
		byte[] buf = new byte[65000];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			Message received = new Message(packet);
			
			//was requested by peer
			System.out.println(restoreInitiators);
			System.out.println(received.toString());
			if(restoreInitiators.containsKey(received.getFileId()))
			{
				restoreInitiators.get(received.getFileId()).putMessage(received);
			}
			else {
				new Thread(new UpdateRequest(peer, received)).start();
			}
			
		}
	}
	
	
    public void addRestoreInitiator(String fileId, RestoreInitiator restore) {
    	restoreInitiators.put(fileId, restore);
    }
    
    public void removerestoreInitiator(String fileId){
    	restoreInitiators.remove(fileId);
    }
}
