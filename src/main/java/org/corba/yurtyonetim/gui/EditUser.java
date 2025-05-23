package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.apache.commons.codec.digest.DigestUtils;
import org.corba.yurtyonetim.database.ManagerDAO;
import org.corba.yurtyonetim.database.Validation;
import org.corba.yurtyonetim.session.SessionManager;
import org.corba.yurtyonetim.users.Manager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditUser extends BaseMenu implements Initializable {
    @FXML
    private TextField tcBox;
    @FXML
    private Label statusLabel;

    @FXML
    private TextField nameBox;
    @FXML
    private TextField surnameBox;
    @FXML
    private TextField mailBox;
    @FXML
    private TextField phoneBox;
    @FXML
    private PasswordField passwordBox1;
    @FXML
    private PasswordField passwordBox2;



    private Manager loggedInManager = SessionManager.getLoggedInManager();

    public void saveButtonClick(ActionEvent event) {
        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());

        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //mail regex
        String telNoRegex = "^5[0-9]{9}$"; // 5 ile başlayan 10 haneli numara

        String nameInput = nameBox.getText();
        String surnameInput = surnameBox.getText();
        String mailInput = mailBox.getText();
        String phoneInput = phoneBox.getText();
        String passwordInput1 = passwordBox1.getText();
        String passwordInput2 = passwordBox2.getText();

        //girişler geçerli mi diye kontroller
        if (nameInput.isEmpty() || surnameInput.isEmpty() || mailInput.isEmpty() || phoneInput.isEmpty()) {
            statusLabel.setText("Boş kutucuk bırakmayınız!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (nameInput.length()>41 || surnameInput.length()>41) {
            statusLabel.setText("40 karakteri geçmeyiniz!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (mailInput.equals(loggedInManager.getEposta())) {

        } else if (!mailInput.matches(emailRegex)) {
            statusLabel.setText("Geçersiz e-posta!");
            statusLabel.setTextFill(Color.RED);
            mailBox.clear();
            return;
        } else {
            boolean alreadyExists;

            //veritabanı erişiminde sorun yaşanması durumu için kontrol
            try {
                alreadyExists = Validation.epostaKontrol(mailInput);
            } catch (SQLException e) {
                statusLabel.setText("Veritabanı erişiminde sorun yaşandı: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
                return;
            }

            if (alreadyExists) {
                statusLabel.setText("Girdiğiniz e-postaya sahip bir üye zaten vardır!");
                statusLabel.setTextFill(Color.RED);
                mailBox.clear();
                return;
            }

        }

        //geçerli ve özgün telefon kontrolü
        if (phoneInput.equals(loggedInManager.getTelNo())) {

        }  else if (!phoneInput.matches(telNoRegex)) {
            statusLabel.setText("Geçersiz telefon numarası! 5 ile başlamalı ve 10 haneli olmalı.");
            statusLabel.setTextFill(Color.RED);
            phoneBox.clear();
            return;
        } else {
            boolean alreadyExists;

            //veritabanı erişiminde sorun yaşanması durumu için kontrol
            try {
                alreadyExists = Validation.telNoKontrol(phoneInput);
            } catch (SQLException e) {
                statusLabel.setText("Veritabanı erişiminde sorun yaşandı: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
                return;
            }

            if (alreadyExists) {
                statusLabel.setText("Girdiğiniz telefon numarasına sahip bir üye zaten vardır!");
                statusLabel.setTextFill(Color.RED);
                phoneBox.clear();
                return;
            }
        }

        if (!(passwordInput1.isEmpty() && passwordInput2.isEmpty())) {
            if (passwordInput1.equals(passwordInput2)) {
                loggedInManager.setPassword(DigestUtils.sha256Hex(passwordInput1));
            } else {
                statusLabel.setText("Şifreler eşleşmiyor.");
                statusLabel.setTextFill(Color.RED);
                return;
            }
        }

        loggedInManager.setName(nameInput);
        loggedInManager.setSurname(surnameInput);
        loggedInManager.setEmail(mailInput);
        loggedInManager.setTelNo(phoneInput);

        String message = ManagerDAO.updateManagerInDatabase(loggedInManager);

        statusLabel.setText(message);
        statusLabel.setTextFill(darkModeDefTextColor());

    }

    public void deleteButtonClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("UYARI");
        alert.setHeaderText("Şu an giriş yapmış olan hesabı silmek üzeresiniz.");
        alert.setContentText("Hesabı silmek istediğinizden emin misiniz? (Silme işlemi sonrası hesaptan çıkış yapılacaktır!)");

        //onay kutucuğu
        if (alert.showAndWait().get() == ButtonType.OK) {
            //silme metodu çağırılır mesaj bildirim olarak gösteriilir
            logoutNoConf(event, ManagerDAO.deleteManager(loggedInManager.getTcNo()));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameBox.setText(loggedInManager.getName());
        surnameBox.setText(loggedInManager.getSurname());
        mailBox.setText(loggedInManager.getEposta());
        phoneBox.setText(loggedInManager.getTelNo());
        tcBox.setText(loggedInManager.getTcNo());
    }
}
