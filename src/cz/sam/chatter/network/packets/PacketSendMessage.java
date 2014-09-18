package cz.sam.chatter.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketSendMessage extends Packet {
	
	private String message;
	
	public PacketSendMessage(int packetID) {
		super(packetID);
	}

	@Override
	public void onPacketRecive(DataInputStream inputStream) throws Exception {
		this.message = this.readString(inputStream);
	}

	@Override
	public void onPacketSending(DataOutputStream outputStream) throws Exception {
		this.writeString(this.message, outputStream);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
