package utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {
	
	
	private static final Pattern INTEGER = Pattern.compile("[0-9]+");
	private static final Pattern IPV4 = Pattern.compile(".*/([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+):([0-9]+)");
	private static final Pattern IPV6 = Pattern.compile(".*/([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+(%[a-zA-Z0-9]+)?):([0-9]+)");
	private static final Random randomGenerator = new Random();
	
	
	
	public static boolean isInteger(String Integer) {
		return INTEGER.matcher(Integer).matches(); 
	}
	
	public static boolean isIPV4(String ip) {
		return IPV4.matcher(ip).matches();
	}
	
	public static boolean isIPV6(String ip) {
		return IPV6.matcher(ip).matches();
	}
	
	public static final byte[] sha256(String text) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(text.getBytes(StandardCharsets.UTF_8));
	}
	
	public int randomNumber(int min, int max) {
		return randomGenerator.nextInt() % (max-min) + min;
	}
}
