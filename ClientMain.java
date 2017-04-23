package assignment7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private static Map<String, String> names = new HashMap<String, String>();
    
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FlowPane pane = new FlowPane(); 
		TextField name = new TextField();
		Button register = new Button("Register");
		pane.getChildren().addAll(name, register);
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		
		register.setOnAction( e -> {
			Client client = new Client(name.getText());
//			 if (name == null) {
//                 return;
//             }
//             synchronized (names) {
//                 if (!names.containsKey(name)) {
//                	 String pw = "";
//                     names.put(name.getText(), pw);
//                    // break;
//                 }
//             }
			name.clear();
		});
		
		primaryStage.show();
	}

}
