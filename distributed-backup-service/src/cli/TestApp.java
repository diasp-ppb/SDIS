package cli;

import utils.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import filesystem.FileSystem;
import peer.RMIservice;

public class TestApp {
	
	private static final int PEER_AP = 0;
	private static final int COMMAND = 1;
	private static final int OPND_1 = 2;
	private static final int OPND_2 = 3;
	private static final int MAX_ARGS_REQUEST = 3; 
	
	public static void main( String [] args) {
		
		
		//TEMP
		/*System.out.println(args[PEER_AP]);
		System.out.println(args[COMMAND]);
		System.out.println(args[OPND_1]);
		System.out.println(args[OPND_2]);
		
		String[] cmdInfo = new String[MAX_ARGS_REQUEST];
		
		if(!validCommand(args,cmdInfo)){
			System.out.println("INVALID COMMAND");
			return;
		}
		
		String request = cmdInfo[0] + " " + cmdInfo[1]+" "+cmdInfo[2];
		
		System.out.println("RESQUEST: " + request); //TESTE TEMP
		
		//TODO BUILD REQUEST HERE
		
		//TEST CHUNK SPLIT 
		/*
		ArrayList<Chunk> teste = Utils.splitFile(cmdInfo[1], 3);
		
		for(int i = 0; i < teste.size(); i++){
			System.out.println(teste.get(i).toString());
		}*/
		
		
		// RMI TO TEST SERVER SIDE
		
	 	
		 try {
			 	System.out.println(args[PEER_AP]);
	            Registry registry = LocateRegistry.getRegistry();
	            RMIservice stub = (RMIservice) registry.lookup("helloserver1");
	             stub.backup(args[OPND_1], 2);
	             
	             TimeUnit.SECONDS.sleep(5);
	             System.out.println(stub.state());
	             TimeUnit.SECONDS.sleep(2);
	             stub.reclaim(64000);
	             TimeUnit.SECONDS.sleep(2);
	             System.out.println(stub.state());
	             //stub.restore(args[OPND_1]);
	             
	             

	             TimeUnit.SECONDS.sleep(10);
		         
	            //TimeUnit.SECONDS.sleep(6);
	              //stub.delete(args[OPND_1]);
	            //System.out.println(stub.state());
	         //   System.out.println("response: " + response);
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	        
		// TO TEST MESSAGE SPLIT
	//	Message t = new Message("asdasdasd \r\n\r\n asdasdadasda"," ");
 
	}
private static boolean validCommand(String[] args, String[] request) {
	
	//TODO  Validar se ficheiro existe 
	
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
	System.out.println("COMMAND "+command  + " " + n_args);
	
	if(!Utils.isIPV4(args[PEER_AP]) && !Utils.isIPV6(args[PEER_AP]) && !Utils.isInteger(args[PEER_AP])){
		System.out.println("INVALID IP/PORT");
		return false;
	}
		
	
	switch(command){
		case "BACKUP":
				if(n_args != 4 || !(Utils.isInteger(args[OPND_2])))
					return false;
						
				request[1] = args[OPND_1];
				request[2] = args[OPND_2];
			break;
			
		case "DELETE":
				if(n_args != 3)
					return false;
				request[1] = args[OPND_1];
			break;
			
		case "RECLAIM":
				if(n_args != 3 || !Utils.isInteger(args[OPND_1]))
					return false;
				
				request[1] = args[OPND_1];
			break;
		case "STATE":
				if(n_args != 2)
					return false;
			break;
		default:
			System.out.println("Commands avaiable: BACKUP | DELETE | RECLAIM");
			return false;
		}
	
	request[0] = command; //commom to all 
	return true;
	}
}


