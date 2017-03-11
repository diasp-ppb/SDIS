package cli;



public class TestApp {
	
	private static final int PEER_AP = 0;
	private static final int COMMAND = 1;
	private static final int OPND_1 = 2;
	private static final int OPND_2 = 3;
	
	
	public static void main( String [] args){
		
		if(args.length < 2 || args.length > 4){
			System.out.println("TestAPP");
			System.out.println("Usage:  java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2> ");
			System.out.println("");
			System.out.println("<peer_ap> Is the peer's access point.");
			System.out.println("<sub-protocol> Options: BACKUP | RESTORE | DELETE | RECLAIM");
			System.out.println("<opnd_1> Path name of the file | amount of space to reclaim");
			System.out.println("<opnd_2> An integer that specifies the desired replication degree");
			return;
		}
		
		System.out.println(args[PEER_AP]);
		System.out.println(args[COMMAND]);
		System.out.println(args[OPND_1]);
		System.out.println(args[OPND_2]);
		
		
		if(validCommand(args)){
			return;
		}
		
	}


private static boolean validCommand(String[] args){
	
	//TODO CHECK PEER_AP
	
	String command = args[COMMAND];		
	int n_args = args.length;
	switch(command){
		case "BACKUP":
				if(n_args != 4)
					return false;
			break;
		case "DELETE":
				if(n_args != 3)
					return false;
			break;
		case "RECLAIM":
				if(n_args != 3)
					return false;
			break;
		case "STATE":
				if(n_args != 2)
					return false;
			break;
		default:
			System.out.println("Commands avaiable: BACKUP | DELETE | RECLAIM");
			return false;
		}
	return false;
	}
}