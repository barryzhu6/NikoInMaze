package edu.dsbbproj.nikoinmaze;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class mPane extends AnchorPane {
    private final SimpleIntegerProperty val;
    private final SimpleBooleanProperty attacked;
    private final SimpleDoubleProperty height = new SimpleDoubleProperty();

    mPane(Scene stage, int m, int n){
        super();
        StackPane stackPane = new StackPane();
        getChildren().add(stackPane);
        val = new SimpleIntegerProperty(0);
        attacked = new SimpleBooleanProperty(false);
        height.bind(Bindings.min(stage.heightProperty().divide(m+2.5),
                stage.widthProperty().divide(n+0.5)));
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        stackPane.prefHeightProperty().bind(height);
        stackPane.prefWidthProperty().bind(height);
        prefHeightProperty().bind(height);
        prefWidthProperty().bind(height);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setElevation(90);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(2);
        setStyle("-fx-background-color:#ccccff;"+
                "-fx-border-color:#696969;"+
                "-fx-border-width:2;");
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setInput(null);
        setEffect(colorAdjust);

        //the effect on choosing mPanes
        setOnMouseEntered(mouseEvent -> {
            colorAdjust.setBrightness(0.3);
            colorAdjust.setContrast(0);
            colorAdjust.setSaturation(0);
        });
        setOnMouseExited(mouseEvent -> {
            colorAdjust.setBrightness(0);
            colorAdjust.setContrast(0);
            colorAdjust.setSaturation(0);
        });

        val.addListener((observable, oldValue, newValue) -> {
            /*
            different val for different status
            0 for normal status
            1 for barrier
            2 for niko
            3 for path
            */
            if ( newValue.intValue() == 0) {
                setStyle("-fx-background-color:#ccccff;"+
                        "-fx-border-color:#696969;"+
                        "-fx-border-width:2;");
                colorAdjust.setHue(0);
            }
            if (newValue.intValue() == 1) colorAdjust.setHue(0.8);
            if (newValue.intValue() == 2) {
                ImageView niko = new ImageView();
                niko.setSmooth(true);
                niko.setImage(new Image("niko.png"));
                niko.setFitWidth(1);
                niko.setFitHeight(1);
                if (n < 50 || m < 50) {
                    niko.fitHeightProperty().bind(height.multiply(0.8));
                    niko.fitWidthProperty().bind(height.multiply(0.8));
                }
                stackPane.getChildren().add(niko);
                setStyle("-fx-background-color:#FFD700;"+
                        "-fx-border-color:#EEEEEE;"+
                        "-fx-border-width:2;");
            }
            if (newValue.intValue() == 3) {
                setStyle("-fx-background-color:#FFD700;"+
                        "-fx-border-color:#EEEEEE;"+
                        "-fx-border-width:2;");
            }
        });

        attacked.addListener(((observable, oldValue, newValue) -> {
            setStyle("-fx-background-color:#ccccff;"+
                    "-fx-border-color:#696969;"+
                    "-fx-border-width:2;");
            if (newValue){
                setStyle("-fx-background-color:#ccccff;"+
                        "-fx-border-color:#ff6600;"+
                        "-fx-border-width:2;");
            }
        }));
    }
    public void setVal(SimpleIntegerProperty val) {
        this.val.bind(val);
    }

    public void setAttacked(boolean attacked) {
        this.attacked.set(attacked);
    }

}
