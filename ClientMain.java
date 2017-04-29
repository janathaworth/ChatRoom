package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import assignment7.Client.IncomingReader;
import javafx.application.Application;
import javafx.application.Platform;
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

//label and ~~ and try catch

public class ClientMain extends Application {
	PrintWriter writer;
	private BufferedReader reader;
	Label incorrect;
	Label incorrect2;

	Boolean processed;

	Map<String, String> userList;

	TextField name2;
	PasswordField pw2;
	PasswordField pw3;
	TextField name; // login
	PasswordField pw; // login
	Stage primaryStage;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
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

				// synchronized (Server.names) {
				// name.clear();
				// pw.clear();
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
			// pass3.setAlignment(Pos.CENTER);
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
						processed = true;
						message = message.substring(12);
						System.out.println("sub" + message);
						String name = message.split(" ")[0];
						String password = message.split(" ")[1];
						if (!userList.containsKey(name)) {
							userList.put(name, password);
						}

						// name2.clear();
						// pw2.clear();
						// pw3.clear();

					}
					if (message.contains("1parse1")) {
						if (userList.containsKey(name2.getText())) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									incorrect2.setText("Username already taken!");
									incorrect2.setTextFill(Color.rgb(210, 39, 30));
								}
							});
						}

						else if (name2.getText().equals("") || name2.getText().contains(" ")
								|| name2.getText().contains("~")) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									incorrect2.setText("Invalid Username");
									incorrect2.setTextFill(Color.rgb(210, 39, 30));
								}
							});

						} else if (pw2.getText().equals("") || pw2.getText().contains(" ")) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									incorrect2.setText("Invalid Password");
									incorrect2.setTextFill(Color.rgb(210, 39, 30));
								}
							});

						} else if (!pw2.getText().equals(pw3.getText())) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									incorrect2.setText("Passwords do not match. Please try again.");
									incorrect2.setTextFill(Color.rgb(210, 39, 30));
								}
							});

						} else {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									incorrect2.setText("");
									Client client = new Client(name2.getText(), primaryStage);
									client.writer.println("~parseme~" + name2.getText() + " " + pw2.getText());
									client.writer.flush();
								}
							});

						}
					}
					/* Log In Page */
					// username exists
					// //Server.names.containsKey(name.getText())
					// password does not match //Server.names.get(name.getText()
					// != pw.getText()
					if (message.contains("2parse2")) {
						if (userList.containsKey(name.getText())) {
							if (!(userList.get(name.getText()).equals(pw.getText()))) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										incorrect.setText("Your password is incorrect!");
										incorrect.setTextFill(Color.rgb(210, 39, 30));
									}
								});

							} else {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										incorrect.setText("");
										Client client = new Client(name.getText(), primaryStage);
									}
								});
							}
						} else {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									
									incorrect.setText("Incorrect Username!");
									incorrect.setTextFill(Color.rgb(210, 39, 30));
								}
							});
						}
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
