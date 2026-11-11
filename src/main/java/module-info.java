module com.mycompany.angkotlampung {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.angkotlampung to javafx.fxml;
    exports com.mycompany.angkotlampung;
}
