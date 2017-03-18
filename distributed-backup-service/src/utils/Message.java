package utils;

import java.net.DatagramPacket;

public class Message {
	
	private  String msg;
	private  String header;
	private  String body;
	
	
	public enum Day{
		MESSAGE_TYPE,
		VERSION,
		SENDER_ID,
		FILE_ID,
		CHUNK_NO,
		REPLICATION_DEGREE
	}
	public  Message(String header,String body){
		this.header = header;
		this.body = body;
	}
	
	public  Message(DatagramPacket packet){
		msg = new String(packet.getData(),0, packet.getLength());
		
		String[] parts = msg.split("[\\r\\n]+");
		
		header = parts[0];
		body = parts[1];	
	}
	
	public String getMsg(){
		return msg;
	}
	
	public String[] headerFields() {
		return header.split("\\s+");
	}
	
	public String getBody(){
		return body;
	}
}
