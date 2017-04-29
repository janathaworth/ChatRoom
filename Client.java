package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import junit.framework.Test;

public class Client  {
	ArrayList<String> online; 
	int port = 4242;
	
	ListView<String> list;
	private BufferedReader reader; 
	PrintWriter writer;
	TextArea ta;
	TextField  tf;
	String name; 
	String lastClick;
	
	public Client(String name, Stage s) {
		this.name = name; 
		s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
//		writer.println("tparsemet" + name);
//		writer.flush();
		online = new ArrayList<String>(); 
	}
	
	public void reviveClient() {
		Stage s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
//		writer.println("tparsemet" + name);
//		writer.flush();
		online = new ArrayList<String>(); 
	}
	
	public Scene getScene() {
		
		try {
			Socket sock = new Socket("10.146.224.142", 4242); //10.146.204.23
			writer = new PrintWriter(sock.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			Thread readerThread = new Thread(new IncomingReader()); 
			readerThread.start();
			}
		catch (Exception e) {
					e.printStackTrace(); 
		}
		
		// Panel p to hold the label and text field
		// Create UI
		BorderPane border = new BorderPane(); 
		FlowPane pane = new FlowPane();
		FlowPane pane1 = new FlowPane();
		pane1.setHgap(4);
		//pane1.setStyle("-fx-border-color: green");
		pane1.setAlignment(Pos.CENTER);
		tf = new TextField();
		VBox v = new VBox(); 
		ta = new TextArea();
		ta.setPrefRowCount(23);
		ta.setEditable(false);
		TextArea clients = new TextArea(); 
		clients.setEditable(false);
		clients.setPrefColumnCount(14);
		//ScrollPane online = new ScrollPane(clients);
		
//		MenuBar menuBar = new MenuBar(); 
//		ScrollPane online = new ScrollPane();

		 list = new ListView<>();

		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("Everyone");
//		ArrayList<String> users = Server.getUsers(); 
//		for (String name : users) {
//			if (!name.equals(this.name)){
//				items.add(name);
//			}
//		}
		writer.println("users");
		list.setItems(items);
		border.setLeft(list);
		pane.getChildren().add( v);
		
		String receiver = ""; 
		list.addEventHandler(MouseEvent.MOUSE_CLICKED, 
		    new EventHandler<MouseEvent>() { 
	        @Override public void handle(MouseEvent e) {
	        	String thisClick = list.getSelectionModel().getSelectedItem();
	        	lastClick = thisClick; 
	        	//if (lastClick == null || !lastClick.equals(thisClick)) {
	        	if (thisClick != null) {
	        		ta.setText("");
	        		writer.println("update:" + name + ":" + thisClick );
	        		writer.flush(); 
	        		//writer.print( thisClick + "ttt");
	        	}
	        		
	        	//}
	        }
		});
	   
		// handle action event
		tf.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			// send the message to the server
//			writer.println(name + ": " + message);
//			writer.flush();
			playSound();
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			writer.println(lastClick + "~" + name + ": " + message );
			writer.flush(); 
		});
//		ImageView cry = new ImageView(
//			      new Image("http://allenage.com/wp-content/uploads/2016/03/crying-emoji.jpg?15b286")
//		);
		ImageView smileImage = new ImageView(
			      new Image("http://clipart-library.com/images/6Tyoad7Rc.jpg")
		);
		smileImage.setFitWidth(40);
		smileImage.setPreserveRatio(true);
		smileImage.setSmooth(true);
		Button emojiBt = new Button("" , smileImage);
		//emojiBt.setContentDisplay(ContentDisplay.TOP);
		emojiBt.setOnAction(e -> {
			writer.println(smileImage);
			writer.flush(); 
		});
		emojiBt.setStyle("-fx-background-color: transparent;");	//gray shades: http://www.computerhope.com/cgi-bin/htmlcolor.pl?c=C0C0C0
		
