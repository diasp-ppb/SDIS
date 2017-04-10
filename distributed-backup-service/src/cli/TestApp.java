package cli;

import utils.*;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import peer.RMIservice;

public class TestApp {
	private static final int PEER_AP = 0;
	private static final int COMMAND = 1;
	private static final int OPND_1 = 2;
	private static final int OPND_2 = 3;
	private static final int MAX_ARGS_REQUEST = 3;

	public static void main(String [] args) {
		System.out.println(args[PEER_AP]);
		System.out.println(args[COMMAND]);
		//System.out.println(args[OPND_1]);
		//System.out.println(args[OPND_2]);

		if(!validCommand(args)) {
			System.out.println("INVALID COMMAND");
			return;
		}


		try {
			System.out.println("Connecting to peer " + args[PEER_AP] + ".");
			Registry registry = LocateRegistry.getRegistry();
			RMIservice stub = (RMIservice) registry.lookup(args[PEER_AP]);
			
			switch (args[COMMAND]) {
			case "BACKUP":
				stub.backup(args[OPND_1], Integer.parseInt(args[OPND_2]));
				break;
			case "RESTORE":
				stub.restore(args[OPND_1]);
				break;
			case "DELETE":
				stub.delete(args[OPND_1]);
				break;
			case "RECLAIM":
				stub.reclaim(Integer.parseInt(args[OPND_1]));
				break;
			case "STATE":
				System.out.println(stub.state());
				break;
			default:
				return;
			}
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	private static boolean validCommand(String[] args) {
		if(args.length < 2 || args.length > 4){
			System.out.println("TestAPP");
			System.out.println("Usage:  java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
			System.out.println("");
			System.out.println("<peer_ap> Is the peer's access point.");
			System.out.println("<sub-protocol> Options: BACKUP | RESTORE | DELETE | RECLAIM | STATE");
			System.out.println("<opnd_1> Path name of the file | amount of space to reclaim");
			System.out.println("<opnd_2> An integer that specifies the desired replication degree");
			return false;
		}

		String command = args[COMMAND];		
		int n_args = args.length;

		switch (command) {
		case "BACKUP":
			if (n_args != 4 || !Utils.isInteger(args[OPND_2])) {
				return false;
			}
			break;
		case "RESTORE":
			if(n_args != 3) {
				return false;
			}
			break;
		case "DELETE":
			if(n_args != 3) {
				return false;
			}
			break;
		case "RECLAIM":
			if (n_args != 3 || !Utils.isInteger(args[OPND_1])) {
				return false;
			}
			break;
		case "STATE":
			if(n_args != 2) {
				return false;
			}
			break;
		default:
			System.out.println("Commands avaiable: BACKUP | RESTORE | DELETE | RECLAIM | STATE");
			return false;
		}

		return true;
	}
}


