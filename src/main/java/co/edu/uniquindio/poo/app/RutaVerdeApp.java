package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RutaVerdeApp extends Application {
    private final ContextoApp contexto = new ContextoApp();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/co/edu/uniquindio/poo/view/main-view.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setContexto(contexto);
        stage.setTitle("RutaVerde Quindio");
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(new Scene(root, 1350, 840));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
