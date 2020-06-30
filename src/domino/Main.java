package domino;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    private Stage mainStage;
    private Menu menu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize
        mainStage = primaryStage;
        mainStage.centerOnScreen();
        mainStage.initStyle(StageStyle.UNDECORATED);
        menu = new Menu(mainStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
