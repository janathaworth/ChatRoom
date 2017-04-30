/* CHATROOM ServerMain.java
 * EE422C Project 7 submission by
 * Janat Haworth
 * jlh6554
 * 16235
 * Connie Jehng
 * cj23478
 * 16235
 * Slip days used: 1
 * Spring 2017
 */

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

