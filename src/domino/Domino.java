package domino;

import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Domino extends ImageView implements Serializable {

    private Pane pane;
    private Image image;
    private int upperValue;
    private int lowerValue;
    private final int WIDTH = 60;
    private final int HEIGHT = 120;
    private boolean isSelected;

    public Domino(int upperValue, int lowerValue) {
        this.upperValue = upperValue;
        this.lowerValue = lowerValue;

        String path = "images/dominoes/"+ Integer.toString(upperValue) + "" + Integer.toString(lowerValue) + ".png";
        image = new Image(getClass().getResourceAsStream(path));
        setFitWidth(WIDTH);
        setFitHeight(HEIGHT);
        setImage(image);
        isSelected = false;

        setOnMouseEntered(e-> setCursor(Cursor.HAND));
    }

    public boolean bennevan(Domino[] dominok) {
        for (Domino d : dominok) {
            if (this.equals(d)) {
                return true;
            }
        }
        return false;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public int getUpperValue() {
        return upperValue;
    }

    public void setUpperValue(int upperValue) {
        this.upperValue = upperValue;
    }

    public int getLowerValue() {
        return lowerValue;
    }

    public void setLowerValue(int lowerValue) {
        this.lowerValue = lowerValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
