package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.corba.yurtyonetim.database.DormDAO;
import org.corba.yurtyonetim.database.StudentDAO;
import org.corba.yurtyonetim.users.Student;



public class BecayisManagement extends BaseMenu{
    @FXML
    private TextField tcBox1;
    @FXML
    private TextField tcBox2;
    @FXML
    private TextField dormBox1;
    @FXML
    private TextField dormBox2;

    @FXML
    private Button applyButton;
    @FXML
    private Button updateButton;

    @FXML
    private Label statusLabel;

    Student st1;
    Student st2;


    public void updateButtonClick(ActionEvent event) {

        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());

        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan

        String tcNo1 = tcBox1.getText();
        String tcNo2 = tcBox2.getText();

        //tc numarası kontrolü
        if (!(tcNo1.matches(tcNoRegex) && tcNo2.matches(tcNoRegex))) {
            statusLabel.setText("Geçersiz kimlik numarası!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        //öğrenciler veritabanından çekilir
        st1 = StudentDAO.getStudentByTc(tcNo1);
        st2 = StudentDAO.getStudentByTc(tcNo2);

        //geçerli öğrenci kontrolü
        if (st1 == null) {
            statusLabel.setText("İlk kutucuktaki kimlik numarasına sahip öğrenci bulunamadı!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (st2 == null) {
            statusLabel.setText("İkinci kutucuktaki kimlik numarasına sahip öğrenci bulunamadı!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        String dorm1 = st1.getCurrentDorm();
        String dorm2 = st2.getCurrentDorm();

        //öğrenciler aynı yurtta mı kontrolü
        if (dorm1.equals(dorm2)) {
            statusLabel.setText("Öğrenciler zaten aynı yurtta bulunmaktadır. Yurt: " + dorm1);
            statusLabel.setTextFill(Color.RED);
            return;
        }

        //kutucuklar guncellenir
        dormBox1.setText(dorm1);
        dormBox2.setText(dorm2);

        tcBox1.setDisable(true);
        tcBox2.setDisable(true);

        updateButton.setDisable(true);
        applyButton.setDisable(false);

        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());
    }

    public void applyButtonClick(ActionEvent event) {
        statusLabel.setText("");
        statusLabel.setTextFill(darkModeDefTextColor());

        String tcNo1 = tcBox1.getText();
        String tcNo2 = tcBox2.getText();

        String message = DormDAO.makeBecayisStatic(tcNo1,tcNo2);

        resetPage();

        statusLabel.setText(message);
        
    }

    public void resetPage() {
        dormBox1.clear();
        dormBox2.clear();
        tcBox1.setDisable(false);
        tcBox2.setDisable(false);

        updateButton.setDisable(false);
        applyButton.setDisable(true);
        statusLabel.setText("");
    }
}
