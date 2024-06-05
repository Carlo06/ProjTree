package controller;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    @FXML
    private Button buyTree_btn,back;

    @FXML
    private AnchorPane mainHome;

    @FXML
    private Label money_num,treeplanted_num;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void switchStore(ActionEvent event){
        try {
            if(event.getSource() == buyTree_btn){
                buyTree_btn.getScene().getWindow().hide();
    
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Store.fxml"));
    
                        Stage stage = new Stage();
                        stage.setTitle("Store");
                        stage.setResizable(false);
    
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    public void gobacktoHome(ActionEvent event){
        try {
            if(event.getSource() == back){
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

    public void addTree(){

    }










    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
