/* CHATROOM ClientMain.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ClientMain extends Application {
	Map<String, String> userList;
	PrintWriter writer;
	private BufferedReader reader;
	Boolean waiting;

	TextField name; // login
	PasswordField pw; // login
	TextField name2; //reg
	PasswordField pw2; //reg
	PasswordField pw3;
	Label incorrect;
	Label incorrect2;
	Stage primaryStage;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		waiting = true; 
		this.primaryStage = primaryStage;
		VBox box = new VBox();
		FlowPane user = new FlowPane();
		FlowPane pass = new FlowPane();
		name = new TextField();
		pw = new PasswordField();
		user.getChildren().addAll(new Label("Username: "), name);
		pass.getChildren().addAll(new Label("Password:  "), pw);
		user.setAlignment(Pos.CENTER);
		pass.setAlignment(Pos.CENTER);

		Button login = new Button("Log In");
		Hyperlink register = new Hyperlink("Create Account");
		incorrect = new Label();
		box.getChildren().addAll(user, pass, login, register, incorrect);
		box.setAlignment(Pos.CENTER);
		box.setSpacing(10);
		VBox.setMargin(user, new Insets(20, 0, 0, 0));
		VBox.setMargin(login, new Insets(0, 0, 0, 40));
		VBox.setMargin(register, new Insets(0, 0, 0, 42));
		VBox.setMargin(incorrect, new Insets(0, 0, 0, 42));
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);

		userList = new HashMap<String, String>();
		try {
			Socket sock = new Socket("10.146.239.174", 4242); // 10.146.204.23
			writer = new PrintWriter(sock.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventHandler log = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("logging");
				writer.println("userspass log");
				writer.flush();

				while (waiting) {

				}

				if (userList.containsKey(name.getText())) {
					if (!(userList.get(name.getText()).equals(pw.getText()))) {
						incorrect.setText("Your password is incorrect!");
						incorrect.setTextFill(Color.rgb(210, 39, 30));

					} 
					else {
						incorrect.setText("");
						Client client = new Client(name.getText(), primaryStage);
						client.writer.println("*" + name.getText());
						client.writer.flush();

					}
				} 
				else {
					incorrect.setText("Incorrect Username!");
					incorrect.setTextFill(Color.rgb(210, 39, 30));

				}
			}
		};

		login.setOnAction(log);
		pw.setOnAction(log);

		register.setOnAction(e -> {

			VBox box2 = new VBox();
			FlowPane user2 = new FlowPane();
			FlowPane pass2 = new FlowPane();
			FlowPane pass3 = new FlowPane();
			name2 = new TextField();
			pw2 = new PasswordField();
			pw3 = new PasswordField();
			user2.getChildren().addAll(new Label("Username: "), name2);
			pass2.getChildren().addAll(new Label("Password:  "), pw2);
			pass3.getChildren().addAll(new Label("Re-Enter Password: "), pw3);
			user2.setAlignment(Pos.CENTER);
			pass2.setAlignment(Pos.CENTER);
			Button reg = new Button("Register");
			incorrect2 = new Label();
			box2.getChildren().addAll(user2, pass2, pass3, reg, incorrect2);
			box2.setAlignment(Pos.CENTER);
			box2.setSpacing(10);
			VBox.setMargin(user2, new Insets(20, 0, 0, 0));
			VBox.setMargin(reg, new Insets(0, 0, 0, 50));
			VBox.setMargin(incorrect2, new Insets(0, 0, 0, 46));
			VBox.setMargin(pass3, new Insets(0, 0, 0, 54));

			Scene newScene = new Scene(box2);
			primaryStage.setScene(newScene);

			EventHandler click = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					System.out.println("clicked");
					writer.println("userspass reg");
					writer.flush();
					
					while(waiting) {
						
					}

					if (userList.containsKey(name2.getText())) {
						incorrect2.setText("Username already taken!");
						incorrect2.setTextFill(Color.rgb(210, 39, 30));
					}

					else if (name2.getText().equals("") || name2.getText().contains(" ")
							|| name2.getText().contains("~") || name2.getText().equals("Everyone")) {

						incorrect2.setText("Invalid Username");
						incorrect2.setTextFill(Color.rgb(210, 39, 30));

					} 
					else if (pw2.getText().equals("") || pw2.getText().contains(" ")) {
						incorrect2.setText("Invalid Password");
						incorrect2.setTextFill(Color.rgb(210, 39, 30));

					} 
					else if (!pw2.getText().equals(pw3.getText())) {
						incorrect2.setText("Passwords do not match. Please try again.");
						incorrect2.setTextFill(Color.rgb(210, 39, 30));

					} 
					else {
						incorrect2.setText("");
						Client client = new Client(name2.getText(), primaryStage);
						client.writer.println("~parseme~" + name2.getText() + " " + pw2.getText());
						client.writer.flush();
					}
				}
			};
			reg.setOnAction(click);
			pw3.setOnAction(click);

		});

		primaryStage.show();
	}

	class IncomingReader implements Runnable {
		String message;

		@Override
		public void run() {
			try {
				message = reader.readLine();
				while (message != null) {
					if (message.contains("1parseme1")) {
						message = message.substring(12);
						System.out.println("sub" + message);
						String name = message.split(" ")[0];
						String password = message.split(" ")[1];
						if (!userList.containsKey(name)) {
							userList.put(name, password);
						}
					}
					if (message.contains("1parse1")) {
						waiting = false; 				
					}
					/* Log In Page */
					
					if (message.contains("2parse2")) {
						waiting = false; 
					}
					System.out.println("main received" + message);
					message = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
