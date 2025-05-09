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

        Student student;

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




}