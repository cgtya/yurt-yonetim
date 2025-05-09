package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.corba.yurtyonetim.users.Student;
import org.corba.yurtyonetim.users.staticgecici;


public class StudentEdit extends BaseMenu  {

    @FXML
    private TextField tcBox;
    @FXML
    private Button searchButton;
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
    private TextField dormBox;

    @FXML
    private Label penaltyLabel;
    @FXML
    private CheckBox onLeaveCheck;
    @FXML
    private Button penaltyButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button deleteButton;

    Student student;

    //gorev tamamlandığında sayfayı eski haline döndüren fonksiyon
    private void resetPage() {
        nameBox.clear();
        surnameBox.clear();
        mailBox.clear();
        phoneBox.clear();
        dormBox.clear();
        penaltyLabel.setText("");
        onLeaveCheck.setSelected(false);
        nameBox.setDisable(true);
        surnameBox.setDisable(true);
        mailBox.setDisable(true);
        phoneBox.setDisable(true);
        penaltyButton.setDisable(true);
        applyButton.setDisable(true);
        deleteButton.setDisable(true);
        tcBox.setDisable(false);
        searchButton.setDisable(false);
        statusLabel.setText("");
        student = null;
    }


    public void searchButtonClick(ActionEvent event) {
        String tcNo = tcBox.getText();
        if (tcNo.isEmpty()) {
            statusLabel.setText("Kutucuğu boş bırakmayınız.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (!tcNo.matches("^[1-9][0-9]{10}$")) {
            statusLabel.setText("Geçersiz kimlik numarası.");
            statusLabel.setTextFill(Color.RED);
            tcBox.clear();
            return;
        }

        //değişiklikler kaydedilene kadar tc kutucuğu değiştirilemez
        tcBox.setDisable(true);
        searchButton.setDisable(true);


        //database erişiminde sorun yaşanması/öğrenci bulunamaması durumu için kontrol
        try {
            student = staticgecici.getStudentByTc(tcNo);
            if (student == null) {
                statusLabel.setText("Böyle bir öğrenci bulunamadı.");
                statusLabel.setTextFill(Color.RED);
                tcBox.setDisable(false);
                searchButton.setDisable(false);
                return;
            }
        } catch (Exception e) {
            statusLabel.setText("Öğrenci bilgileri alınırken hata oluştu: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
            tcBox.setDisable(false);
            searchButton.setDisable(false);
            return;
        }

        //öğrenci bulunması durumunda kutucuklar aktifleştirilir
        nameBox.setDisable(false);
        surnameBox.setDisable(false);
        mailBox.setDisable(false);
        phoneBox.setDisable(false);
        penaltyButton.setDisable(false);
        applyButton.setDisable(false);
        deleteButton.setDisable(false);

        //kutucuklar doldurulur
        nameBox.setText(student.getName());
        surnameBox.setText(student.getSurname());
        phoneBox.setText(student.getTelNo());
        mailBox.setText(student.getEposta());
        penaltyLabel.setText(String.valueOf(student.getDiciplineNo()));
        onLeaveCheck.setSelected(student.isOnleave());
        dormBox.setText(student.getCurrentDorm());

    }


    //değişiklikleri student classına kaydedip database fonksiyonuna gönderir
    public void saveButtonClick(ActionEvent event){
        String newName = nameBox.getText();
        String newSurname = surnameBox.getText();
        String newMail = mailBox.getText();
        String newPhone = phoneBox.getText();

        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //mail regex
        String telNoRegex = "^5[0-9]{9}$"; // 5 ile başlayan 10 haneli numara

        //girişler geçerli mi diye kontroller
        if (newName.isEmpty() || newSurname.isEmpty() || newMail.isEmpty() || newPhone.isEmpty()) {
            statusLabel.setText("Boş kutucuk bırakmayınız!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (!newMail.matches(emailRegex)) {
            statusLabel.setText("Geçersiz E-Posta!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (!newPhone.matches(telNoRegex)) {
            statusLabel.setText("Geçersiz telefon!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (newName.length()>41 || newSurname.length()>41) {
            statusLabel.setText("40 karakteri geçmeyiniz!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        student.setName(newName);
        student.setSurname(newSurname);
        student.setEmail(newMail);
        student.setTelNo(newPhone);

        String message = staticgecici.updateStudentInDatabase(student);

        resetPage();

        statusLabel.setText(message);
        statusLabel.setTextFill(Color.GREEN);


    }

    //cezayı 1 artırıp sayfayı sıfırlar
    public void penaltyButtonClick(ActionEvent event) {

        String message = staticgecici.addDisiplineRecord(student.getTcNo());

        resetPage();

        statusLabel.setText(message);
        statusLabel.setTextFill(Color.RED);
    }

    //TODO öğrenci silme

}