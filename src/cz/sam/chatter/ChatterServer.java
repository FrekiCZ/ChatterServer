package cz.sam.chatter;

import java.net.ServerSocket;
import java.net.Socket;

import cz.sam.chatterc.client.Client;
import cz.sam.chatterc.client.ClinetManager;

public class ChatterServer {
	
	private ClinetManager clientManager;
	
	public ChatterServer() {
		this.clientManager = new ClinetManager();
	}
	
	public void start() throws Exception {
		System.out.println("Chatter server online !");
		ServerSocket serverSocket = new ServerSocket(24468);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			Client client = new Client(clientSocket, this.clientManager);
			this.clientManager.add(client);
			client.start();
		}
	}
	
}
