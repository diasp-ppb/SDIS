package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import peer.Peer;
import protrocols.BackupInitiator;
import protrocols.BackupProtocol;
import protrocols.RestoreInitiator;
import utils.Message;

public class BackupChannel extends Channel {
	
	
	public BackupChannel(Peer peer, InetAddress address, int port) {
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
			
			Message  msg = new Message(packet);
			if(!msg.getSenderId().equals(peer.getId()))
			new Thread(new BackupProtocol(peer, packet)).start();
		}
	}
}
