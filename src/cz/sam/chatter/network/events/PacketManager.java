/*
 * Copyright 2014 Sam.
 */

package cz.sam.chatter.network.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.sam.chatter.network.packets.Packet;


public class PacketManager {
    
    private static List<PacketListener> listeners = new ArrayList<>();
    
    
    public static void registerListener(PacketListener listener) {
        listeners.add(listener);
    }
    
    public static void packetSend(Packet packet) {
        Iterator<PacketListener> i = listeners.iterator();
        while(i.hasNext()) {
            i.next().onPacketSending(packet);
        }
    }
    
    public static void packetRecive(Packet packet) {
        Iterator<PacketListener> i = listeners.iterator();
        while(i.hasNext()) {
            i.next().onPacketRecive(packet);
        }
    }
    
}
