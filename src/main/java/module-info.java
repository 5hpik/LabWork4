module shpik.jmt3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens shpik.jmt3 to javafx.fxml;
    exports shpik.jmt3;
}