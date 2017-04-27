package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client  {
	int port = 4242;
	
	ListView<String> list;
	private BufferedReader reader; 
	private PrintWriter writer;
	TextArea ta;
	TextField  tf;

	String name; 
	
	public Client(String name, Stage s) {
		this.name = name; 
		s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
		writer.println("tparsemet" + name);
		writer.flush();
	}
	
	public Scene getScene() {
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
		
		MenuBar menuBar = new MenuBar(); 
		ScrollPane online = new ScrollPane();
		
		 list = new ListView<>();

		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("Online Users:");
		for (String name : Server.names.keySet()) {
			if (!name.equals(this.name)){
				items.add(name);
				Menu menu1 = new Menu(name);
				menuBar.getMenus().add(menu1);
				clients.appendText(name + " is online\n");
			}
		}
		list.setItems(items);
		online.setContent(menuBar);
		border.setLeft(list);
		//border.setBottom(pane1);
		pane.getChildren().add( v);
		// handle action event
		tf.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			// send the message to the server
			writer.println(name + ": " + message);
			writer.flush();
		});
		Button sendBt = new Button("Send");
		sendBt.setStyle("-fx-background-color: #55ffff");
		sendBt.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			writer.println(name + ": " +  message);
			//System.out.println(message);
			writer.flush(); 
		});
		
		
		//String image = "http://allenage.com/wp-content/uploads/2016/03/crying-emoji.jpg?15b286"; 
		//Button emoji = new Button();
		//emoji.setGraphic(new ImageView(image));
		pane1.getChildren().addAll(new Label("Enter a message: "), tf, sendBt);
		v.getChildren().addAll(new ScrollPane(ta), pane1);
		border.setRight(pane);
		Socket sock;
		try {
			sock = new Socket("127.0.0.1", 4242);
			writer = new PrintWriter(sock.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			Thread readerThread = new Thread(new IncomingReader()); 
			readerThread.start();
			}
		catch (Exception e) {
					e.printStackTrace(); 
		}
		return new Scene(border);
	}
		
	class IncomingReader implements Runnable {
		String message; 
		public void run() { 
			try {
				
				message = reader.readLine(); 
				String check = "tparsemet" + name;
				while (message != null) {
					//System.out.println(message);
					if (!message.equals(check)) {
						if (message.contains("tparsemet")) {
							message = message.substring(9);
							Platform.runLater(new Runnable() {
				                 @Override public void run() {
				                	
				                	 list.getItems().add(message);
				                 }
							});
						}
						else {
							ta.appendText(message + "\n");
						}
					}
					message = reader.readLine(); 
				}
			} 
			catch (IOException ex) { ex.printStackTrace(); }
			}
	}
}
