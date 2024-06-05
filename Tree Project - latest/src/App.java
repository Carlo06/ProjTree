
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(root);
            // Icon
            Image icon = new Image(getClass().getResourceAsStream("images/Logo.png"));
            stage.getIcons().add(icon);
            
            // Disables resizing of window
            stage.setResizable(false);
            stage.setScene(scene);

            // CSS
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());


            // App Title
            stage.setTitle("Pocket Tree's");
            stage.show();

        } 


        catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message dialog
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
