/* CHATROOM ClientObserver.java
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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer{
	public ClientObserver(OutputStream out) {
		super(out);
	}
	
	@Override 
	public void update(Observable o, Object arg) {
		this.println(arg);
		this.flush();
	}


}