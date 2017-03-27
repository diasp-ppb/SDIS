package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

import peer.Peer;
import utils.Message;

public class Channel implements Runnable {
	
	private InetAddress address;
	private int port;
	protected Peer peer;
	protected MulticastSocket socket;
	
	public Channel(Peer peer, InetAddress address, int port) {
		this.address = address;
		this.port = port;
		this.peer = peer;
		
		try {
			socket = new MulticastSocket(port);
			socket.joinGroup(address);
		} catch (IOException e) {
			System.out.println("Channel: socket creation failed");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
	public void sendMessage(Message m) {
		byte[] msg = m.getMsg().getBytes(StandardCharsets.US_ASCII);
		DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Channel: send message fail");
			e.printStackTrace();
		}
	}
	
	public void closeSocket() {
		try {
			socket.leaveGroup(address);
			socket.close();
		} catch (IOException e) {
			System.out.println("Channel: socket close failed");
			e.printStackTrace();
		}
		
	}
	
}
