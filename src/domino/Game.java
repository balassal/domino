package domino;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Game {

    private Stage stage;
    private GameBoard gameBoard;
    private Pane gamePane;

    private int score = 0;
    private int life=3;

    private Stack<Domino> osszesPar;
    private Domino[] side = new Domino[10];
    private Domino[][] center = new Domino[5][5];

    private Mode mode;

    public Game(Stage stage, Mode mode) throws IOException, ClassNotFoundException {
        this.mode = mode;
        osszesPar = new Stack<>();
        this.stage=stage;
        stage.centerOnScreen();
        gameBoard = new GameBoard(stage);
        gamePane = gameBoard.getGamePane();

        feltoltOsszesPar();

        if (mode == Mode.NEW) {
            startNewGame();
        } else {
            resumeGame();
        }

        gamePane.addEventHandler(MouseEvent.MOUSE_CLICKED, new DominoEventHandler(this));

        gameBoard.getBtDrawDominoes().setOnMouseClicked(e->{
            int count = getInAvaibleSizeOf(side);
            if (count==0) return;
            if (count > osszesPar.size()) {
                osszesPar.clear();
                feltoltOsszesPar();
                for (int i = 0; i < side.length; i++) {
                    if(side[i]!=null){
                        continue;
                    } else {
                        side[i]=osszesPar.pop();
                    }
                }
                setScore(getScore() - 300);
                if(getScore()<0) setScore(0);
                gameBoard.getLbPoints().setText("Pontok: "+getScore());
                gamePane.getChildren().clear();
                megjelenitDominok();
            } else {
                for (int i = 0; i < side.length; i++) {
                    if (side[i] != null) {
                        continue;
                    } else {
                        side[i] = osszesPar.pop();
                    }
                }
                setScore(getScore() - 300);
                if(getScore()<0) setScore(0);
                gameBoard.getLbPoints().setText("Pontok: "+getScore());
                gamePane.getChildren().clear();
                megjelenitDominok();
            }
        });
        gameBoard.getBtMenu().setOnMouseClicked(e ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DOMINO");
            alert.setHeaderText(null);
            alert.setContentText("Főmenübe lépés előtt szeretnéd menteni az aktuális állást?");
            ButtonType save = new ButtonType("Mentem");
            ButtonType toMenu = new ButtonType("Nem mentem");
            ButtonType cancel = new ButtonType("Mégse", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(save, toMenu, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == save) {
                try {
                    save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                new Menu(this.stage);
            } else if (result.get() == toMenu) {
                File file = new File("center.data");
                if (file.exists()) {
                    file.delete();
                    file = new File("side.data");
                    file.delete();
                    file = new File("score.data");
                    file.delete();
                }
                new Menu(this.stage);
            } else {
                alert.close();
            }
        });
        gameBoard.getBtQuit().setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DOMINO");
            alert.setHeaderText(null);
            alert.setContentText("Kilépés előtt szeretnéd menteni az aktuális állást?");
            ButtonType save = new ButtonType("Mentem");
            ButtonType quit = new ButtonType("Kilépek");
            ButtonType cancel = new ButtonType("Mégse", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(save, quit, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == save) {
                try {
                    save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Platform.exit();
            } else if (result.get() == quit) {
                File file = new File("center.data");
                if (file.exists()) {
                    file.delete();
                    file = new File("side.data");
                    file.delete();
                    file = new File("score.data");
                    file.delete();
                }
                Platform.exit();
            } else {
                alert.close();
            }
        });
    }

    private void startNewGame() {
        gameBoard.getLbLife().setText("Lehetőségek: " + life);
        gameBoard.getLbPoints().setText("Pontok: "+score);
        oldalFeltolt();
        kozepFeltolt();
        megjelenitDominok();
    }

    private void resumeGame() throws IOException, ClassNotFoundException {
        load();
        gameBoard.getLbPoints().setText("Pontok: "+score);
        megjelenitDominok();
    }

    public void save() throws IOException {
        ArrayList<Point> sideDominoValues = new ArrayList<>();
        for (int i = 0; i < side.length; i++) {
            Domino d = side[i];
            if (d == null) {
                sideDominoValues.add(null);
            } else {
                Point p = new Point(d.getUpperValue(), d.getLowerValue());
                sideDominoValues.add(p);
            }
        }
        Stack<Point> centerDominoValues = new Stack<>();
        for (int i=4; i>=0; i--) {
            for (int j = 4; j>=0; j--) {
                Domino d = center[i][j];
                if (d == null) {
                    centerDominoValues.push(null);
                } else {
                    Point p = new Point(d.getUpperValue(), d.getLowerValue());
                    centerDominoValues.push(p);
                }
            }
        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("side.data"));
        out.writeObject(sideDominoValues);
        out.flush();
        out.close();
        out = new ObjectOutputStream(new FileOutputStream("center.data"));
        out.writeObject(centerDominoValues);
        out.flush();
        out.close();

        DataOutputStream dos = new DataOutputStream(new FileOutputStream("score.data"));
        dos.writeInt(getScore());
        dos.close();
    }

    public void load() throws IOException, ClassNotFoundException {
        ArrayList<Point> sideDominoValues;
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("side.data"));
        sideDominoValues = (ArrayList<Point>) input.readObject();
        for (int i = 0; i < side.length; i++) {
            Point p = sideDominoValues.get(i);
            if (p == null) {
                side[i] = null;
            } else {
                Domino d = new Domino(p.getX(), p.getY());
                side[i] = d;
            }
        }
        input.close();

        Stack<Point> centerDominoValues;
        input = new ObjectInputStream(new FileInputStream("center.data"));
        centerDominoValues = (Stack<Point>) input.readObject();
        for (int i = 0; i < center.length; i++) {
            for (int j = 0; j < center[i].length; j++) {
                Point p = centerDominoValues.pop();
                if (p == null) {
                    center[i][j] = null;
                } else {
                    Domino d = new Domino(p.getX(), p.getY());
                    center[i][j] = d;
                }
            }
        }
        input.close();

        DataInputStream dis = new DataInputStream(new FileInputStream("score.data"));
        setScore(dis.readInt());
        dis.close();
    }


    /************* MEGJELENÍTÉS ****************/

    public void megjelenitDominok() {
        megjelenitOldal();
        megjelenitKozep();
        megjelenitVonalak();
    }

    private void megjelenitOldal(){
        for (int i = 0; i < 5; i++) {
            if(side[i]==null) continue;
            Domino d = side[i];
            d.setLayoutX(10);
            d.setLayoutY(i * 130);
            gamePane.getChildren().add(d);
        }
        for (int i = 5; i < side.length; i++) {
            if(side[i]==null) continue;
            Domino d = side[i];
            d.setLayoutX(730);
            d.setLayoutY((i-5) * 130);
            gamePane.getChildren().add(d);
        }
    }

    private void megjelenitVonalak() {
        Line leftLine = new Line();
        leftLine.setStartX(80);
        leftLine.setEndX(80);
        leftLine.setStartY(0);
        leftLine.setEndY(gamePane.getHeight());
        Line rightLine = new Line();
        rightLine.setStartX(720);
        rightLine.setEndX(720);
        rightLine.setStartY(0);
        rightLine.setEndY(gamePane.getHeight());
        gamePane.getChildren().addAll(leftLine, rightLine);
    }

    private void megjelenitKozep(){
        for (int i = 0; i < center.length; i++) {
            for (int j = 0; j < center[i].length; j++) {
                if(center[i][j] == null) continue;
                Domino d = center[i][j];
                d.setLayoutX((j + 1) * 110+30);
                d.setLayoutY((i * 130)+70);
                d.setEffect(null);
                gamePane.getChildren().add(d);
            }
        }
    }


    /************* LISTAMŰVELETEK ***************/

    private void oldalFeltolt() {
        for (int i = 0; i < side.length; i++) {
            side[i] = osszesPar.pop();
        }
    }

    private void kozepFeltolt(){

        for (int i = 4; i < center.length; i++) {
            for (int j = 0; j < center[i].length; j++) {
                center[i][j] = osszesPar.pop();
            }
        }
    }

    private void feltoltOsszesPar() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Domino d = new Domino(i, j);
                osszesPar.add(d);
            }
        }
        Collections.shuffle(osszesPar);
    }

    private int getAvaibleSizeOf(Domino[] dominos) {
        int count = 0;
        for (Domino d : dominos) {
            if (d != null) {
                count++;
            }
        }
        return count;
    }

    private int getInAvaibleSizeOf(Domino[] dominos) {
        int count = 0;
        for (Domino d : dominos) {
            if (d == null) {
                count++;
            }
        }
        return count;
    }



    /************* GETTEREK & SETTEREK *****************/

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Stack<Domino> getOsszesPar() {
        return osszesPar;
    }

    public void setOsszesPar(Stack<Domino> osszesPar) {
        this.osszesPar = osszesPar;
    }

    public Domino[][] getCenter() {
        return center;
    }

    public void setCenter(Domino[][] center) {
        this.center = center;
    }

    public Domino[] getSide() {
        return side;
    }

    public void setSide(Domino[] side) {
        this.side = side;
    }
}
