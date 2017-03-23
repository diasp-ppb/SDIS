package filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import chunk.Chunk;

public class FileSystem {
	
	private final String  chunkDir = "data/chunks/";
	private final String  files  ="data/files/";
	
	public  FileSystem () {
		//Create the peer dir if they dont exists
		if(!directoryExist("data")){
			File data = new File("data");
			data.mkdir();
		}
		if(!directoryExist(chunkDir)){
			File chunks = new File(chunkDir);
			chunks.mkdir();
		}
		if(!directoryExist(files)){
			File file = new File(files);
			file.mkdir();
		}
	}
	
	public static boolean fileExist(String filePathString) {
		File file  = new File(filePathString);
		return (file.exists() && file.isFile());
	}
	
	public static boolean directoryExist(String dirPath) {
		File file = new File(dirPath);
		return (file.exists() && file.isDirectory());
	}
	
	
	byte[]  loadFile(String path) throws FileNotFoundException {
		
		File file = new File(path);
		
		FileInputStream in = new FileInputStream(files);
		
		byte[] data =  new byte[(int) file.length()]; 
		
		
		try {
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println("Load file failed: " + path);
			e.printStackTrace();
		}
		;
		return data;
	}
	
	void saveFile(String name,byte[] data) {
		
		if(!fileExist(files+name)){
			File newfile = new File(files+name);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream out = new FileOutputStream(files+name);
			out.write(data);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void deleteFile(){
		
	}
	
	Chunk loadChunk(String chunkID){
		
		return null;
	}
	
	void Savechunk(Chunk ck){
		
	}
	
	void Deletechunk(String chunkID){
		
	}
	
	
	
	public static ArrayList<Chunk> splitFile( String filepath , int repDegree) throws IOException{	
		ArrayList<Chunk> result = new ArrayList<Chunk>();			
		int chunkCount = 0;
		byte buffer[] = new byte[Chunk.MAX_LENGTH];
		boolean multipleSize = false;
		
		 
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
		return result;
	}
}
