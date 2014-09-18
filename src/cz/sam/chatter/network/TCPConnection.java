package cz.sam.chatter.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import cz.sam.chatter.network.events.PacketListener;
import cz.sam.chatter.network.packets.Packet;
import cz.sam.chatter.network.packets.PacketQuit;
import cz.sam.chatterc.client.Client;

public class TCPConnection {
	
	private Client client;
	private Socket socket;
    public DataOutputStream output;
    public DataInputStream input;
    
    private boolean isReadThreadRunning = false;
    private boolean isWriteThreadRunning = false;
    private Thread readThread;
    private Thread writeThread;
    public PacketListener packetListener;
    
    private Queue<Packet> writeQueue;
    
    public TCPConnection(Client client) {
        try {
        	this.client = client;
            this.socket = client.getSocket();
            this.output = new DataOutputStream(socket.getOutputStream());
            this.input = new DataInputStream(socket.getInputStream());
            this.writeQueue = new LinkedList<>();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void start() {
        this.isReadThreadRunning = true;
        this.isWriteThreadRunning = true;
        this.readThread();
        this.writeThread();
    }
    
    private void readThread() {
    	final TCPConnection connection = this;
        this.readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isReadThreadRunning) {
                    try {
                        Packet.recivePackets(connection);
                    } catch(Exception ex) {
                        stop();
                    }
                }
            }
            
        });
        this.readThread.start();
    }
    
    private void writeThread() {
    	final TCPConnection connection = this;
        this.writeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isWriteThreadRunning) {
                    if(writeQueue.size() > 0) {
                        try {
                            Packet packet = writeQueue.remove();
                            Packet.sendPacket(packet, connection);
                        } catch(Exception ex) {
                            stop();
                        }
                    }
                }
            }
            
        });
        this.writeThread.start();
    }
    
    public void stop() {
        try {
            this.isReadThreadRunning = false;
            this.isWriteThreadRunning = false;
            this.readThread.interrupt();
            this.writeThread.interrupt();
            this.socket.close();
            this.client.getClientManager().remove(this.client.getId());
    		System.out.println("Client disconnected: " + this.client.getClientUsername());
    		PacketQuit packetQuit = Packet.packetQuit;
    		packetQuit.setUsername(this.client.getClientUsername());
    		this.client.sendPacketToAllClients(packetQuit);
        } catch (IOException ex) { }
    }
    
    public void setPacketListener(PacketListener listener) {
    	this.packetListener = listener;
    }
    
    public void sendPacket(Packet packet) {
        this.writeQueue.add(packet);
    }
	
}
