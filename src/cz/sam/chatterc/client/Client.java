package cz.sam.chatterc.client;

import java.net.Socket;
import java.util.Map.Entry;

import cz.sam.chatter.network.TCPConnection;
import cz.sam.chatter.network.events.PacketListener;
import cz.sam.chatter.network.packets.Packet;
import cz.sam.chatter.network.packets.PacketLogin;

public class Client implements Runnable, PacketListener {
	
	private int id;
	private TCPConnection connection;
	private Socket socket;
	private ClinetManager clientManager;
	private String clientUsername;
	
	public Client(Socket socket, ClinetManager clientManager) {
		this.socket = socket;
		this.clientManager = clientManager;
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		this.connection.stop();
	}
	
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		this.connection = new TCPConnection(this);
		this.connection.setPacketListener(this);
		this.connection.start();
	}
	
	public void sendPacket(Packet packet) {
		this.connection.sendPacket(packet);
	}
	
	@Override
	public void onPacketRecive(Packet packet) {
		if(packet.getPacketID() == Packet.packetLogin.getPacketID()) {
			PacketLogin loginPacket = (PacketLogin) packet;
			this.clientUsername = loginPacket.getUsername();
			System.out.println("Client connected as " + this.clientUsername);
			this.sendClientConnection();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			for(Entry<Integer, Client> e : this.clientManager.getAllClients()) {
				if(e.getValue().getId() != this.id) {
					PacketLogin packetLogin = Packet.packetLogin;
					packetLogin.setUsername(e.getValue().getClientUsername());
					this.sendPacket(packetLogin);
				}
			}
		}
	}

	private void sendClientConnection() {
		PacketLogin packetLogin = Packet.packetLogin;
		packetLogin.setUsername(this.clientUsername);
		System.out.println("Sending: " + packetLogin.getUsername());
		this.sendPacketToAllClients(packetLogin);
	}

	public void sendPacketToAllClients(Packet packet) {
		for(Entry<Integer, Client> e : this.clientManager.getAllClients()) {
			if(e.getValue().getId() != this.id) {
				Client client = e.getValue();
				client.sendPacket(packet);
			}
		}
	}

	@Override
	public void onPacketSending(Packet packet) {
		
	}

	public int getId() {
		return id;
	}

	public Socket getSocket() {
		return socket;
	}

	public ClinetManager getClientManager() {
		return clientManager;
	}
	
	public String getClientUsername() {
		return this.clientUsername;
	}
	
}
