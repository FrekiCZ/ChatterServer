package cz.sam.chatter.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import cz.sam.chatter.network.TCPConnection;

public abstract class Packet {
    
    public static Packet[] packets = new Packet[256];
    
    public static PacketLogin packetLogin = new PacketLogin(0);
    public static PacketQuit packetQuit = new PacketQuit(1);
    public static PacketSendMessage packetMessage = new PacketSendMessage(2);
    
    private int packetID;
    
    public Packet(int packetID) {
        if(Packet.packets[packetID] != null) {
            try {
                throw new Exception("Packet with this id (" + packetID + ") is already exist");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.packetID = packetID;
            Packet.packets[packetID] = this;
        }
    }
    
    public void writeString(String par1String, DataOutputStream outputStream) throws Exception {
        outputStream.writeShort(par1String.length());
        outputStream.writeChars(par1String);
    }
    
    public String readString(DataInputStream inputStream) throws Exception {
        int size = inputStream.readShort();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            sb.append(inputStream.readChar());
        }
        return sb.toString();
    }
    
    public abstract void onPacketRecive(DataInputStream inputStream) throws Exception;
    public abstract void onPacketSending(DataOutputStream outputStream) throws Exception;
    
    public static void sendPacket(Packet packet, TCPConnection connection) throws Exception {
    	connection.output.write(packet.getPacketID());
        packet.onPacketSending(connection.output);
        connection.packetListener.onPacketSending(packet);
    }
    
    public static void recivePackets(TCPConnection connection) throws Exception {
        int packetID = connection.input.read();
        if(Packet.packets[packetID] != null) {
            Packet packet = Packet.packets[packetID];
            packet.onPacketRecive(connection.input);
            connection.packetListener.onPacketRecive(packet);
        } else {
            throw new Exception("Packet with this id (" + packetID + ") not exist !");
        }
    }
    
    public int getPacketID() {
        return this.packetID;
    }
    
}