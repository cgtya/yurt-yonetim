package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;
import org.apache.commons.codec.digest.DigestUtils;
import org.corba.yurtyonetim.database.ManagerDAO;
import org.corba.yurtyonetim.database.Validation;
import org.corba.yurtyonetim.users.Manager;

import java.sql.SQLException;

public class AddManager extends BaseMenu {
    @FXML
    private TextField nameBox;
    @FXML
    private TextField surnameBox;
    @FXML
    private TextField mailBox;
    @FXML
    private TextField phoneBox;
    @FXML
    private TextField IDBox;
    @FXML
    private Label statusLabel;
    @FXML
    private PasswordField passwordBox1;
    @FXML
    private PasswordField passwordBox2;


    Manager manager;

    public void buttonClickAddManager(ActionEvent event) {
        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //mail regex
        String telNoRegex = "^5[0-9]{9}$"; // 5 ile başlayan 10 haneli numara

        String tcInput = IDBox.getText();
        String nameInput = nameBox.getText();
        String surnameInput = surnameBox.getText();
        String mailInput = mailBox.getText();
        String phoneInput = phoneBox.getText();
        String passwordInput1 = passwordBox1.getText();
        String passwordInput2 = passwordBox2.getText();

        boolean minLengthCheck = !nameInput.isEmpty() && !surnameInput.isEmpty() && !mailInput.isEmpty() && !phoneInput.isEmpty() && !tcInput.isEmpty() && !passwordBox1.getText().isEmpty() && !passwordBox2.getText().isEmpty();
        boolean maxLengthCheck = nameInput.length()<41 && surnameInput.length()<41 && mailInput.length()<41;

        //karakter uzunluğu kontrolleri
        if (!minLengthCheck) {
            statusLabel.setText("Lütfen tüm kutucukları doldurunuz.");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (!maxLengthCheck) {
            statusLabel.setText("Maksimum karakter sınırı aşıldı!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        //geçerli ve özgün tc kimlik kontrolü
        if (!tcInput.matches(tcNoRegex)) {
            statusLabel.setText("Geçersiz TC Kimlik No! 11 haneli olmalı ve 0 ile başlamamalı.");
            statusLabel.setTextFill(Color.RED);
            IDBox.clear();
            return;
        } else {

            boolean alreadyExists;

            //veritabanı erişiminde sorun yaşanması durumu için kontrol
            try {
                alreadyExists = Validation.tcKontrol(tcInput);
            } catch (SQLException e) {
                statusLabel.setText("Veritabanı erişiminde sorun yaşandı: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
                return;
            }

            if (alreadyExists) {
                statusLabel.setText("Girdiğiniz kimlik numarasına sahip bir üye zaten vardır!");
                statusLabel.setTextFill(Color.RED);
                IDBox.clear();
                return;
            }

        }

        //geçerli ve özgün mail kontrolü
        if (!mailInput.matches(emailRegex)) {
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
        if (!phoneInput.matches(telNoRegex)) {
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

        if (!passwordInput1.equals(passwordInput2)) {
            statusLabel.setText("Şifreler eşleşmiyor.");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        String passSha256 = DigestUtils.sha256Hex(passwordInput1);

        manager = new Manager(nameInput,surnameInput,tcInput,phoneInput,mailInput,passSha256);

        //manager nesnesi veritabanına yüklenir
        String message = ManagerDAO.addManagerStatic(manager);

        statusLabel.setText(message);
        statusLabel.setTextFill(darkModeDefTextColor());

    }


}
