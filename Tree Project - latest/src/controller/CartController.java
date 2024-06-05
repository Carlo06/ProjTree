package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class CartController implements Initializable {

    @FXML
    private ListView<String> cartListView; // List view displaying items in the cart
    @FXML
    private Button checkoutBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void checkout(ActionEvent event) {
        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");

            String insertOrder = "INSERT INTO orders (user_id) VALUES (?)";
            prepare = connect.prepareStatement(insertOrder);
            prepare.setInt(1, UserSession.getInstance().getUserId());
            prepare.executeUpdate();

            String clearCart = "DELETE FROM cart WHERE user_id = ?";
            prepare = connect.prepareStatement(clearCart);
            prepare.setInt(1, UserSession.getInstance().getUserId());
            prepare.executeUpdate();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Checkout successful!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load items from cart into cartListView
        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");

            String cartQuery = "SELECT t.tree_name FROM cart c JOIN trees t ON c.tree_id = t.tree_id WHERE c.user_id = ?";
            prepare = connect.prepareStatement(cartQuery);
            prepare.setInt(1, UserSession.getInstance().getUserId());
            result = prepare.executeQuery();

            while (result.next()) {
                cartListView.getItems().add(result.getString("tree_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
