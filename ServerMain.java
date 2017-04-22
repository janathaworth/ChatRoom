package assignment7;

public class ServerMain {

	public static void main(String[] args) {
		try {
			 new Server().setUp(); 
		}
		catch( Exception e) {
			e.printStackTrace(); 
		}

	}

}

