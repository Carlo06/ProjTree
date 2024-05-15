package controller;

import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginRegisterController implements Initializable {

    @FXML
    private Button loginBtn,regbutton;

    @FXML
    private AnchorPane login_form,reg_form;

    @FXML
    private PasswordField pass;

    @FXML
    private Hyperlink createAccount,alreadyhaveAccount;

    @FXML
    private TextField reg_conpassword;

    @FXML
    private TextField reg_email;

    @FXML
    private TextField reg_password;

    @FXML
    private TextField reg_user;

    @FXML
    private TextField user;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;


    public void switchForm(ActionEvent event){

        if(event.getSource() == createAccount){
            reg_form.setVisible(true);
            login_form.setVisible(false);
        }else if(event.getSource() == alreadyhaveAccount){
            reg_form.setVisible(false);
            login_form.setVisible(true);
        }
    }

    public void registerAccount() {
        String insertData = "INSERT INTO [user] (username, email, password, confirmpassword, date) VALUES (?, ?, ?, ?, ?)";
        String checkUsername = "SELECT username FROM [user] WHERE username = ?";
        String checkEmail = "SELECT email FROM [user] WHERE email = ?";

        try {
            // Establish connection to the MS SQL Server database
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;integratedSecurity=true;encrypt=true;trustServerCertificate=true");

            if (reg_user.getText().isEmpty() || reg_password.getText().isEmpty() || reg_email.getText().isEmpty() || reg_conpassword.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            } else {
                // Check if the username already exists
                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, reg_user.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(reg_user.getText() + " was already taken");
                    alert.showAndWait();
                } else {
                    // Check if the email already exists
                    prepare = connect.prepareStatement(checkEmail);
                    prepare.setString(1, reg_email.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText(reg_email.getText() + " was already taken");
                        alert.showAndWait();
                    } else {
                        // Check password length and confirm password
                        if (reg_password.getText().length() < 8) {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid Password, at least 8 characters needed");
                            alert.showAndWait();
                        } else if (!reg_conpassword.getText().equals(reg_password.getText())) {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Passwords do not match");
                            alert.showAndWait();
                        } else {
                            // Insert the new user into the database
                            prepare = connect.prepareStatement(insertData);
                            prepare.setString(1, reg_user.getText());
                            prepare.setString(2, reg_email.getText());
                            prepare.setString(3, reg_password.getText());
                            prepare.setString(4, reg_conpassword.getText());

                            // Set the current date as the registration date
                            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                            prepare.setDate(5, sqlDate);

                            // Execute the insertion
                            prepare.executeUpdate();

                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully created a new account");
                            alert.showAndWait();

                            // Clear the text fields
                            reg_user.clear();
                            reg_email.clear();
                            reg_password.clear();
                            reg_conpassword.clear();

                            reg_form.setVisible(false);
                            login_form.setVisible(true);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loginAccount(){
        String selecData = "SSELECT username, password FROM [user] WHERE username = '"
                + user.getText() + "' and password = '" + pass.getText() + "'";
        String checkUsername = "SELECT username FROM [user] WHERE username = ?";

                try {
                    connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;integratedSecurity=true;encrypt=true;trustServerCertificate=true");

                    if(user.getText().isEmpty() || pass.getText().isEmpty()){
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all blank fields");
                        alert.showAndWait();
                    }else{
                        prepare = connect.prepareStatement(checkUsername);
                        prepare.setString(1, user.getText());
                        result = prepare.executeQuery();
                        if(result.next()){

                            data.user = user.getText();

                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully login");
                            alert.showAndWait();

                            loginBtn.getScene().getWindow().hide();

                            Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));

                            Stage stage = new Stage();
                            stage.setTitle("POCKET TREE");

                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        }else{
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Incorrect Username/Password");
                            alert.showAndWait();
                        }
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
