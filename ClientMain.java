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
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static Map<String, String> names = new HashMap<String, String>();
    
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox box = new VBox(); 
		FlowPane user = new FlowPane();
		FlowPane pass = new FlowPane(); 
		TextField name = new TextField();
		TextField pw = new TextField();
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
		
		login.setOnAction( e -> {
			Client client = new Client(name.getText(), primaryStage);
//			 if (name == null) {
//                 return;
//             }
//             synchronized (names) {
//                 if (!names.containsKey(name)) {
//                	// String pw = "";
//                     //names.put(name.getText(), pw);
//                    // break;
//                 }
//             }
			name.clear();
		});
		
		primaryStage.show();
	}

}
