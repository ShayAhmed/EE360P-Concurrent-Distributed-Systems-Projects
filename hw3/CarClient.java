package hw3;

import java.util.Scanner;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class CarClient {

	public static void main(String[] args) {
		String hostAddress;
	    int tcpPort;
	    int udpPort;
	    int clientId;

	    if (args.length != 2) {
	      System.out.println("ERROR: Provide 2 arguments: commandFile, clientId");
	      System.out.println("\t(1) <command-file>: file with commands to the server");
	      System.out.println("\t(2) client id: an integer between 1..9");
	      System.exit(-1);
	    }

	    String commandFile = args[0];
	    clientId = Integer.parseInt(args[1]);
	    hostAddress = "localhost";
	    tcpPort = 7002;// hardcoded -- must match the server's tcp port
	    udpPort = 8002;// hardcoded -- must match the server's udp port

	    try {
	        Scanner sc = new Scanner(new FileReader(commandFile));
	        
	        boolean mode = false;
	        InetAddress ia = InetAddress.getByName(hostAddress);
	        
	        DatagramSocket datasocket = new DatagramSocket();
	        DatagramPacket sPacket,rPacket;
	        
	        Socket socket = new Socket(ia,tcpPort);
	        InputStream input = socket.getInputStream();
	    	OutputStream output = socket.getOutputStream();
	    	
	    	byte[] buf = new byte[1024];

	        while(sc.hasNextLine()) {
	          String cmd = sc.nextLine();
	          String[] tokens = cmd.split(" ");

	          if (tokens[0].equals("setmode")) {
	            // TODO: set the mode of communication for sending commands to the server 
	        	  if(tokens[1].equals("T")) {
	        		  //mode = true;
	        	  } else {
	        		  mode = false;
	        	  }
	        	  
	          }
	          else if (tokens[0].equals("rent")) {
	            // TODO: send appropriate command to the server and display the
	            // appropriate responses form the server
	        	  if(mode) {
	        		  
	        	  } else {

	        		  //send command
	        		  byte[] data = new byte[cmd.length()+1];
		        	  data = cmd.getBytes();
		        	  sPacket = new DatagramPacket(data,data.length,ia,udpPort);
		        	  datasocket.send(sPacket);
		        	  
		        	  //receive response
		        	  rPacket = new DatagramPacket(buf,buf.length);
		        	  datasocket.receive(rPacket);
		        	  String response = new String(rPacket.getData());
		        	  System.out.println(response);
	        	  }
	        	  
	          } else if (tokens[0].equals("return")) {
	            // TODO: send appropriate command to the server and display the
	            // appropriate responses form the server
	        	  if(mode) {
	        		  
	        	  } else {
	        		  
	        		  //send command
	        		  byte[] data = new byte[cmd.length()];
		        	  data = cmd.getBytes();
		        	  sPacket = new DatagramPacket(data,data.length,ia,udpPort);
		        	  datasocket.send(sPacket);
		        	  
		        	  //receive response
		        	  rPacket = new DatagramPacket(buf,buf.length);
		        	  datasocket.receive(rPacket);
		        	  String response = new String(rPacket.getData());
		        	  System.out.println(response);
	        	  }
	          } else if (tokens[0].equals("inventory")) {
	        	  System.out.println("-------------------inventory--------------------------------");
	            // TODO: send appropriate command to the server and display the
	            // appropriate responses form the server
	        	  if(mode) {
	        		  
	        	  } else {
	        		  
	        		  //send command
	        		  byte[] data = new byte[cmd.length()];
		        	  data = cmd.getBytes();
		        	  sPacket = new DatagramPacket(data,data.length,ia,udpPort);
		        	  datasocket.send(sPacket);
		        	  
		        	  //receive response
		        	  rPacket = new DatagramPacket(buf,buf.length);
		        	  datasocket.receive(rPacket);
		        	  String response = new String(rPacket.getData());
		        	  System.out.println(response);
	        	  }
	          } else if (tokens[0].equals("list")) {
	        	  System.out.println("-------------------list--------------------------------");
	            // TODO: send appropriate command to the server and display the
	            // appropriate responses form the server
	        	  if(mode) {
	        		  
	        	  } else {
	        		  
	        		  //send command
	        		  byte[] data = new byte[cmd.length()];
		        	  data = cmd.getBytes();
		        	  sPacket = new DatagramPacket(data,data.length,ia,udpPort);
		        	  datasocket.send(sPacket);
		        	  
		        	  //receive response
		        	  rPacket = new DatagramPacket(buf,buf.length);
		        	  datasocket.receive(rPacket);
		        	  String response = new String(rPacket.getData());
		        	  System.out.println(response);
	        	  }
	          } else if (tokens[0].equals("exit")) {
	            // TODO: send appropriate command to the server 
	        	  if(mode) {
	        		  
	        	  } else {
	        		  
	        		  //send command
	        		  byte[] data = new byte[cmd.length()];
		        	  data = cmd.getBytes();
		        	  sPacket = new DatagramPacket(data,data.length,ia,udpPort);
		        	  datasocket.send(sPacket);
		        	  
		        	  //receive response
		        	  rPacket = new DatagramPacket(buf,buf.length);
		        	  datasocket.receive(rPacket);
		        	  String response = new String(rPacket.getData());
		        	  System.out.println(response);
	        	  }
	          } else {
	            System.out.println("ERROR: No such command");
	          }
	        }
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
