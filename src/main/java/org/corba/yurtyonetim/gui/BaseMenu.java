package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class BaseMenu {

    public void about(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Hakkında");
        about.setHeaderText("selam");
        about.setContentText("j");
        about.showAndWait();

    }
}
