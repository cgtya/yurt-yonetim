package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

public class Login {

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameBox;
    @FXML
    private PasswordField passwordBox;
    @FXML
    private Label statusLabel;
    @FXML
    private Label title;

    public void login(ActionEvent event) throws InterruptedException, IOException {
        String username;
        String password;

        try {
            username = usernameBox.getText();
            password = passwordBox.getText();
        } catch (Exception e) {
            statusLabel.setText("Giriş yapılırken bir sorunla karşılaşıldı: " + e);
            statusLabel.setTextFill(Color.RED);
            passwordBox.clear();
            Thread.sleep(500);        //delay to prevent brute force attack
            return;
        }

        //checks passwords and usernames for lengths exceeding 40 characters
        if (username.length()>40 || password.length()>40) {
            statusLabel.setText("Hatalı Kullanıcı Adı/Şifre girişi");
            statusLabel.setTextFill(Color.RED);
            passwordBox.clear();
            Thread.sleep(500);        //delay to prevent brute force attack
            return;
        }

        //hashes passwords for safekeeping
        String passSha256 = DigestUtils.sha256Hex(password);

        //TODO checking credentials

        redirect(event);
    }
    private void redirect(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        Scene scene;

        root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
