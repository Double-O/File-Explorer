package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    public static Group root_1 = new Group();
    public static Group root_2 = new Group();



    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent Root1 = FXMLLoader.load(getClass().getResource("sample.fxml"));

        root_1.getChildren().add(Root1);


        Scene scn1 = new Scene(root_1, 1000, 600);

        primaryStage.setTitle("File Explorer");
        primaryStage.setScene(scn1);
        primaryStage.show();
    }


    public static void main(String[] args)  {

        launch(args);

    }
}
