package utils;

import java.io.File;
import java.util.regex.Pattern;

public class Utils {
	
	
	private static final Pattern INTEGER = Pattern.compile("[0-9]+");
	
	
	
	public static boolean fileExist(String filePathString){
		File file  = new File(filePathString);
		return (file.exists() && file.isFile());
	}
	
	public static boolean directoryExist(String dirPath){
		File file = new File(dirPath);
		return (file.exists() && file.isDirectory());
	}
	
	
	public static boolean isInteger(String Integer){
		return INTEGER.matcher(Integer).matches(); 
	}
	
}
