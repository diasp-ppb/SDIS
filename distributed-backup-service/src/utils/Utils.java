package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import chunk.Chunk;

public class Utils {
	
	
	private static final Pattern INTEGER = Pattern.compile("[0-9]+");
	private static final Pattern IPV4 = Pattern.compile(".*/([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+):([0-9]+)");
	private static final Pattern IPV6 = Pattern.compile(".*/([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+(%[a-zA-Z0-9]+)?):([0-9]+)");
	
	
	public static boolean fileExist(String filePathString) {
		File file  = new File(filePathString);
		return (file.exists() && file.isFile());
	}
	
	public static boolean directoryExist(String dirPath) {
		File file = new File(dirPath);
		return (file.exists() && file.isDirectory());
	}
	
	
	public static boolean isInteger(String Integer) {
		return INTEGER.matcher(Integer).matches(); 
	}
	
	public static boolean isIPV4(String ip) {
		return IPV4.matcher(ip).matches();
	}
	
	public static boolean isIPV6(String ip) {
		return IPV6.matcher(ip).matches();
	}
	
	public static ArrayList<Chunk> splitFile( String filepath , int repDegree){
			
		ArrayList<Chunk> result = new ArrayList<Chunk>();			
		int chunkCount = 0;
		byte buffer[] = new byte[Chunk.MAX_LENGTH];
		boolean multipleSize = false;
		
		 try {
			   FileInputStream in = new FileInputStream(filepath);
	           int eof = in.read(buffer);
	            
	            for(;eof != -1;chunkCount++) {
	            	
	            	if(eof % Chunk.MAX_LENGTH == 0) {
	            		multipleSize = true;
	            	} else
	            		multipleSize = false;
	            	
	            	result.add(new Chunk(chunkCount, "TODO", repDegree, Arrays.copyOf(buffer, eof)));
	            	System.out.println(eof);
	            	eof = in.read(buffer);
	            }
	   
	            System.out.println("MULIPLO: " + multipleSize);
	    
	            if(multipleSize){
	            	result.add(new Chunk(chunkCount,"TODO",repDegree,new byte[0]));
	            }
	           
	            in.close();
	  
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
}
