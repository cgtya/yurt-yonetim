package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.corba.yurtyonetim.users.staticgecici;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

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

    @FXML
    private Label databaseStatusLabel;
    @FXML
    private PasswordField databasePasswordBox;
    @FXML
    private TextField databaseUsernameBox;
    @FXML
    private TextField databaseURLBox;

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

    public void databaseConnection(ActionEvent event) {
        //kutucuklar kontrol edilir boş işe varsayılan değerler getirilir, dolu ise yazılan değerler getirilir
        if (databaseURLBox.getText().isEmpty()) {
            staticgecici.setUrl(staticgecici.getUrlDefault());
            staticgecici.setDatabasePassword(staticgecici.getDatabasePasswordDefault());
        } else {
            staticgecici.setUrl(databaseURLBox.getText());
        }

        if (databaseUsernameBox.getText().isEmpty()) {
            staticgecici.setUser(staticgecici.getUserDefault());
        } else {
            staticgecici.setUser(databaseUsernameBox.getText());
        }

        if (databasePasswordBox.getText().isEmpty()) {
            staticgecici.setDatabasePassword(staticgecici.getDatabasePasswordDefault());
        } else {
            staticgecici.setDatabasePassword(databasePasswordBox.getText());
        }

        //veritabanı bağlantısı kontrol edilir
        initialize(null,null);

    }

    //program başlatıldığında veritabanı bağlantısı başarılı şekilde kurulmuş mu diye kontrol edip kullanıcıya geri bildirim verir
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (staticgecici.testDatabaseConnection()) {
            databaseStatusLabel.setText("Veritabanı Bağlantısı: Başarılı");
            databaseStatusLabel.setTextFill(Color.GREEN);
        } else {

            databaseStatusLabel.setText("Veritabanı Bağlantısı: Başarısız");
            databaseStatusLabel.setTextFill(Color.RED);
        }
        statusLabel.setText("");
    }

}
