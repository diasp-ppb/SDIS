package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import peer.Peer;
import protrocols.BackupProtocol;

public class BackupChannel extends Channel {
	
	public BackupChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		byte[] buf = new byte[65000];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			new Thread(new BackupProtocol(peer, packet)).start();
		}
	}

}
