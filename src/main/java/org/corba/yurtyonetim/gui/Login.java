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
import org.corba.yurtyonetim.database.*;
import org.corba.yurtyonetim.session.SessionManager;
import org.corba.yurtyonetim.users.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Button loadExampleButton;

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


    public void login(ActionEvent event) throws IOException {
        Manager managerLoggingIn;

        String username;
        String password;
        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan

        username = usernameBox.getText();
        password = passwordBox.getText();


        //checks passwords and usernames for lengths exceeding 40 characters
        if (username.length()>40 || password.length()>40 || !username.matches(tcNoRegex)) {
            loginError();
            return;
        }

        managerLoggingIn = ManagerDAO.getManagerByTc(username);

        if (managerLoggingIn == null) {
            loginError();
            return;
        }

        //hashes passwords for safekeeping
        String passSha256 = DigestUtils.sha256Hex(password);

        if (!managerLoggingIn.getPassword().equals(passSha256)) {
            loginError();
            return;
        }

        SessionManager.setLoggedInManager(managerLoggingIn);

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
            DatabaseConfig.setUrl(DatabaseConfig.getUrlDefault());
            DatabaseConfig.setDatabasePassword(DatabaseConfig.getDatabasePasswordDefault());
        } else {
            DatabaseConfig.setUrl("jdbc:" + databaseURLBox.getText());
        }

        if (databaseUsernameBox.getText().isEmpty()) {
            DatabaseConfig.setUser(DatabaseConfig.getUserDefault());
        } else {
            DatabaseConfig.setUser(databaseUsernameBox.getText());
        }

        if (databasePasswordBox.getText().isEmpty()) {
            DatabaseConfig.setDatabasePassword(DatabaseConfig.getDatabasePasswordDefault());
        } else {
            DatabaseConfig.setDatabasePassword(databasePasswordBox.getText());
        }

        //diğer veritabanı dosyaları güncellenir
        DormDAO.initCredUpdate();
        ManagerDAO.initCredUpdate();
        StudentDAO.initCredUpdate();
        Validation.initCredUpdate();

        //veritabanı bağlantısı kontrol edilir
        initialize(null,null);

    }

    //program başlatıldığında veritabanı bağlantısı başarılı şekilde kurulmuş mu diye kontrol edip kullanıcıya geri bildirim verir
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DatabaseConfig.testDatabaseConnection()) {
            databaseStatusLabel.setText("Veritabanı Bağlantısı: Başarılı");
            databaseStatusLabel.setTextFill(Color.GREEN);
            loginButton.setDisable(false);
            if (DatabaseConfig.isDatabaseCreated()) {
                firstSetup();
            }
        } else {
            databaseStatusLabel.setText("Veritabanı Bağlantısı: Başarısız");
            databaseStatusLabel.setTextFill(Color.RED);
            loginButton.setDisable(true);
        }

    }

    private void loginError() {
        statusLabel.setText("Hatalı Kullanıcı Adı/Şifre girişi");
        statusLabel.setTextFill(Color.RED);
        passwordBox.clear();
        usernameBox.clear();
    }

    private void firstSetup() {
        statusLabel.setText("İlk defa veritabanı oluşturulduğu algılandı! Örnek veri ile çalışmak ister misiniz?\nVeritabanı yeniden oluşturulana dek bir daha sorulmayacaktır!");
        statusLabel.setTextFill(Color.BLACK);

        loadExampleButton.setDisable(false);
        loadExampleButton.setVisible(true);

    }

    public void loadExampleData() {
        statusLabel.setText(DatabaseConfig.importExampleData());
        statusLabel.setTextFill(Color.BLACK);
        loadExampleButton.setDisable(true);
    }

}
