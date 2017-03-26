package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import protrocols.BackupProtocol;

public class BackupChannel extends Channel {
	
	public BackupChannel(InetAddress address, int port) {
		super(address, port);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		byte[] buf = new byte[64000];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				this.socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			new Thread(new BackupProtocol(packet)).start();
		}
	}

}
