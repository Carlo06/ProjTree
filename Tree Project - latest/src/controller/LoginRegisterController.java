package controller;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginRegisterController implements Initializable {

    @FXML
    private Button loginBtn, regbutton;

    @FXML
    private AnchorPane login_form, reg_form;

    @FXML
    private PasswordField pass, reg_conpassword, reg_password;

    @FXML
    private Hyperlink createAccount, alreadyhaveAccount;

    @FXML
    private TextField user, reg_email, reg_user;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void switchForm(ActionEvent event) {
        if (event.getSource() == createAccount) {
            reg_form.setVisible(true);
            login_form.setVisible(false);
        } else if (event.getSource() == alreadyhaveAccount) {
            reg_form.setVisible(false);
            login_form.setVisible(true);
        }
    }

    public void registerAccount() {
        String insertData = "INSERT INTO [user] (username, email, password, confirmpassword, date) VALUES (?, ?, ?, ?, ?)";
        String checkUsername = "SELECT username FROM [user] WHERE username = ?";
        String checkEmail = "SELECT email FROM [user] WHERE email = ?";

        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");

            if (reg_user.getText().isEmpty() || reg_password.getText().isEmpty() || reg_email.getText().isEmpty() || reg_conpassword.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            } else {
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
                            prepare = connect.prepareStatement(insertData);
                            prepare.setString(1, reg_user.getText());
                            prepare.setString(2, reg_email.getText());
                            prepare.setString(3, reg_password.getText());
                            prepare.setString(4, reg_conpassword.getText());

                            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
                            prepare.setDate(5, sqlDate);

                            prepare.executeUpdate();

                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully created a new account");
                            alert.showAndWait();

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

    public void loginAccount() {
        String selectData = "SELECT user_id, username, password FROM [user] WHERE username = ? AND password = ?";
        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");

            if (user.getText().isEmpty() || pass.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, user.getText());
                prepare.setString(2, pass.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    int userId = result.getInt("user_id");
                    UserSession.getInstance().setUserId(userId);

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Home");
                    Image icon = new Image("images/Logo.png");
                    stage.getIcons().add(icon);
                    stage.setResizable(false);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    private int getUserIdFromDatabase(String username, String password) {
        int userId = -1;
        try {
            connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true", "sa", "12345");

            String query = "SELECT user_id FROM [user] WHERE username = ? AND password = ?";
            prepare = connect.prepareStatement(query);
            prepare.setString(1, username);
            prepare.setString(2, password);
            result = prepare.executeQuery();

            if (result.next()) {
                userId = result.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
