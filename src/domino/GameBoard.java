package domino;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class GameBoard {

    private Stage stage;
    private Pane gamePane;
    private Button btDrawDominoes;
    private Label lbPoints;
    private Label lbLife;
    private Button btMenu;
    private Button btQuit;


    public GameBoard(Stage stage) {
        this.stage=stage;
        stage.centerOnScreen();
        AnchorPane ap = new AnchorPane();
        gamePane = new Pane();
        gamePane.setPrefSize(800, 700);
        gamePane.setTranslateX(0);
        gamePane.setTranslateY(50);


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(7.5));
        header.setSpacing(40);
        header.setPrefSize(800, 50);
        header.setMaxSize(800,50);
        header.setLayoutX(0);
        header.setLayoutY(0);

        btDrawDominoes = new Button("Új leosztás");
        btDrawDominoes.setPrefSize(130, 35);

        lbPoints = new Label("Pontok:");
        lbPoints.setFont(Font.font(18));
        lbPoints.setPrefSize(130, 35);

        lbLife = new Label();
        lbLife.setPrefSize(130,35);

        btMenu = new Button("Főmenü");
        btMenu.setPrefSize(130,35);


        btQuit = new Button("Kilépés");
        btQuit.setPrefSize(130,35);


        ImageView iw = new ImageView();
        Image background = new Image(getClass().getResourceAsStream("images/menu_background.png"));
        iw.setImage(background);
        iw.setFitWidth(800);
        iw.setFitHeight(550);
        iw.setOpacity(0.1);

        header.getChildren().addAll(btMenu,btDrawDominoes, lbPoints, btQuit);
        ap.getChildren().addAll(iw,header,gamePane);

        Scene scene = new Scene(ap, 800, 700);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Button getBtDrawDominoes() {
        return btDrawDominoes;
    }

    public void setBtDrawDominoes(Button btDrawDominoes) {
        this.btDrawDominoes = btDrawDominoes;
    }

    public Label getLbPoints() {
        return lbPoints;
    }

    public void setLbPoints(Label lbPoints) {
        this.lbPoints = lbPoints;
    }

    public Label getLbLife() {
        return lbLife;
    }

    public void setLbLife(Label lbLife) {
        this.lbLife = lbLife;
    }

    public Button getBtMenu() {
        return btMenu;
    }

    public void setBtMenu(Button btMenu) {
        this.btMenu = btMenu;
    }

    public Button getBtQuit() {
        return btQuit;
    }

    public void setBtQuit(Button btQuit) {
        this.btQuit = btQuit;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
    }
}
