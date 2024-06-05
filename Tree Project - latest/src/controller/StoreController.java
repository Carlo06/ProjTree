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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StoreController implements Initializable {
    @FXML
    private Button add_balete_btn, add_bamboo_btn, add_banana_btn, add_elemi_btn, add_langsat_btn,
    add_mangove_btn, add_meranti_btn, add_molave_btn, add_narra_btn, back, view_cart;

    @FXML
    private Text Balete_Tree, Bamboo_Tree, Banana_Tree, Elemi_Tree, Langsat_Tree, Mangrove_Tree, Molave_Tree, Narra_Tree, White_Meranti;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void addToCart(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String treeName = "";
        if(clickedButton == add_balete_btn) {
            treeName = "Balete Tree";
        } else if(clickedButton == add_bamboo_btn) {
            treeName = "Bamboo Tree";
        } else if(clickedButton == add_banana_btn) {
            treeName = "Banana Tree";
        } else if(clickedButton == add_elemi_btn) {
            treeName = "Elemi Tree";
        } else if(clickedButton == add_langsat_btn) {
            treeName = "Langsat Tree";
        } else if(clickedButton == add_mangove_btn) {
            treeName = "Mangrove Tree";
        } else if(clickedButton == add_meranti_btn) {
            treeName = "White Meranti";
        } else if(clickedButton == add_molave_btn) {
            treeName = "Molave Tree";
        } else if(clickedButton == add_narra_btn) {
            treeName = "Narra Tree";
        }


        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");
    
            String treeQuery = "SELECT tree_id FROM trees WHERE tree_name = ?";
            prepare = connect.prepareStatement(treeQuery);
            prepare.setString(1, treeName);
            result = prepare.executeQuery();
    
            if (result.next()) {
                int tree_id = result.getInt("tree_id");
    
                String checkTreeQuery = "SELECT * FROM cart WHERE tree_id = ?";
                prepare = connect.prepareStatement(checkTreeQuery);
                prepare.setInt(1, tree_id);
                ResultSet checkResult = prepare.executeQuery();
    
                if (checkResult.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("The " + treeName + " is already added to the cart");
                    alert.showAndWait();
                } else {
                    if(clickedButton == add_balete_btn) {
                        String addTree = "INSERT INTO cart (user_id, tree_id, quantity) VALUES (?,?,?)";
                
                        try {
                            prepare = connect.prepareStatement(addTree);
                            prepare.setString(1, "userID_placeholder"); // Placeholder for user_id
                            prepare.setInt(2, tree_id);
                            prepare.setInt(3, 1);
                
                            prepare.executeUpdate();
                
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText(null);
                            alert.setContentText(treeName + " added to cart!");
                            alert.showAndWait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Tree not found");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


    public void viewCart(ActionEvent event) {

        try {
            view_cart.getScene().getWindow().hide();

            Parent root = FXMLLoader.load(getClass().getResource("/view/cartname.fxml"));


            Stage stage = new Stage();
            stage.setTitle("Cart");
            stage.setResizable(false);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gobacktoHome(ActionEvent event) {
        try {
            if (event.getSource() == back) {
                back.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));

                Stage stage = new Stage();
                stage.setTitle("Home");
                Image icon = new Image("images/Logo.png");
                stage.getIcons().add(icon);
                stage.setResizable(false);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
