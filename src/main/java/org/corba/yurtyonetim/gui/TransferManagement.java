package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.corba.yurtyonetim.users.Student;
import org.corba.yurtyonetim.users.staticgecici;


public class TransferManagement extends BaseMenu {
    @FXML
    private TextField tcBox;
    @FXML
    private TextField dormBox;

    @FXML
    private ChoiceBox<String> availableDorm;

    @FXML
    private Button applyButton;
    @FXML
    private Button updateButton;

    @FXML
    private Label statusLabel;

    Student st1;

    public void updateButtonClick(ActionEvent event) {
        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());

        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan

        String tcNo = tcBox.getText();

        //tc numarası kontrolü
        if (!tcNo.matches(tcNoRegex)) {
            statusLabel.setText("Geçersiz kimlik numarası!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        //öğrenci veritabanından çekilir
        st1 = staticgecici.getStudentByTc(tcNo);

        //geçerli öğrenci kontrolü
        if (st1 == null) {
            statusLabel.setText("İlk kutucuktaki kimlik numarasına sahip öğrenci bulunamadı!");
            statusLabel.setTextFill(Color.RED);
            return;
        }


        String dorm = st1.getCurrentDorm();

        //kutucuklar guncellenir
        dormBox.setText(dorm);

        tcBox.setDisable(true);

        updateButton.setDisable(true);
        applyButton.setDisable(false);

        availableDorm.getItems().addAll(staticgecici.getBosYurtlarForStudent(tcNo));
        availableDorm.setDisable(false);

        statusLabel.setText("Öğrencinin nakil olacağı yurdu seçip onaylayabilirsiniz. \nSeçilen öğrenci: " + st1.getName() + " " + st1.getSurname());
        statusLabel.setTextFill(darkModeDefTextColor());
    }

    public void applyButtonClick(ActionEvent event) {
        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());

        String tcNo = tcBox.getText();
        String dormChoice = availableDorm.getValue();

        //nakil metodu çalıştırlır
        String message = staticgecici.nakilYapStatic(tcNo,dormChoice);

        //sayfa eski haline döndürülür
        resetPage();

        //geri bildirim verilir
        statusLabel.setText(message);
        statusLabel.setTextFill(darkModeDefTextColor());
    }

    //sayfatyı eski haline döndüren metot
    public void resetPage() {
        dormBox.clear();
        tcBox.setDisable(false);

        availableDorm.getItems().clear();
        availableDorm.setDisable(true);

        updateButton.setDisable(false);
        applyButton.setDisable(true);
        statusLabel.setText("");
    }





}
