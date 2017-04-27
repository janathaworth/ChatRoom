package assignment7;

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
    
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox box = new VBox(); 
		FlowPane user = new FlowPane();
		FlowPane pass = new FlowPane(); 
		TextField name = new TextField();
		PasswordField pw = new PasswordField();
		user.getChildren().addAll(new Label("Username: "), name);
		pass.getChildren().addAll(new Label("Password:  "), pw );
		user.setAlignment(Pos.CENTER);
		pass.setAlignment(Pos.CENTER);
		
		Button login = new Button("Log In");
		Hyperlink register = new Hyperlink("Create Account");
		Label incorrect = new Label();
		box.getChildren().addAll(user, pass, login, register, incorrect);
		box.setAlignment(Pos.CENTER);
		box.setSpacing(10);
		VBox.setMargin(user, new Insets(20, 0, 0, 0));
		VBox.setMargin(login, new Insets(0, 0, 0, 40));
		VBox.setMargin(register, new Insets(0, 0, 0, 42));
		VBox.setMargin(incorrect, new Insets(0, 0, 0, 42));
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		
		EventHandler log = new EventHandler<ActionEvent> () {
			@Override public void handle(ActionEvent e) {
				synchronized (Server.names) {
	                 if (Server.names.containsKey(name.getText())) {			//username exists
	                	 if(Server.names.get(name.getText()) != pw.getText()) {	//password does not match
	                		 incorrect.setText("Your password is incorrect!");
	                         incorrect.setTextFill(Color.rgb(210, 39, 30));
	                	 }
	                	 else {
	                		 Client client = new Client(name.getText(), primaryStage);
	                	 }
	                 }
	                 else{
	                	 incorrect.setText("Incorrect Username!");
	                	 incorrect.setTextFill(Color.rgb(210, 39, 30));
	                 }
	             }
	             
				name.clear();
				pw.clear(); 
			}
		};
		
		login.setOnAction(log);
		pw.setOnAction(log);
		
		
		register.setOnAction( e -> {
			
			VBox box2 = new VBox(); 
			FlowPane user2 = new FlowPane();
			FlowPane pass2 = new FlowPane(); 
			FlowPane pass3 = new FlowPane(); 
			TextField name2 = new TextField();
			PasswordField pw2 = new PasswordField();
			PasswordField pw3 = new PasswordField();
			user2.getChildren().addAll(new Label("Username: "), name2);
			pass2.getChildren().addAll(new Label("Password:  "), pw2 );
			pass3.getChildren().addAll(new Label("Re-Enter Password: "), pw3 );
			user2.setAlignment(Pos.CENTER);
			pass2.setAlignment(Pos.CENTER);
			//pass3.setAlignment(Pos.CENTER);
			Button reg = new Button("Register");
			Label incorrect2 = new Label();
			box2.getChildren().addAll(user2, pass2, pass3, reg, incorrect2);
			box2.setAlignment(Pos.CENTER);
			box2.setSpacing(10);
			VBox.setMargin(user2, new Insets(20, 0, 0, 0));
			VBox.setMargin(reg, new Insets(0, 0, 0, 50));
			VBox.setMargin(incorrect2, new Insets(0, 0, 0, 46));
			VBox.setMargin(pass3, new Insets(0, 0, 0, 54));
			
			Scene newScene = new Scene(box2); 
			primaryStage.setScene(newScene);
			
			EventHandler click = new EventHandler<ActionEvent> () {
				@Override public void handle(ActionEvent e) {
					synchronized (Server.names) {
		                 if (Server.names.containsKey(name2.getText())) {			
		                		incorrect2.setText("Username already taken!");
		                        incorrect2.setTextFill(Color.rgb(210, 39, 30));
		                 }
		                 else if (name2.getText().equals("")) {
		                	 incorrect2.setText("Please enter a username");
	                         incorrect2.setTextFill(Color.rgb(210, 39, 30));
		                 }
		                 else if (pw2.getText().equals("")) {
		                	 incorrect2.setText("Please enter a password");
	                         incorrect2.setTextFill(Color.rgb(210, 39, 30));
		                 }
		                 else if(!pw2.getText().equals(pw3.getText())) {
		                	 incorrect2.setText("Passwords do not match. Please try again.");
	                         incorrect2.setTextFill(Color.rgb(210, 39, 30));
		                 }
		                 else{
		                	 Server.names.put(name2.getText(), pw2.getText());	//add new user
		                	 Client client = new Client(name2.getText(), primaryStage);
		                 }
		             }
		             
					name2.clear();
					pw2.clear(); 
					pw3.clear(); 
	            }
			};
			
			reg.setOnAction(click);
			pw3.setOnAction(click);

		});
		
		
		primaryStage.show();
	}

}
