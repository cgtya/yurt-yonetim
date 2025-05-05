module org.corba.yurtyonetim {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    //opens org.corba.yurtyonetim to javafx.fxml;
    //exports org.corba.yurtyonetim;
    exports org.corba.yurtyonetim.gui;
    opens org.corba.yurtyonetim.gui to javafx.fxml;
}