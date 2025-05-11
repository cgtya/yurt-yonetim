package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.corba.yurtyonetim.users.staticgecici;

import java.net.URL;
import java.util.ResourceBundle;

public class Mainmenu extends BaseMenu implements Initializable {

    @FXML
    private TextField tcBox;
    @FXML
    private ProgressBar moganProg;
    @FXML
    private ProgressBar osmantanProg;
    @FXML
    private ProgressBar huseyinProg;
    @FXML
    private ProgressBar golbasiProg;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void leaveButton(ActionEvent event) {
        //öğrenci tcsini kontrol eder
        String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan
        String tcNo = tcBox.getText();

        if(!tcNo.matches(tcNoRegex)) {
            statusLabel.setText("Geçersiz Kimlik Numarası.");
            statusLabel.setTextFill(Color.RED);
        }

        //öğrencinin izin durumunu değiştiren fonksiyon
        statusLabel.setText(staticgecici.toggleLeaveStatus(tcNo));
        statusLabel.setTextFill(darkModeDefTextColor());
    }

}
