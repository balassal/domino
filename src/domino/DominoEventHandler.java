package domino;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.Arrays;

public class DominoEventHandler implements EventHandler<MouseEvent> {

    private Game game;
    private Domino[] side;
    private Domino[][] center;
    private Domino selectedDomino;
    private Domino targetDomino;

    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    public DominoEventHandler(Game game) {
        this.game = game;
        side=game.getSide();
        center = game.getCenter();
        selectedDomino=null;
        targetDomino=null;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getTarget() instanceof Domino) {
            if (selectedDomino == null) {
                selectedDomino = (Domino) event.getTarget();
                if (selectedDomino.bennevan(side)) {
                    selectedDomino.setEffect(new DropShadow(10, Color.GREEN));
                }
            } else {
                targetDomino= (Domino) event.getTarget();
                if (targetDomino != selectedDomino) {
                    if (targetDomino.bennevan(side)) {
                        targetDomino.setEffect(new DropShadow(10, Color.GREEN));
                        selectedDomino.setEffect(null);
                        selectedDomino=targetDomino;
                        targetDomino=null;
                    } else {
                        if (selectedDomino.getLowerValue() == targetDomino.getUpperValue()) {
                            int indexOfSelectedDomino = Arrays.asList(side).indexOf(selectedDomino);
                            for (int i = 0; i < center.length; i++) {
                                for (int j = 0; j < center[i].length; j++) {
                                    if (targetDomino == center[i][j]) {
                                        try {
                                            center[(i - 1)][j] = selectedDomino;
                                            side[indexOfSelectedDomino]=null;
                                            selectedDomino=null;
                                            targetDomino=null;
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("DOMINO");
                                            alert.setHeaderText(null);
                                            alert.setContentText("Erre az oszlopra nem tudsz több Dominot rakni!");
                                            alert.showAndWait();
                                        }
                                    }
                                }
                            }
                            game.setScore(game.getScore()+100);
                            game.getGameBoard().getLbPoints().setText("Pontok: "+formatter.format(game.getScore()));
                            checkLines();
                            game.getGamePane().getChildren().clear();
                            game.megjelenitDominok();
                        }
                    }
                }
            }
        }
    }

    private void checkLines() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (center[3][i] != null) {
                count++;
            }
        }
        if (count == 5) {
            for(int i=4; i>=1; i--) {
                for(int j=4; j>=0; j--) {
                    center[i][j] = center[(i - 1)][j];
                }
            }
            for (int i = 0; i < 5; i++) {
                center[0][i]=null;
            }
            game.setScore(game.getScore()+500);
            game.getGameBoard().getLbPoints().setText("Pontok: "+formatter.format(game.getScore()));
        }

    }
}