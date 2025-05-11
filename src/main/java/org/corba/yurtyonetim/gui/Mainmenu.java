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
    @FXML
    private Label moganLabel;
    @FXML
    private Label osmanLabel;
    @FXML
    private Label huseyinLabel;
    @FXML
    private Label golbasiLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int golbasi = staticgecici.getStudentCount("Golbasi");
        int huseyin = staticgecici.getStudentCount("HuseyinGazi");
        int osmantan = staticgecici.getStudentCount("OsmanTan");
        int mogan = staticgecici.getStudentCount("Mogan");

        //eğer hata ile karşılaşılmaz ise progressbarlar güncellenir
        if (!(golbasi==-1)) {
            golbasiProg.setProgress(golbasi/200.0);

            golbasiLabel.setText(golbasi+"/200");
            golbasiLabel.setTextFill(darkModeDefTextColor());

        } else {
            golbasiLabel.setText("Hata!");
            golbasiLabel.setTextFill(Color.RED);
        }

        if (!(huseyin==-1)) {
            huseyinProg.setProgress(huseyin/200.0);

            huseyinLabel.setText(huseyin+"/200");
            huseyinLabel.setTextFill(darkModeDefTextColor());
        } else {
            huseyinLabel.setText("Hata!");
            huseyinLabel.setTextFill(Color.RED);
        }

        if (!(osmantan==-1)) {
            osmantanProg.setProgress(osmantan/200.0);

            osmanLabel.setText(osmantan+"/200");
            osmanLabel.setTextFill(darkModeDefTextColor());
        } else {
            osmanLabel.setText("Hata!");
            osmanLabel.setTextFill(Color.RED);
        }

        if (!(mogan==-1)) {
            moganProg.setProgress(mogan/200.0);

            moganLabel.setText(mogan+"/200");
            moganLabel.setTextFill(darkModeDefTextColor());
        } else {
            moganLabel.setText("Hata!");
            moganLabel.setTextFill(Color.RED);
        }
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
