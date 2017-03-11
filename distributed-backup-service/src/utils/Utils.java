package utils;

import java.io.File;

public class Utils {
	
	
	
	
	
	public static boolean fileExist(String filePathString){
		File file  = new File(filePathString);
		return (file.exists() && file.isFile());
	}
	
	public static boolean directoryExist(String dirPath){
		File file = new File(dirPath);
		return (file.exists() && file.isDirectory());
	}
	
	
	
	
}
