package library.ck_librarymanagament;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.stage.StageStyle.UNDECORATED;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        stage.initStyle(UNDECORATED);
        Scene scene = new Scene(fxmlLoader.load(), 550, 400);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)  {
        launch();
    }
}