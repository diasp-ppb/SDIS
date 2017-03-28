package utils;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;

public class Message {
	
	private String msg;
	private String header;
	private String body;
	
	private DatagramPacket packet;
	
	private EnumMap<Field, String> fields;
	
	public enum Field {
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
	
	public Message(EnumMap<Field, String> fields, byte[] body) {
		this.fields = fields;
		
		convertFieldsToHeader();
		
		this.body = new String(body, StandardCharsets.US_ASCII);
		this.msg = this.header + "\r\n\r\n" + this.body;
		System.out.println(msg.length());
	}
	
	public Message(EnumMap<Field, String> fields) {
		this.fields = fields;
		convertFieldsToHeader();
		msg = header;
	}
	
	public Message(DatagramPacket packet) {
		msg = new String(packet.getData(), 0, packet.getLength());
		
		String[] parts = msg.split("\r\n\r\n",2);
		
		System.out.println("msg parts: " + parts.length);
		header = parts[0];
		body = parts[1];
		
		fields = new EnumMap<Field, String>(Field.class);
		
		parseHeaderFields();
		
		System.out.println("HEADER :" + header) ;
		System.out.println("fields :" + fields) ;
		
	}
	
	private void parseHeaderFields() {
		String[] fieldArray = header.split("\\s+");
		
		for (int i = 0; i < fieldArray.length; i++) {
			fields.put(Field.values()[i], fieldArray[i]);
		}
	}
	
	private void convertFieldsToHeader() {
		header = "";
		
		for(Field field : Field.values()) {
	        String value = fields.get(field);
	        
	        if (value != null) {
	        	header += " "+ value;
	        }
		}
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getType() {
		return fields.get(Field.MESSAGE_TYPE);
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
	
	public byte[] getData() {
		return body.getBytes(StandardCharsets.US_ASCII);
	}
	
	public DatagramPacket getPacket() {
		return packet;
	}
	
	public  String toString() {
		return header;
		
	}
}
