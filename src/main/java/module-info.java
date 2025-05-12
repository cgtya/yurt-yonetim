module org.corba.yurtyonetim {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.apache.commons.codec;


    opens org.corba.yurtyonetim.gui to javafx.fxml;
    exports org.corba.yurtyonetim.gui;
}