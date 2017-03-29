package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import filesystem.UpdateRequest;
import peer.Peer;
import protrocols.BackupProtocol;
import utils.Message;

public class RestoreChannel  extends Channel{

	public RestoreChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
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
			
			new Thread(new UpdateRequest(peer, received)).start();
		}
	}

}
