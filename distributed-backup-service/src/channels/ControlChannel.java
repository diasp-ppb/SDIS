package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import filesystem.UpdateRequest;
import peer.Peer;
import protrocols.DeleteProtocol;
import protrocols.RestoreProtocol;
import utils.Message;

public class ControlChannel extends Channel{

	public ControlChannel(Peer peer, InetAddress address, int port) {
		super(peer, address, port);
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
					new Thread(new UpdateRequest(peer, received)).start();
					break;
				case "GETCHUNK":
					new Thread(new RestoreProtocol(peer, packet)).start();
					break;
				case "DELETE":
					new Thread(new DeleteProtocol(peer,received)).start();
					break;
				case "REMOVED":
					new Thread(new UpdateRequest(peer, received)).start();
					break;
				default:
					break;
				}
			}
		}
	}
}
