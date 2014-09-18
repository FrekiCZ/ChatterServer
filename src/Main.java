import cz.sam.chatter.ChatterServer;


public class Main {

	public static void main(String[] args) {
		ChatterServer server = new ChatterServer();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
