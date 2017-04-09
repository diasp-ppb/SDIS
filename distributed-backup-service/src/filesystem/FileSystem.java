package filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.Message;

public class FileSystem {

	private final String  chunkDir = "data/chunks/";
	private final String  filesDir = "data/files/";
	private final String  backupDir= "data/backup/";
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
		
		if(!directoryExist(backupDir)) {
			File file = new File(backupDir);
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

	public byte[] loadChunk(int fileID, int chunkNo) throws FileNotFoundException {

		String path = chunkDir +fileID+ "/"+chunkNo;

		File load = new File(path);

		//System.out.println(path);

		if(directoryExist(path)) {
			System.out.println("existe");
		};

		FileInputStream in = new FileInputStream(load);
		byte[] data = new byte[(int) load.length()];

		try {
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println("Load file failed: " + fileID+ " " +chunkNo);
			e.printStackTrace();
		}
		return data;
	}

	public void saveChunk(int fileStoreId, int chunkNo,byte[] data) {
		String fileChunks = chunkDir + fileStoreId + "/";
		String path = fileChunks + chunkNo;
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

	void Deletechunk(String chunkID,String fileID){
		String path = chunkDir + fileID + "/" + chunkID;
		if(fileExist(path)) {
			File erase = new File(path);
			erase.delete();
		}
	}

	public  ArrayList<byte[]> splitFile(File file) throws IOException {	

		ArrayList<byte[]> result = new ArrayList<byte[]>();			
		byte buffer[] = new byte[Message.body_MAX_LENGHT];
		boolean multipleSize = false;

		FileInputStream in = new FileInputStream(file);
		int eof = in.read(buffer);

		while(eof != -1) {
			if(eof % Message.body_MAX_LENGHT == 0) {
				multipleSize = true;
			} else
				multipleSize = false;

			result.add(Arrays.copyOf(buffer, eof));
			eof = in.read(buffer);
		}

		if(multipleSize) {
			result.add(new byte[0]);
		}

		in.close();
		return result;
	}

	private void saveFile(String name,byte[] data , boolean append) {

		System.out.println(data.length);

		if(!fileExist(filesDir + name)) {
			File newfile = new File(filesDir + name);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(filesDir + name, append);
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

	public  boolean restoreFile (Message data[], String filename) {

		if(data.length > 0) {
			saveFile(filename,data[0].getData(), false);
			for( int i = 1; i < data.length ; i++) {
				saveFile(filename, data[i].getData(), true);
			}
			return true;
		}
		return false;
	}
	
	
	public void addFileToDBbackup(String key, FileData value, boolean append) {
		
		String files = "files";
		
		String save = key + " " + value.getName();
		save += " " + value.getFileSize() +  " ";
		
		if(value.getOwner() == null) {
			save += "NULL";
		}else{
			save += value.getOwner();
		}
		save += " " + value.getLastModification() + " " + value.getReplicationDegree();
		save += " " + value.getFileId() + "\n";
		
		if(!fileExist(backupDir + files)) {	
			try {
				File newfile = new File(backupDir + files);
				newfile.createNewFile();
			} catch (IOException e) {
				System.out.println("Unable to create file "+ files );
				return;
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(backupDir + files, append);
			out.write(save.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	public void addChunkToDBbackup(String key, ChunkData value , boolean append) {
		String chunks = "chunks";
		
		String save = key + " " + value.getCurrentReplication();
		save += " " + value.getMinReplication() + " " + value.getChunkSize();
		save += " " + value.getFileId() + " " + value.getChunkNo() + "\n";
		
		if(!fileExist(backupDir + chunks)) {	
			try {
				File newfile = new File(backupDir + chunks);
				newfile.createNewFile();
			} catch (IOException e) {
				System.out.println("Unable to create file "+ chunks );
				return;
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(backupDir + chunks, append);
			out.write(save.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	
	}

	public void saveDatabase(Database db){
		Map<String, FileData> save =  db.getSentFiles();
		
		Iterator<Entry<String, FileData>> it = save.entrySet().iterator();
		
		Map.Entry<String, FileData> entry = it.next();

		addFileToDBbackup(entry.getKey(),entry.getValue(),false);
		
		while(it.hasNext()){
			entry = it.next();
			addFileToDBbackup(entry.getKey(),entry.getValue(),true);
		}
		
		
		Map<String, ChunkData> chunks =  db.getStoredChunks();
		
		Iterator<Entry<String, ChunkData>> itChunk = chunks.entrySet().iterator();
		
		Map.Entry<String, ChunkData> entryChunk = itChunk.next();
		
		addChunkToDBbackup(entryChunk.getKey(),entryChunk.getValue(),false);
		
		while(itChunk.hasNext()){
			entryChunk = itChunk.next();
			addChunkToDBbackup(entryChunk.getKey(),entryChunk.getValue(),true);
		}
		
		
	}
	
	
	
	public boolean loadDatabase(Database db) {
		
		String files = backupDir + "files";
		String chunks = backupDir + "chunks";
		
		
		if(!fileExist(chunks)) {
			return false;
		}
		
		if(!fileExist(files)) {	
			return false;
		}
		
		String line;
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(files)))) {
		    
		    while ((line = br.readLine()) != null) {
		    	String [] parts = line.split("\\s+");
		    	String path = parts[0];
		    	String name = parts[1];
		    	long filesize = Long.parseLong(parts[2]);
		    	String owner = parts[3];
		    	long lastModification = Long.parseLong(parts[4]);
		    	int chunkNo = Integer.parseInt(parts[5]);
		    	int replicationDegree = Integer.parseInt(parts[6]);
		    	String fileId = parts[7];
		    	db.saveStoredFile(path,new FileData(name,filesize,owner, lastModification, chunkNo,replicationDegree, fileId));
		    }
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(chunks)))) {
			 String [] parts = line.split("\\s+");
			 
			 String chunkKey = parts[0];
			 int currentReplication = Integer.parseInt(parts[1]);
		     int minReplication = Integer.parseInt(parts[2]);
			 int chunkSize = Integer.parseInt(parts[3]);
			 String fileId = parts[4];
			 int chunkNo = Integer.parseInt(parts[5]);
			 
			 ChunkData newData = new ChunkData(chunkKey, currentReplication, minReplication, chunkSize, fileId, chunkNo);
			 
			 db.saveChunkInfo(chunkKey, newData);
			
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		
		return true;
	}
}
