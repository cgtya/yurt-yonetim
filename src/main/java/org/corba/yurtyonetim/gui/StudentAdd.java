package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;
import org.corba.yurtyonetim.users.Manager;

public class StudentAdd extends BaseMenu {
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
        boolean maxLengthCheck = nameInput.length()<41 && surnameInput.length()<41 && mailInput.length()<41 && phoneInput.length()<11 && tcInput.length()<11;

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

        //geçerli tc kimlik kontrolü TODO aktif edilecek
        if (!tcInput.matches(tcNoRegex)) {
            statusLabel.setText("Geçersiz TC Kimlik No! 11 haneli olmalı ve 0 ile başlamamalı.");
            statusLabel.setTextFill(Color.RED);
            IDBox.clear();
            return;
        } else {
            /*
            if (Manager.tcKontrol(tcInput)) {
                statusLabel.setText("Girdiğiniz kimlik numarasına sahip bir öğrenci zaten vardır!");
                statusLabel.setTextFill(Color.RED);
                IDBox.clear();
                return;
            }
            */
        }
        //geçerli mail kontrolü TODO aktif edilecek
        if (!mailInput.matches(emailRegex)) {
            statusLabel.setText("Geçersiz e-posta!");
            statusLabel.setTextFill(Color.RED);
            mailBox.clear();
            return;
        } else {
            /*
            if(Manager.epostakontrol(mailInput)){
                statusLabel.setText("Aradığınız E-Posta adresine sahip bir öğrenci zaten vardır.");
                mailBox.clear();
                return;
            }
            */
        }

        //geçerli telefon kontrolü TODO aktif edilecek
        if (!phoneInput.matches(telNoRegex)) {
            statusLabel.setText("Geçersiz telefon numarası! 5 ile başlamalı ve 10 haneli olmalı.");
            statusLabel.setTextFill(Color.RED);
            phoneBox.clear();
            return;
        } else {
            /*
            if(Manager.telnoKontrol(phoneInput)){
                statusLabel.setText("Aradığınız telefon numarasına sahip bir öğrenci zaten vardır.");
                phoneBox.clear();
                return;
            }
            */
        }

        //Manager.addStudent();

        statusLabel.setText("eklendi!!!");
        statusLabel.setTextFill(Color.GREEN);




    }


}
