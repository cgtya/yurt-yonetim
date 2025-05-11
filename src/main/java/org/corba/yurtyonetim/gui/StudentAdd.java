package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;
import org.corba.yurtyonetim.users.Student;
import org.corba.yurtyonetim.users.staticgecici;

import java.sql.SQLException;

public class StudentAdd extends BaseMenu implements Initializable {
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
    private ChoiceBox<String> dormBox;

    Student student;

    //ChoiceBox un içine elemanları getiren metot
    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle rb) {
        dormBox.getItems().clear();
        dormBox.getItems().addAll(staticgecici.getBosYurtlar());
    }


    public void buttonClickAddStudent(ActionEvent event) {
        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //mail regex
        String telNoRegex = "^5[0-9]{9}$"; // 5 ile başlayan 10 haneli numara

        String tcInput = IDBox.getText();
        String nameInput = nameBox.getText();
        String surnameInput = surnameBox.getText();
        String mailInput = mailBox.getText();
        String phoneInput = phoneBox.getText();

        boolean minLengthCheck = !nameInput.isEmpty() && !surnameInput.isEmpty() && !mailInput.isEmpty() && !phoneInput.isEmpty() && !tcInput.isEmpty();
        boolean maxLengthCheck = nameInput.length()<41 && surnameInput.length()<41 && mailInput.length()<41 && phoneInput.length()<11 && tcInput.length()<12;

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
                alreadyExists = staticgecici.tcKontrol(tcInput);
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
                alreadyExists = staticgecici.epostaKontrol(mailInput);
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
                alreadyExists = staticgecici.telNoKontrol(phoneInput);
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

        String dormInput = dormBox.getValue();

        //veritabanına yükleyen metod için student nesnesi oluşturuldu
        student = new Student(nameInput,surnameInput,tcInput,phoneInput,mailInput,dormInput,0,false);

        //student nesnesi veritabanına yüklenir

        String message = staticgecici.addStudentStatic(student);

        statusLabel.setText(message);
        statusLabel.setTextFill(darkModeDefTextColor());


        //eğer yerleştirilen öğrenci yurttaki son kontenjanı dolduruyorsa yurdun müsait yurtlardan çıkarılması için
        initialize(null,null);

    }


}
