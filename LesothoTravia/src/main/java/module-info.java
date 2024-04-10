module com.lesotho.lesothotravia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.lesotho.lesothotravia to javafx.fxml;
    exports com.lesotho.lesothotravia;
}