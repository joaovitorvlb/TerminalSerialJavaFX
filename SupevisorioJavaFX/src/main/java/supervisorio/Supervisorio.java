package supervisorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// 

public class Supervisorio extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTelaPrincipal.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		FXMLTelaPrincipalController controller = loader.getController();
		stage.getIcons().add(new Image("supervisorio/communityicon-4v21sx0aiam41.png"));
		stage.setScene(scene);
		stage.setOnHidden(e -> controller.exitApplication());
		stage.show();
	}
}
