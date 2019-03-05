package hw3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CarServer {

	public static void main(String[] args) {
		int tcpPort;
	    int udpPort;
	    if (args.length != 1) {
	      System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
	      System.exit(-1);
	    }
	    String fileName = args[0];
	    tcpPort = 7002;
	    udpPort = 8002;

	    // parse the inventory file
	    
	    ArrayList<CarType> inventory = new ArrayList<CarType>();
	    ArrayList<Record> records = new ArrayList<Record>();
	    AtomicInteger recordId = new AtomicInteger(0);
	    
	    try {
			Scanner sc = new Scanner(new FileReader(fileName));
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] components = line.split(" ");
				
				CarType addition = new CarType(components[0],components[1],Integer.parseInt(components[2]));
				inventory.add(addition);
			}
			System.out.println(inventory);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	    // TODO: handle request from clients
	    
	    Thread tcp_server = new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
					ServerSocket server = new ServerSocket(tcpPort);
					
					while(true) {
						
						Socket clientSocket = server.accept();
						ClientHandler client = new ClientHandler(clientSocket);
						Thread handle = new Thread(client);
						handle.start();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	    	
	    });
	    
	    Thread udp_server = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					DatagramSocket datasocket = new DatagramSocket(udpPort);
					DatagramPacket sPacket, rPacket;
					byte[] buf = new byte[1024];
					while(true) {
						
						rPacket = new DatagramPacket(buf,buf.length);
						datasocket.receive(rPacket);
						
						String cmd = new String(rPacket.getData());
						//cmd = cmd.replaceAll("\"", " ");
						cmd = cmd.trim();
						String[] tokens = cmd.split(" ");
						System.out.println("here");


						if (tokens[0].equals("rent")) {
				            // TODO: send appropriate command to the server and display the
				            // appropriate responses form the server

							//process command
							String name = tokens[1];
							String brand = tokens[2];
							String color = tokens[3];
							//String correct = color + "\"";
							System.out.print("what the person wants: ");
							System.out.println(name +brand+color);
							
							boolean have_car = false;
							boolean done = false;
							
							for(int i=0;i<inventory.size();i++) {
								System.out.print(inventory.get(i).brand + " "+brand+" ");
								System.out.print(inventory.get(i).color + " "+color);
								System.out.println(" "+inventory.get(i).quantity);
								if (inventory.get(i).brand.equals(brand)) {
									System.out.println("brand same");
								}
								if (inventory.get(i).color.equals(color)) {
									System.out.println("color same");
								}
								
								if(inventory.get(i).brand.equals(brand) && inventory.get(i).color.equals(color) 
										&& inventory.get(i).quantity != 0) {
									
									inventory.get(i).decrementQuantity();
									Record new_record = new Record(recordId.incrementAndGet(),name,brand,color);
									records.add(new_record);
									
									String response = new String();
									response = "Your request has been approved, " + new_record.id 
											+ " " + name + " " + brand + " " + color;
									byte[] rent_udp = new byte[response.length()+1];
									rent_udp = response.getBytes();
									sPacket = new DatagramPacket(rent_udp,
											rent_udp.length,
											rPacket.getAddress(),
											rPacket.getPort());
									datasocket.send(sPacket);
									done = true;
									break;
								} else if(inventory.get(i).brand.equals(brand) && inventory.get(i).color.equals(color)) {
									have_car = true;
								}
							}
							
							if(have_car && !done) {
								String response = new String();
								response = "Request Failed - Car not available";
								byte[] rent_udp = new byte[response.length()+1];
								rent_udp = response.getBytes();
								sPacket = new DatagramPacket(rent_udp,
										rent_udp.length,
										rPacket.getAddress(),
										rPacket.getPort());
								datasocket.send(sPacket);
							} else if(!have_car && !done) {
								String response = new String();
								response = "Request Failed - We do not have this car";
								byte[] rent_udp = new byte[response.length()+1];
								rent_udp = response.getBytes();
								sPacket = new DatagramPacket(rent_udp,
										rent_udp.length,
										rPacket.getAddress(),
										rPacket.getPort());
								datasocket.send(sPacket);
							}
							
							//send response
				       				        	 				        	  
				        } else if (tokens[0].equals("return")) {
				            // TODO: send appropriate command to the server and display the
				            // appropriate responses form the server
							
							//process command
							
							//send response
				       				        	 				
				        } else if (tokens[0].equals("inventory")) {
				            // TODO: send appropriate command to the server and display the
				            // appropriate responses form the server
							
							//process command
							
							//send response
				       				        	 				
				        } else if (tokens[0].equals("list")) {
				        	 System.out.println("-------------------list--------------------------------");
				        	 System.out.println("record"+records);
				            // TODO: send appropriate command to the server and display the
				            // appropriate responses form the server
							
							//process command
				        	String name = tokens[1];
				        	System.out.println(name);
				        	String response = "";
				        	for(Record rec:records) {
				        		System.out.println("record");
				        		System.out.println(rec);
				        		if (rec.name.equals(name)){
				        			response = response + rec.id + " "+rec.brand+" "+rec.color; 
				        		}
				        	}
				        	//System.out.println("responce: "+response);
				        	if(response.equals("")) {
				        		response = "No record found for "+tokens[1];
				        	}
				        	//System.out.println("responce: "+response);
							//send response
				        	
							byte[] rent_udp = new byte[response.length()+1];
							rent_udp = response.getBytes();
							sPacket = new DatagramPacket(rent_udp,
									rent_udp.length,
									rPacket.getAddress(),
									rPacket.getPort());
							datasocket.send(sPacket);
							response = "";
							//done = true;
							break;
				       				        	 				
				        } else if (tokens[0].equals("exit")) {
				            // TODO: send appropriate command to the server 
							
							//process command
							
							//send response
				       				        	 				
				        }
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	    	
	    });
	    
	    tcp_server.start();
	    udp_server.start();
	    
	}

}

class CarType{
	
	String brand;
	String color;
	int quantity;
	
	public CarType(String brand, String color, int quantity) {		
		this.brand = brand;
		this.color = color;
		this.quantity = quantity;		
	}
	
	public synchronized int incrementQuantity() {
		quantity++;
		return quantity;
	}
	
	public synchronized int decrementQuantity() {
		quantity--;
		return quantity;
	}
	@Override
	public String toString() {
		String ret = this.brand+this.color+this.quantity;
		return ret;
	}
}

class Record{
	
	int id;
	String brand,color,name;
	
	public Record(int r, String n, String b, String c) {
		id = r;
		name = n;
		brand = b;
		color = c;
	}
	@Override
	public String toString() {
		return id + " "+name + " "+brand + " "+color;
	}
}

class ClientHandler implements Runnable{
	
	Socket mySocket;
	InputStream input;
	OutputStream output;
	
	public ClientHandler(Socket s) {		
		try {
			mySocket = s;
			input = s.getInputStream();
			output = s.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
	}
	
}
