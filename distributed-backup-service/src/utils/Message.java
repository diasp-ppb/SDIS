package utils;

import java.net.DatagramPacket;
import java.util.EnumMap;

public class Message {
	
	private String msg;
	private String header;
	private String body;
	
	private EnumMap<Field, String> fields;
	
	private enum Field {
		MESSAGE_TYPE,
		VERSION,
		SENDER_ID,
		FILE_ID,
		CHUNK_NO,
		REPLICATION_DEGREE
	}
	
	public Message(String header, String body) {
		this.header = header;
		this.body = body;
	}
	
	public Message(DatagramPacket packet) {
		msg = new String(packet.getData(), 0, packet.getLength());
		
		String[] parts = msg.split("[\\r\\n]+");
		
		header = parts[0];
		body = parts[1];
		
		fields = new EnumMap<Field, String>(Field.class);
		
		parseHeaderFields();
	}
	
	private void parseHeaderFields() {
		String[] fieldArray = header.split("\\s+");
		
		for (int i = 0; i < fieldArray.length; i++) {
			fields.put(Field.values()[i], fieldArray[i]);
		}
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getVersion() {
		return fields.get(Field.VERSION);
	}
	
	public String getFileId() {
		return fields.get(Field.FILE_ID);
	}
	
	public int getChunkNo() {
		return Integer.parseInt(fields.get(Field.CHUNK_NO));
	}
	
	public int getReplicationDeg() {
		return Integer.parseInt(fields.get(Field.REPLICATION_DEGREE));
	}
}
