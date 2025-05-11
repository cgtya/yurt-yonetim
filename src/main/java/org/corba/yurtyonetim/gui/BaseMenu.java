package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.corba.yurtyonetim.users.staticgecici;

import java.io.IOException;

public class BaseMenu {

    private static boolean darkMode = false;

    public void setDarkMode(boolean darkMode) {
        BaseMenu.darkMode = darkMode;
    }
    public boolean getDarkMode() {
        return darkMode;
    }

    public Color darkModeDefTextColor() {
        if (darkMode) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public void changeScene(ActionEvent event, Parent root) throws IOException {
        Stage stage;
        Scene scene;
        
        Object source = event.getSource();
        if (source instanceof MenuItem) {
            stage = (Stage) ((MenuItem) source).getParentPopup().getOwnerWindow();
        } else if (source instanceof Node) {
            stage = (Stage) ((Node) source).getScene().getWindow();
        } else {
            throw new IllegalArgumentException("Desteklenmeyen kaynak tipi: " + source.getClass());
        }

        scene = new Scene(root);

        if (darkMode) {
            String cssPath = getClass().getResource("/org/corba/yurtyonetim/gui/dark-theme.css").toExternalForm();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssPath);
        }

        stage.setScene(scene);
        stage.show();
    }

    public void about(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Hakkında");
        about.setHeaderText("Yurt Yönetim Sistemi");
        about.setContentText("a");
        about.showAndWait();

    }

    public void studentAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("studentadd.fxml"));
        changeScene(event,root);
    }

    public void studentEdit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("studentedit.fxml"));
        changeScene(event,root);
    }

    public void becayisManagement(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("becayismanagement.fxml"));
        changeScene(event,root);
    }

    public void transferManagement(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("transfermanagement.fxml"));
        changeScene(event,root);
    }

    public void editUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("edituser.fxml"));
        changeScene(event,root);
    }

    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış");
        alert.setHeaderText("Çıkış yapmak üzeresiniz.");
        alert.setContentText("Çıkış yapılsın mı?");

        //onay kutucuğu
        if (alert.showAndWait().get() == ButtonType.OK) {
            //giriş yapan hesap değişkenini boşaltır
            staticgecici.setLoggedInManager(null);

            //anasayfa ekranına döndürür
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            changeScene(event,root);
        }

    }

    public void mainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        changeScene(event,root);
    }

    public void darkMode(ActionEvent event) {
        if (darkMode) {
            darkMode = false;
        } else {
            darkMode = true;
        }
    }
}