module library.ck_librarymanagament {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens library.ck_librarymanagament to javafx.fxml;
    exports library.ck_librarymanagament;
}