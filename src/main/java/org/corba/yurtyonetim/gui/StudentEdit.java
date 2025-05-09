package org.corba.yurtyonetim.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.corba.yurtyonetim.users.Student;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentEdit extends BaseMenu implements Initializable {
    @FXML
    private ChoiceBox<String> selectSearchAttribute;
    @FXML
    private TextField searchBox;
    @FXML
    private Button searchButton;

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> tcColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> surnameColumn;
    @FXML
    private TableColumn<Student, String> mailColumn;
    @FXML
    private TableColumn<Student, String> phoneColumn;

    @FXML
    private TableColumn<Student, String> dormColumn;
    @FXML
    private TableColumn<Student, String> roomColumn;
    @FXML
    private TableColumn<Student, String> penaltyColumn;
    @FXML
    private TableColumn<Student, String> onLeaveColumn;
    @FXML
    private TableColumn<Student, String> leftLeaveColumn;

    private String[] searchAttributes = {"Kimlik No'ya göre","Ada göre","Soyada göre","E-postaya göre","Telefona göre","Yurda göre"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectSearchAttribute.getItems().addAll(searchAttributes);
    }

    public void searchButtonClick(ActionEvent event) {
        String searchAttribute = selectSearchAttribute.getValue();
        String searchInput = searchBox.getText();
        if (searchInput.length()>41) {
            searchInput = searchInput.substring(0,40);
        }
        switch (searchAttribute) {
            case "Kimlik No'ya göre": {

            }
        }
    }




}