		DropShadow shadow = new DropShadow();
		//Adding the shadow when the mouse cursor is on
		emojiBt.addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            emojiBt.setEffect(shadow);
		        }
		});
		//Removing the shadow when the mouse cursor is off
		emojiBt.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            emojiBt.setEffect(null);
		        }
		});
		
		Button sendBt = new Button("Send");
		sendBt.setStyle("-fx-background-color: #55ffff");
		sendBt.setOnAction(e -> {
			//SOUND
//			AudioClip aud = new AudioClip(
//					"https://www.soundjay.com/button/sounds/button-09.mp3");
//		     aud.play();
//			 aud.stop();
			playSound();
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			writer.println(name + ": " + message);
			writer.println("                                       " + timeStamp);
			writer.flush(); 
		});
		
		DropShadow sendShadow = new DropShadow();
		//Adding the shadow when the mouse cursor is on
		sendBt.addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	sendBt.setEffect(shadow);
		        }
		});
	//	Removing the shadow when the mouse cursor is off
		sendBt.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	sendBt.setEffect(null);
		        }
		});
        Label catLabel = new Label("\uD83D\uDC31");
		//String image = "http://allenage.com/wp-content/uploads/2016/03/crying-emoji.jpg?15b286"; 
		pane1.getChildren().addAll(new Label("Enter a message: "), tf, emojiBt, sendBt, catLabel);
	    FlowPane.setMargin(emojiBt,new Insets(0));

		v.getChildren().addAll(new ScrollPane(ta), pane1);
		border.setRight(pane);
		
		return new Scene(border);
	}
	
	
	/*   Play Audio Sounds 	*/	
	public void playSound() {
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
            	AudioClip aud = new AudioClip(
    					"https://www.soundjay.com/button/sounds/button-09.mp3");
    		     aud.play();            
            };
        };
	}

	class IncomingReader implements Runnable {
		ImageView greenIcon = new ImageView( new Image(
				"http://www.clker.com/cliparts/u/g/F/R/X/9/green-circle-hi.png"));
		String message; 
		String receiver = ""; 
		String sender = ""; 
		public void run() {  
			try {
				message = reader.readLine(); 
				String check = "~parseme~" + name;
				while (message != null) {
					//System.out.println(message);
					if (!message.equals(check)) {
						if (message.contains("~parseme~")) {
							String not = message.substring(9);
							if (!online.contains(not)) {
								online.add(not);
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
					                	 list.getItems().add( not);
					                 }
								});
							}
						}
						
						else if (message.contains("~")) {
							//System.out.println(message);
							String[] parts = message.split("~");
							int i = 1; 
							while (parts[i - 1].equals(parts[i])) {
								i++; 
							}
							receiver = parts[i - 1];
							sender = parts[i].split(":")[0];
							if (( lastClick != null && (name.equals(receiver) && lastClick.equals(sender)) )|| name.equals(sender) ) {
								System.out.println(Arrays.toString(parts));
								ta.appendText(parts[i].split("tmstp")[0] + "\n");
//								Text t = new Text();
//						        t.setStyle("-fx-background-color: #DFF2BF;-fx-text-fill: #4F8A10;-fx-font-weight:bold;");
//						        t.setText( parts[i].split("tmstp")[1]+ "\n");
//						        ta.appendText(t.toString());
							}
							if(receiver.equals("Everyone") && !name.equals(sender) && lastClick.equals("Everyone")) {
								ta.appendText(parts[i].split("tmstp")[0] + "\n");
							}
							//System.out.println(Arrays.toString(parts));
						}
//						else if (message.contains("*")) {
//							message = message.substring(1);
//							if (name.equals(message)) {
//								reviveClient();
//							}
//						
//						}
						else if (message.contains("update")) {
							String[] incoming = message.split("::");
							System.out.println(Arrays.toString(incoming));
							System.out.println("length is " + incoming.length);
							String sent = incoming[1];
							String to = incoming[2];
							if (incoming.length <= 3 && name.equals(sent)) {
								ta.setText("");
							}
							else if (name.equals(sent)) {
								ta.appendText(incoming[3] + "\n");
							}
						}
//						else {
//							//System.out.println(receiver);
////							if (receiver.equals(name) && lastClick.equals(sender)) {
////								ta.appendText(message + "\n");
////							}
//							sender = message.split(":")[0];
//							if (sender.equals(name) || lastClick.equals(sender)) {
//								ta.appendText(message + "\n");
//							}
//							
						//}
					}
					System.out.println("received " + message);
					message = reader.readLine(); 
				}
				} 
			catch (IOException ex) { ex.printStackTrace(); }
			}
	}
}
