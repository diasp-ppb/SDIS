package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;

public class Message {
	
	private byte[] msg;
	private String header;
	private byte[] body;
	
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
	
	public Message(String header, byte[] body) {
		this.header = header;
		this.body = body;
	}
	
	public Message(EnumMap<Field, String> fields, byte[] body) {
		this.fields = fields;
		
		convertFieldsToHeader();
		
		this.body = body;
		ByteArrayOutputStream builder = new ByteArrayOutputStream();
		try {
			builder.write(this.header.getBytes());
			builder.write(this.body);
		} catch (IOException e) {
			System.out.println("builder fail");
			e.printStackTrace();
		}
		
		
		this.msg = builder.toByteArray();
		
		
	}
	
	public Message(EnumMap<Field, String> fields) {
		this.fields = fields;
		convertFieldsToHeader();
		msg = header.getBytes();
	}
	
	public Message(DatagramPacket packet) {
		msg = Arrays.copyOf(packet.getData(), packet.getLength());
		
		
		
		String copy = new String(packet.getData(), packet.getOffset(), packet.getLength());
		String[] parts = copy.split("\r\n\r\n", 2);
		
	
		header = parts[0];
		body = Arrays.copyOfRange(msg, header.length() + 4+25,msg.length);
		System.out.println("mesage DatagramPacket packe "+ this.body.length);
		fields = new EnumMap<Field, String>(Field.class);
		
		parseHeaderFields();
	}
	
	private void parseHeaderFields() {
		String[] fieldArray = header.split("\\s+");
		
		for (int i = 0; i < fieldArray.length; i++) {
			fields.put(Field.values()[i], fieldArray[i]);
		}
	}
	
	private void convertFieldsToHeader() {
		header = "";
		
		for (Field field : Field.values()) {
	        String value = fields.get(field);
	        
	        if (value != null) {
		        if (field == Field.MESSAGE_TYPE) {
		        	header += value;
		        } else {
		        	header += " " + value;
		        }
	        }
		}
		header += "\r\n\r\n";
	}
	
	public byte[] getMsg() {
		return msg;
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
		return body;
	}
	
	public DatagramPacket getPacket() {
		return packet;
	}
	
	public  String toString() {
		return header;
		
	}
}
