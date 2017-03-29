package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import filesystem.UpdateRequest;
import peer.Peer;
import protrocols.BackupProtocol;
import utils.Message;

public class ControlChannel extends Channel{

	public ControlChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	byte[] buf = new byte[65000];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				this.socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			Message received = new Message(packet);
			
			switch(received.getType()){
			case "STORED" :
				new Thread(new UpdateRequest(peer,received)).start();
				break;
	
			case "GETCHUNK":
				break;
			
			case "DELETE":
				break;
			
			case "REMOVED":
				break;
			
			default:
				break;
			}
		}
	}
}
