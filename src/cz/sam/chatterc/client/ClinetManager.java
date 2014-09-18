package cz.sam.chatterc.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ClinetManager {
	
	private Map<Integer, Client> clients = new HashMap<Integer, Client>();
	
	public ClinetManager() {
		
	}
	
	public void add(Client client) {
		int id = 0;
		while(this.clients.containsKey(id)) {
			id++;
		}
		this.clients.put(id, client);
		client.setID(id);
	}
	
	public void remove(int id) {
		this.clients.remove(id);
	}
	
	public void stopALL() {
		for(Entry<Integer, Client> e : clients.entrySet()) {
			e.getValue().stop();
		}
	}
	
	public Set<Entry<Integer, Client>> getAllClients() {
		return this.clients.entrySet();
	}
	
}
