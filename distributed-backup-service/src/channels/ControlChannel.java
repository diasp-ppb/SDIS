package channels;

import java.net.InetAddress;

import peer.Peer;

public class ControlChannel extends Channel{

	public ControlChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
