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

        tcBox.setEditable(false);
        searchButton.setDisable(true);

        Student student;

        //database erişiminde sorun yaşanması durumu için kontrol
        try {
            student = staticgecici.getStudentByTc(tcNo);
            assert student != null;

        } catch (Exception e) {
            statusLabel.setText("Bir hatayla karşılaşıldı:" + e.getMessage());
            statusLabel.setTextFill(Color.RED);
            tcBox.setEditable(true);
            searchButton.setDisable(false);
            return;
        }

        statusLabel.setText(student.getName());


    }




}
