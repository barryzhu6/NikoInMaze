package edu.dsbbproj.nikoinmaze;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class MazeApplication extends Application {
    private static MazeApplication instance;
    private SimpleIntegerProperty[][] mazeBind;
    private ARAStarMazeSolver solver;

    private Maze maze;

    private void setMazeBind(int[][] array) {
        int row = array.length;
        int col = array[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mazeBind[i][j].set(array[i][j]);
            }
        }
    }

    private void initMazeBind(int[][] array) {
        int row = array.length;
        int col = array[0].length;
        mazeBind = new SimpleIntegerProperty[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mazeBind[i][j] = new SimpleIntegerProperty(array[i][j]);
            }
        }
    }

    private void setNiko() {
        int x = maze.getX();
        int y = maze.getY();
        mazeBind[x][y].set(2);
    }

    private void setPath(List<Point> path){
        path.forEach(point -> mazeBind[point.x][point.y].set(3));
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        AnchorPane root = new AnchorPane();
        Scene MainScene = new Scene(root, Paint.valueOf("#FFFFFF00"));
        DoubleProperty titleSize = new SimpleDoubleProperty(10.0);

        primaryStage.setScene(MainScene);
        primaryStage.setHeight(720);
        primaryStage.setWidth(960);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Niko In Maze");
        primaryStage.getIcons().add(new Image("icon.png"));

        //Background Image
        ImageView backg = new ImageView();
        backg.setImage(new Image("backg.png"));
        backg.setPreserveRatio(false);
        backg.setSmooth(true);

        //Maze Panel
        maze = Main.getMaze();
        solver = new ARAStarMazeSolver(maze);
        int row = maze.getMaze().length;
        int col = maze.getMaze()[0].length;
        initMazeBind(maze.getMaze());
        GridPane mazeGrid = new GridPane();
        mPane[][] mPanes = new mPane[row][col];
        Pane base = new StackPane();
        base.getChildren().addAll(mazeGrid);
        base.setStyle("-fx-background-color:#69696900;");
        mazeGrid.setAlignment(Pos.TOP_CENTER);
        base.setPadding(new Insets(15,0,0,0));

        //Dock beneath
        HBox dock = new HBox(10);
        dock.setPadding(new Insets(20,10,20,10));
        dock.setAlignment(Pos.CENTER);
        Rectangle backDock = new Rectangle();
        Group dockPane = new Group();
        dockPane.setStyle("-fx-background-color:#FFFFFF00;");
        dockPane.getChildren().addAll(backDock, dock);
        backDock.setFill(Color.valueOf("#FFFFFF60"));
        backDock.widthProperty().bind(dock.widthProperty());
        backDock.heightProperty().bind(dock.heightProperty());
        backDock.setArcHeight(15);
        backDock.setArcWidth(15);
        backDock.setEffect(new GaussianBlur(4));
        Button button1 = new Button("Time++");
        Label Text = new Label();
        Text.textProperty().set("0");
        Text.setPrefWidth(200);
        Text.setAlignment(Pos.CENTER_LEFT);
        Text.setStyle("-fx-font-size: 20;" +
                "-fx-font-family: Times New Roman;" +
                "-fx-font-weight: Bold");
        Label Name = new Label();
        Name.setText("Time Now: ");
        Name.setAlignment(Pos.CENTER);
        Name.setStyle("-fx-font-size: 20;" +
                "-fx-font-family: Times New Roman;" +
                "-fx-font-weight: Bold");
        dock.getChildren().addAll(Name,Text,button1);
        //TODO
        //add step display

        root.getChildren().addAll(backg,base,dockPane);

        Platform.runLater(() -> {
            primaryStage.show();
            backg.fitWidthProperty().bind(MainScene.widthProperty());
            backg.fitHeightProperty().bind(base.heightProperty());
            base.prefHeightProperty().bind(MainScene.heightProperty());
            base.prefWidthProperty().bind(MainScene.widthProperty());
            AnchorPane.setBottomAnchor(dockPane, 10.0);
            AnchorPane.setLeftAnchor(dockPane, 30.0);
            AnchorPane.setRightAnchor(dockPane,30.0);
            dock.prefWidthProperty().bind(MainScene.widthProperty().subtract(60));
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    mPanes[i][j] = new mPane(MainScene, row, col);
                    mPanes[i][j].setVal(mazeBind[i][j]);
                    mazeGrid.add(mPanes[i][j],j,i);
                    GridPane.setHgrow(mPanes[i][j], Priority.NEVER);
                    GridPane.setVgrow(mPanes[i][j], Priority.NEVER);
                }
            }
            setNiko();
        });

        Point end = new Point(row - 1, col - 1);
        button1.setOnMouseClicked(mouseEvent -> {
            while(maze.getE() >= 0) {
                setNiko();
                Text.textProperty().set(maze.getT() + "  e: " + maze.getE());
                int[] cor = maze.magic();
                if (cor != null) {
                    mPanes[cor[0]][cor[1]].setAttacked(true);
                }
                setMazeBind(maze.getMaze());
                Point start = new Point(maze.getX(), maze.getY());
                List<Point> path = solver.findPath(start, end);
                Boolean bool = maze.answerKunGui();
                if (path.size() > 1) {
                    start = new Point(path.get(1).x, path.get(1).y);
                    maze.move(start.x, start.y);
                }
                setPath(path);
                if (start.x == row-1 && start.y == col-1) {
                    Text.textProperty().set(maze.getT() + "  e: " + maze.getE());
                    setMazeBind(maze.getMaze());
                    setNiko();
                    break;
                }
                if(bool) break;
            }
        });
    }

    public static MazeApplication getInstance() {
        return instance;
    }

}
