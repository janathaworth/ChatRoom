package assignment7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public static Map<String, String> names = new HashMap<String, String>();
    
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
		box.getChildren().addAll(user, pass, login, register);
		box.setAlignment(Pos.CENTER);
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		Label incorrect = new Label();
		login.setOnAction( e -> {
			Client client = new Client(name.getText(), primaryStage);
             synchronized (names) {
                 if (names.containsKey(name.getText())) {		//username exists
                	 if(names.get(name.getText()) != pw.getText()) {	//password does not match
                		 incorrect.setText("Your password is incorrect!");
                         incorrect.setTextFill(Color.rgb(210, 39, 30));
                         VBox iBox = new VBox(); 
                         iBox.getChildren().add(incorrect);
                	 }
                 }
             }
			name.clear();
		});
		
		primaryStage.show();
	}

}
