package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.commons.codec.digest.DigestUtils;

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

    public void login(ActionEvent event) throws InterruptedException {
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
        //TODO forwarding to correct users page

    }

}
