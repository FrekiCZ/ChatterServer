package cz.sam.chatter.network.events;

import cz.sam.chatter.network.packets.Packet;


public interface PacketListener {
    
    void onPacketRecive(Packet packet);
    
    void onPacketSending(Packet packet);
    
}
