package domino;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Menu {

    private Stage stage;
    private ImageView iw;
    private Image background;

    public Menu(Stage stage) {
        this.stage=stage;
        stage.centerOnScreen();

        iw = new ImageView();
        background = new Image(getClass().getResourceAsStream("images/menu_background.png"));
        iw.setImage(background);
        iw.setFitWidth(800);
        iw.setFitHeight(700);
        iw.setOpacity(0.25);

        Label title = new Label("DOMINO");
        title.setFont(Font.font("arial black", 72));
        title.setTextFill(Color.BLACK);
        title.setStyle("-fx-background-color: #C0C0C0");
        title.setLayoutX(240);
        title.setLayoutY(70);

        // new game button
        Button btNewGame = new Button("Új játék");
        btNewGame.setPrefSize(130, 35);
        btNewGame.setLayoutX(335);
        btNewGame.setLayoutY(260);
        btNewGame.setOnMouseClicked(e -> {
            try {
                new Game(this.stage, Mode.NEW);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        // resume game button
        Button btResumeGame = new Button("Folytatás");
        btResumeGame.setPrefSize(130, 35);
        btResumeGame.setLayoutX(335);
        btResumeGame.setLayoutY(320);
        File file = new File("center.data");
        if (file.exists()) {
            btResumeGame.setDisable(false);
        } else {
            btResumeGame.setDisable(true);
        }
        btResumeGame.setOnMouseClicked(e -> {
            try {
                new Game(this.stage, Mode.RESUME);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        // quit button
        Button btQuit = new Button("Kilépés");
        btQuit.setPrefSize(130, 35);
        btQuit.setLayoutX(335);
        btQuit.setLayoutY(500);
        btQuit.setOnMouseClicked(e -> System.exit(1));

        AnchorPane ap = new AnchorPane();
        ap.getChildren().addAll(iw, title, btNewGame, btResumeGame, btQuit);

        Scene scene = new Scene(ap, 800,700);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

}
