module co.edu.uniquindio.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens co.edu.uniquindio.poo.app to javafx.fxml, javafx.graphics;
    opens co.edu.uniquindio.poo.controller to javafx.fxml;
    opens co.edu.uniquindio.poo.model to javafx.base, javafx.fxml;
}