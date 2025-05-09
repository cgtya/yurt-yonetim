package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;


public class StudentEdit extends BaseMenu  {

    @FXML
    private TextField tcBox;
    @FXML
    private Button searchButton;
    @FXML
    private Label statusLabel;




    public void searchButtonClick(ActionEvent event) {
        if (tcBox.getText().isEmpty()) {
            statusLabel.setText("Kutucuğu boş bırakmayınız.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (!tcBox.getText().matches("^[1-9][0-9]{10}$")) {
            statusLabel.setText("Geçersiz kimlik numarası.");
            statusLabel.setTextFill(Color.RED);
            return;
        }



    }




}
