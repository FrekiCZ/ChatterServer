package cz.sam.chatter.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketQuit extends Packet {
	
	private String username;
	
	public PacketQuit(int packetID) {
		super(packetID);
	}

	@Override
	public void onPacketRecive(DataInputStream inputStream) throws Exception {
		this.username = this.readString(inputStream);
	}

	@Override
	public void onPacketSending(DataOutputStream outputStream) throws Exception {
		this.writeString(this.username, outputStream);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}