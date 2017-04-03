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
	private final String  filesDir = "data/files/";
	
	public FileSystem () {
		//Create the peer dir if they dont exists
		if(!directoryExist("data")) {
			File data = new File("data");
			data.mkdir();
		} 

		if(!directoryExist(chunkDir)) {
			File chunks = new File(chunkDir);
			chunks.mkdir();
		}

		if(!directoryExist(filesDir)) {
			File file = new File(filesDir);
			file.mkdir();
		}
	}
	
	public String getChunkDir() {
		return chunkDir;
	}

	public boolean fileExist(String filePathString) {
		File file  = new File(filePathString);
		return (file.exists() && file.isFile());
	}

	public boolean directoryExist(String dirPath) {
		File file = new File(dirPath);
		return (file.exists() && file.isDirectory());
	}


	public byte[] loadFile(String path) throws FileNotFoundException {
		File file = new File(path);

		FileInputStream in = new FileInputStream(file);

		byte[] data =  new byte[(int) file.length()]; 

		try {
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println("Load file failed: " + path);
			e.printStackTrace();
		}

		return data;
	}

	public void saveFile(String name,byte[] data) {
		if(!fileExist(filesDir + name)) {
			File newfile = new File(filesDir + name);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(filesDir + name);
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

	public void deleteFile(String file) {
		File erase = new File (filesDir + file);
		try {
			erase.delete();
		} catch(Exception e){
			System.out.println("Unable to delete File: " + file);
		}
	}

	public byte[] loadChunk(String chunkID, String fileID) throws FileNotFoundException {
		//TODO 
		File load = new File(chunkDir + fileID + "/"+ chunkID);

		FileInputStream in = new FileInputStream(load);
		byte[] data = new byte[(int) load.length()];

		try {
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println("Load file failed: " + chunkID);
			e.printStackTrace();
		}
		return data;
	}

	public void saveChunk(Chunk ck) {
		String fileChunks = chunkDir + ck.getFileId() + "/";
		String path = fileChunks + ck.getFileId() + ck.getChunkNo();
		if(! directoryExist(fileChunks)){
			File theDir = new File(fileChunks);
			theDir.mkdir();
		}
		File newfile = new File(path);
		if(!fileExist(path)) {	
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream out = new FileOutputStream(newfile);
			System.out.println("DATA STORED: " + ck.getFileData().length);
			out.write(ck.getFileData());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void Deletechunk(String chunkID,String fileID){
		String path = chunkDir + fileID + "/" + chunkID;
		if(fileExist(path)) {
			File erase = new File(path);
			erase.delete();
		}
	}

	public  ArrayList<Chunk> splitFile( File file ,byte[] filehash, int repDegree) throws IOException {	
		ArrayList<Chunk> result = new ArrayList<Chunk>();			
		int chunkCount = 0;
		byte buffer[] = new byte[Chunk.MAX_LENGTH];
		boolean multipleSize = false;

		FileInputStream in = new FileInputStream(file);
		int eof = in.read(buffer);

		for(;eof != -1;chunkCount++) {

			if(eof % Chunk.MAX_LENGTH == 0) {
				multipleSize = true;
			} else
				multipleSize = false;

			result.add(new Chunk(chunkCount, new String(filehash), repDegree, Arrays.copyOf(buffer, eof)));
			//System.out.println(eof);
			eof = in.read(buffer);
		}

		//System.out.println("MULIPLO: " + multipleSize);

		if(multipleSize) {
			result.add(new Chunk(chunkCount, new String(filehash),repDegree,new byte[0]));
		}

		in.close();
		//System.out.println(result.size());
		return result;
	}
	

}
