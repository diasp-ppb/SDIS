package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import peer.Peer;
import utils.Message;

public class RestoreChannel  extends Channel{

	public RestoreChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		
	byte[] buf = new byte[65000];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				this.socket.receive(packet);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			//LANÇAR THREAD  para RESTORE 
		}
		
	}
}
