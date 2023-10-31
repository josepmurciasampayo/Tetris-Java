package tetris;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/*
 * Main class of the Game
 */
public class Game extends Application {
    //Overall size

    public static final int SIZE = 32;
    //Size of the horizontal axis
    public static int MAXIMUM_X = SIZE * 12;
    //size of the vertical axis
    public static int MAXIMUM_Y = SIZE * 24;
    //duration of the level in seconds
    public static int LEVEL_DURATION = 120;
    //2D array for the size of the Grid
    public static int[][] GRID = new int[MAXIMUM_X / SIZE][MAXIMUM_Y / SIZE];
    //count of combo - lines you destroyed
    public static int COMBO_COUNT = 0;
    //rotation of the blocks
    public static final int MOVE = 32;

    private static Pane group = new Pane();
    private static Shape object;
    private static Scene scene = new Scene(group, MAXIMUM_X + 384, MAXIMUM_Y);

    public static int score = 0;
    private static int linesNo = 0;

    private static int top = 0;
    private static boolean game = true;
    private static LinkedList<Shape> shapeQueue = new LinkedList<>();
    //private static Shape nextShape = ShapeFactory.getShape();
    private Timer gameTimer;
    private int delta;

    Label scoretext, linestext, leveltext, combotext;
    Button stopBtn;

    private boolean isStarted = true;
    
    //label for z, x, c, v
    Label ztext, xtext, ctext, vtext;

    //Up next shapes
    List<Pane> nextPanes = new ArrayList<Pane>();

    //pane for hint shapes
    Pane hintPane = new Pane();

    //Levels and timer
    //Starting Level
    private int level = 1;
    //private Timer levelTimer;
    private double speed = 2;
    private int seconds = LEVEL_DURATION;
    private int timerTicks = 0;
    private int timeAdder = 0;
    private int comboTime = 0;

    //Random
    Random rand = new Random();

    //Hammer & shaker panel : image, flag to show or hide, alpha value rectangle, label for keys(n, b)
    ImageView hammerView;
    ImageView shakeView;
    boolean showHammer = false;
    boolean showShake = false;
    Rectangle cdhammer;
    Rectangle cdshake;
    Label hammerText, shakeText;

    int nexthammer = 120;   //hammer cooldown: 2 m, 120 s
    int nextshake = 60;     //shaker cooldown: 1 m, 60 s
    int timehammer = 0;     //time counter of hammer: 0~120
    int timeshake = 0;      //time counter of shaker: 0~60

    Shape curShape, hintShape;

    //RoundResult
    int rank_scores[] = new int[100];       //saved rank scores
    String rank_names[] = new String[100];  //saved rank names
    int rank_count = 0;                     //length of above two arrays

    public static void main(String[] args) {
        SoundManager.setup();
        launch(args);
    }

    /**
     *
     */
    @Override
    /*
	 * Start method override of Application class
     */
    public void start(Stage stage) throws Exception {
        startGame();
        stage.setScene(scene);
        stage.setTitle("PC9Tetris");
        stage.show();
    }

    /**
     *
     */
    private void startGame() {
        //Adds five shapes to a queue using a ShapeFactory object. 
        //The ShapeFactory.getShape() method returns a randomly generated shape object. 
        //Each time the method is called, a new shape object is created and added to the queue. 
        //The resulting queue will contain five randomly generated shapes.
        nextPanes = new ArrayList<Pane>();
        shapeQueue = new LinkedList<>();
        shapeQueue.add(ShapeFactory.getShape());
        shapeQueue.add(ShapeFactory.getShape());
        shapeQueue.add(ShapeFactory.getShape());
        shapeQueue.add(ShapeFactory.getShape());
        shapeQueue.add(ShapeFactory.getShape());

        //Play background sound
        SoundManager.playBackground();

        //This loop iterates through each row of a 2D integer array called GRID, and fills each element of the row with the integer value 0.
        for (int[] a : GRID) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(MAXIMUM_X, 0, MAXIMUM_X, MAXIMUM_Y);
        line.setStroke(Color.RED);

        //Score Text label Initialization
        scoretext = new Label("");
        scoretext.setStyle("-fx-font: 20 arial;");
        scoretext.setTextFill(Color.WHITE);
        scoretext.setLayoutY(90);
        scoretext.setLayoutX(MAXIMUM_X + 120);
        scoretext.setFocusTraversable(false);

        //Lines text label initialization
        linestext = new Label("");
        linestext.setStyle("-fx-font: 20 arial;");
        linestext.setLayoutY(90);
        linestext.setTextFill(Color.WHITE);
        linestext.setLayoutX(MAXIMUM_X + 310);
        linestext.setFocusTraversable(false);

        //Level Number label initialization
        leveltext = new Label("Level 1");
        leveltext.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        leveltext.setLayoutY(144);
        leveltext.setTextFill(Color.WHITE);
        leveltext.setLayoutX(MAXIMUM_X + 74);
        leveltext.setFocusTraversable(false);

        //Level timer text label initialization
        combotext = new Label("");
        combotext.setStyle("-fx-font: 26 'cambria';");
        combotext.setLayoutY(135);
        combotext.setTextFill(Color.WHITE);
        combotext.setLayoutX(MAXIMUM_X + 278);
        combotext.setFocusTraversable(false);

        //Start button
        Button startBtn = new Button("Start");
        startBtn.setLayoutX(MAXIMUM_X + 15);
        startBtn.setLayoutY(700);
        Styler.setBtnStyle(startBtn);
        startBtn.setFocusTraversable(false);

        //Stop Button
        stopBtn = new Button("Stop");
        stopBtn.setLayoutX(MAXIMUM_X + 125);
        stopBtn.setLayoutY(700);
        Styler.setBtnStyleActive(stopBtn);
        stopBtn.setFocusTraversable(false);

        //How to play heading label
        Text help = new Text("How to Play");
        help.setFont(new Font("Cambria", 15.0));
        help.setFill(Color.WHITE);
        help.setX(MAXIMUM_X + 15);
        help.setY(530);
        help.setFocusTraversable(false);

        //How to play text label
        Text help1 = new Text("Press Arrow keys to \nchange the shape in \nclockwise and \nanticlockwise direction.\nStop and Start button \ncan be used to pause \nand resume the game \nrespectively.");
        help1.setFill(Color.WHITE);
        help1.setX(MAXIMUM_X + 15);
        help1.setY(550);
        help1.setFocusTraversable(false);

        //Up next shape pane 1
        Pane nextShape1 = new Pane();
        nextShape1.setStyle("-fx-background-color: linear-gradient(to bottom, #B3B3B3, white); -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        nextShape1.setLayoutX(MAXIMUM_X + 35);
        nextShape1.setLayoutY(253);
        nextShape1.setMinWidth(150);
        nextShape1.setMinHeight(100);
        nextPanes.add(nextShape1);

        ztext = new Label("Z");
        ztext.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        ztext.setLayoutY(253);
        ztext.setTextFill(Color.BLACK);
        ztext.setLayoutX(MAXIMUM_X + 35);
        ztext.setFocusTraversable(false);

        //Up next shape pane 2
        Pane nextShape2 = new Pane();
        nextShape2.setStyle("-fx-background-color: linear-gradient(to bottom, #B3B3B3, white); -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        nextShape2.setLayoutX(MAXIMUM_X + 202);
        nextShape2.setLayoutY(253);
        nextShape2.setMinWidth(150);
        nextShape2.setMinHeight(100);
        nextPanes.add(nextShape2);

        xtext = new Label("X");
        xtext.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        xtext.setLayoutY(253);
        xtext.setTextFill(Color.BLACK);
        xtext.setLayoutX(MAXIMUM_X + 202);
        xtext.setFocusTraversable(false);

        //Up next shape pane 3
        Pane nextShape3 = new Pane();
        nextShape3.setStyle("-fx-background-color: linear-gradient(to bottom, #B3B3B3, white); -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        nextShape3.setLayoutX(MAXIMUM_X + 35);
        nextShape3.setLayoutY(373);
        nextShape3.setMinWidth(150);
        nextShape3.setMinHeight(100);
        nextPanes.add(nextShape3);

        ctext = new Label("C");
        ctext.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        ctext.setLayoutY(373);
        ctext.setTextFill(Color.BLACK);
        ctext.setLayoutX(MAXIMUM_X + 35);
        ctext.setFocusTraversable(false);

        //Up next shape pane 4
        Pane nextShape4 = new Pane();
        nextShape4.setStyle("-fx-background-color: linear-gradient(to bottom, #B3B3B3, white); -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        nextShape4.setLayoutX(MAXIMUM_X + 202);
        nextShape4.setLayoutY(373);
        nextShape4.setMinWidth(150);
        nextShape4.setMinHeight(100);
        nextPanes.add(nextShape4);

        vtext = new Label("V");
        vtext.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        vtext.setLayoutY(373);
        vtext.setTextFill(Color.BLACK);
        vtext.setLayoutX(MAXIMUM_X + 202);
        vtext.setFocusTraversable(false);

        Pane cdpane = new Pane();
        //Initialize hammer image to be shown randomly
        InputStream stream = null;
        try {
            stream = new FileInputStream("src/main/resources/imgs/hammer2.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        hammerView = new ImageView();
        hammerView.setImage(image);
        hammerView.setX(MAXIMUM_X + 190);
        hammerView.setY(580);
        hammerView.setFitWidth(75);
        hammerView.setFitHeight(75);
        hammerView.setPreserveRatio(true);
        hammerView.setStyle("-fx-cursor: hand;");

        //Add event handler to hammer
        hammerView.addEventHandler(MouseEvent.MOUSE_CLICKED, hammerhandler);

        hammerText = new Label("V");
        hammerText.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        hammerText.setLayoutX(MAXIMUM_X + 170);
        hammerText.setLayoutY(550);
        hammerText.setTextFill(Color.WHITE);
        hammerText.setFocusTraversable(false);

        cdhammer = new Rectangle(75, 75);
        cdhammer.setX(MAXIMUM_X + 190);
        cdhammer.setY(580);
        cdhammer.setFill(Color.web("0x000000", 0.8));

        InputStream stream1 = null;
        try {
            stream1 = new FileInputStream("src/main/resources/imgs/shake.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image1 = new Image(stream1);
        shakeView = new ImageView();
        shakeView.setImage(image1);
        shakeView.setX(MAXIMUM_X + 290);
        shakeView.setY(580);
        shakeView.setFitWidth(75);
        shakeView.setFitHeight(75);
        shakeView.setPreserveRatio(true);
        shakeView.setStyle("-fx-cursor: hand;");

        shakeView.addEventHandler(MouseEvent.MOUSE_CLICKED, shakeHandler);

        shakeText = new Label("V");
        shakeText.setStyle("-fx-font: 22 'Lucida Calligraphy';");
        shakeText.setLayoutX(MAXIMUM_X + 270);
        shakeText.setLayoutY(550);
        shakeText.setTextFill(Color.WHITE);
        hammerText.setFocusTraversable(false);

        cdshake = new Rectangle(75, 75);
        cdshake.setX(MAXIMUM_X + 290);
        cdshake.setY(580);
        cdshake.setFill(Color.web("0x000000", 0.8));
        
        cdpane.getChildren().addAll(cdhammer, cdshake);

        //Add all the created children to group pane
        group.getChildren().addAll(scoretext, line, linestext, startBtn, stopBtn, help, help1, leveltext, combotext,
            nextShape1, nextShape2, nextShape3, nextShape4, hammerView, shakeView, hammerText, shakeText,
            ztext, xtext, ctext, vtext);

        //Get one initial shape from shape queue and place it in group pane
        Shape a = shapeQueue.poll();
        group.getChildren().addAll(a.sq1, a.sq2, a.sq3, a.sq4);
        if (a.bomb != null) {
            group.getChildren().add(a.bomb);
        } else if (a.rocket != null) {
            group.getChildren().add(a.rocket);
        } else if (a.water != null) {
            group.getChildren().add(a.water);
        } else if (a.flex != null) {
            group.getChildren().add(a.flex);
        } else if (a.block != null) {
            group.getChildren().add(a.block);
        }

        moveOnKeyPress(a);
        object = a;
        curShape = a;
        shapeQueue.add(ShapeFactory.getShape());
        drawUpNext(4);

        //operation to create and add hint shape
        hintPane = new Pane();
        hintShape = new Shape(new Rectangle(a.sq1.getX(), a.sq1.getY(), SIZE - 1, SIZE - 1), new Rectangle(a.sq2.getX(), a.sq2.getY(), SIZE - 1, SIZE - 1),
            new Rectangle(a.sq3.getX(), a.sq3.getY(), SIZE - 1, SIZE - 1), new Rectangle(a.sq4.getX(), a.sq4.getY(), SIZE - 1, SIZE - 1), "h");
        hintPane.getChildren().addAll(hintShape.sq1, hintShape.sq2, hintShape.sq3, hintShape.sq4);
        group.getChildren().add(hintPane);
        
        group.getChildren().add(cdpane);

        //Setup background image of the game
        URL url = getClass().getResource("/imgs/bg.png");
        group.setStyle("-fx-background-image: url(\"" + url.toString() + "\"); -fx-background-repeat: stretch; -fx-background-size: cover; -fx-background-position: center center;");

        //Setup timer for the game and add timer task to it
        gameTimer = new Timer();
        TimerTask task = getTimerTask();

        top = 0;
        level = 1;
        speed = 2;
        score = 0;
        linesNo = 0;
        seconds = LEVEL_DURATION;
        timerTicks = 0;
        game = true;
        timeAdder = 0;

        // count time per 0.1s, 100ms not 1m
        delta = 100;
        gameTimer.schedule(task, 0, (int) (100 / speed));

        //When game starts, start button should be disabled and stop button is enabled
        startBtn.setDisable(true);
        stopBtn.setDisable(false);

        //Add event handler to start button
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isStarted) {
                    isStarted = true;
                    gameTimer = new Timer();
                    TimerTask task = getTimerTask();
                    gameTimer.schedule(task, 0, (int) (100 / speed));
                    delta = 100;
                    SoundManager.playBackground();
                    stopBtn.setDisable(false);
                    startBtn.setDisable(true);
                }

            }
        });

        //Add event handler to stop button
        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isStarted) {
                    isStarted = false;
                    gameTimer.cancel();
                    SoundManager.stopBackground();
                    stopBtn.setDisable(true);
                    startBtn.setDisable(false);
                }
            }
        });
    }
    
    void HammerScreen() {
        // hammer option : delete one bottom line
        ArrayList<Integer> lines = new ArrayList<Integer>();
        lines.add(23);
        breakLines(group, lines);
    }

    /*
	 * Event handler class for hammer
	 * When hammer clicked, it removes bottom row from the game
     */
    EventHandler<MouseEvent> hammerhandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (showHammer == false) {
                return;
            }
            // Turn off the hammer display and play a hammer sound
            showHammer = false;

            SoundManager.playHammer();
            
            HammerScreen();
        }
    };

    EventHandler<MouseEvent> shakeHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (showShake == false) {
                return;
            }
            // Turn off the shake display
            showShake = false;
            ShakeScreen(group);
        }
    };

    /**
     * Draw up next shapes method
     *
     * @param noOfNexts: number of next shapes to be drawn
     */
    private void drawUpNext(int noOfNexts) {
        //Create a linked list of shapes from the shape queue
        LinkedList<Shape> shapes = new LinkedList<>(shapeQueue);

        //Iterate through the number of next shapes to be drawn
        for (int i = 0; i < noOfNexts; i++) {
            Shape shape = shapes.get(i);

            //Set default offsets for x, y, and size
            int xOffset = -130;
            int yOffset = 19;
            int sizeOffset = 1;

            //Adjust offsets based on shape type i
            if (shape.getName().equals("i")) {
                xOffset = -115;
                yOffset = 32;
            }

            //Adjust offsets based on shape type z
            if (shape.getName().equals("z")) {
                xOffset = -165;
            }

            //Copy rectangle 1 of the shape
            Rectangle sq1 = new Rectangle(SIZE - sizeOffset, SIZE - sizeOffset);
            sq1.setX(shape.sq1.getX() + xOffset);
            sq1.setY(shape.sq1.getY() + yOffset);
            sq1.setFill(shape.sq1.getFill());
            sq1.setArcHeight(shape.sq1.getArcHeight());
            sq1.setArcWidth(shape.sq1.getArcWidth());

            //Copy rectangle 2 of the shape
            Rectangle sq2 = new Rectangle(SIZE - sizeOffset, SIZE - sizeOffset);
            sq2.setX(shape.sq2.getX() + xOffset);
            sq2.setY(shape.sq2.getY() + yOffset);
            sq2.setFill(shape.sq2.getFill());
            sq2.setArcHeight(shape.sq2.getArcHeight());
            sq2.setArcWidth(shape.sq2.getArcWidth());

            //Copy rectangle 3 of the shape
            Rectangle sq3 = new Rectangle(SIZE - sizeOffset, SIZE - sizeOffset);
            sq3.setX(shape.sq3.getX() + xOffset);
            sq3.setY(shape.sq3.getY() + yOffset);
            sq3.setFill(shape.sq3.getFill());
            sq3.setArcHeight(shape.sq3.getArcHeight());
            sq3.setArcWidth(shape.sq3.getArcWidth());

            //Copy rectangle 4 of the shape
            Rectangle sq4 = new Rectangle(SIZE - sizeOffset, SIZE - sizeOffset);
            sq4.setX(shape.sq4.getX() + xOffset);
            sq4.setY(shape.sq4.getY() + yOffset);
            sq4.setFill(shape.sq4.getFill());
            sq4.setArcHeight(shape.sq4.getArcHeight());
            sq4.setArcWidth(shape.sq4.getArcWidth());

            //Clear the pane and add the rectangle copies of each square to the next pane
            nextPanes.get(i).getChildren().clear();
            nextPanes.get(i).getChildren().addAll(sq1, sq2, sq3, sq4);

            if (shape.block != null) {
                shape.block.setX(sq3.getX());
                shape.block.setY(sq3.getY());
                nextPanes.get(i).getChildren().add(shape.block);
            }
            if (shape.rocket != null) {
                shape.rocket.setX(sq3.getX());
                shape.rocket.setY(sq3.getY());
                nextPanes.get(i).getChildren().add(shape.rocket);
            }
            if (shape.water != null) {
                shape.water.setX(sq3.getX());
                shape.water.setY(sq3.getY());
                nextPanes.get(i).getChildren().add(shape.water);
            }
            if (shape.flex != null) {
                shape.flex.setX(sq3.getX());
                shape.flex.setY(sq3.getY());
                nextPanes.get(i).getChildren().add(shape.flex);
            }
            if (shape.bomb != null) {
                shape.bomb.setX(sq3.getX());
                shape.bomb.setY(sq3.getY());
                nextPanes.get(i).getChildren().add(shape.bomb);
            }
        }
    }

    /*
	 * Timer function, it will be called every time the game timers ticks
     */
    private TimerTask getTimerTask() {
        return new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        // level automatically increase by 1000 scores (eg. 0~1000 : level 1, 1001~2000 : level2 ...)
                        if (score > 1000 * level) {
                            levelUp();
                        }
                        
                        // show or hide hammer and shaker
                        if (timeAdder % (2000 / delta) == 0) {
                            timerTicks++;
                            if (!showHammer) {
                                timehammer++;
                                hammerText.setText(Integer.toString(nexthammer - timehammer));
                            }
                            if (!showShake) {
                                timeshake++;
                                shakeText.setText(Integer.toString(nextshake - timeshake));
                            }
                        }

                        //Show hammer & shaker
                        if (timehammer == nexthammer) {
                            if (!showHammer) {
                                SoundManager.playfound();
                                showHammer = true;
                                timehammer = 0;
                                hammerText.setText("N");
                            }
                        }
                        if (timeshake == nextshake) {
                            if (!showShake) {
                                SoundManager.playfound();
                                showShake = true;
                                timeshake = 0;
                                shakeText.setText("B");
                            }
                        }
                        showHammer();
                        showShake();

                        //Reached the top = Game Over
                        if (top == 1) {
                            // GAME OVER
                            SoundManager.playlose();
                            game = false;
                            stopBtn.setDisable(true);

                            //Cancel game timer
                            gameTimer.cancel();

                            // Pop-up dialog for game over, includes input box for name
                            JFrame frame = new JFrame("Rankboard");

                            String s = (String) JOptionPane.showInputDialog(
                                frame,
                                "Game Over\n"
                                + "Your name:",
                                "Save your score...",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                "User");

                            rank_scores[rank_count] = score;
                            rank_names[rank_count] = s;
                            rank_count++;
                            
                            // shows rank table, it includes name and score, automatically sort by decrease
                            
                            String[] columnNames = {"Name", "Score"};
                            Object[][] data = new Object[rank_count][2];
                            for (int i = 0; i < rank_count; i++) {
                                data[i][0] = rank_names[i];
                                data[i][1] = rank_scores[i];
                            }
                            
                            // sort
                            for (int i = 0; i < rank_count; i++) {
                                for (int j = i + 1; j < rank_count; j++) {
                                    if ((int) data[i][1] < (int) data[j][1]) {
                                        Object temp = data[i][0];
                                        data[i][0] = data[j][0];
                                        data[j][0] = temp;
                                        temp = data[i][1];
                                        data[i][1] = data[j][1];
                                        data[j][1] = temp;
                                    }
                                }
                            }
                            JTable table = new JTable(data, columnNames);
                            frame.setSize(new Dimension(400, 200));
                            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                            frame.add(table);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);

                            //Add game over text
                            Text over = new Text("GAME OVER");
                            over.setFill(Color.YELLOW);
                            over.setStyle("-fx-font: 70 cambria;");
                            over.setY(250);
                            over.setX(200);

                            //Add restart button to the game
                            Button restartBtn = new Button("Restart Game");
                            restartBtn.setLayoutX(300);
                            restartBtn.setLayoutY(300);
                            Styler.setBtnStyle(restartBtn);
                            restartBtn.setFocusTraversable(false);
                            group.getChildren().addAll(over, restartBtn);

                            //Add event handler to the restart button
                            restartBtn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    SoundManager.stopBackground();

                                    //Get Stage objec of current window
                                    Stage stageOld = (Stage) restartBtn.getScene().getWindow();

                                    group = new Pane();

                                    //Create new JavaFX scene and replace current scene with new one
                                    scene = new Scene(group, MAXIMUM_X + 384, MAXIMUM_Y);
                                    stageOld.setScene(scene);

                                    //Cancel game timer and reset everything to start new game
                                    gameTimer.cancel();

                                    startGame();
                                }
                            });
                        }

                        //If game is currently being played
                        if (game) {
                            //Move object down
                            if (timeAdder % (1000 / delta) == 0) {
                                MoveDown(object);
                            } else if (object.rocket != null && timeAdder % (250 / delta) == 0) {
                                MoveDown(object);
                            }

                            if (timeAdder - comboTime == (5000 / delta)) {
                                comboTime = 0;
                                COMBO_COUNT = 0;
                                combotext.setText("");
                            }

                            //Update scores and lines
                            scoretext.setText(Integer.toString(score));
                            linestext.setText(String.valueOf(linesNo));
                        }
                        timeAdder++;
                    }
                });
            }
        };
    }

    /**
     * Show of hide alpha black rect according to showHammer flag
     */
    private void showHammer() {
        if (!showHammer) {
            if (!group.getChildren().contains(cdhammer)) {
                group.getChildren().add(cdhammer);
            }
        }
        else {
            group.getChildren().remove(cdhammer);
        }
    }

    /**
     * Show of hide alpha black rect according to showHammer flag
     */
    private void showShake() {
        if (!showShake) {
            if (!group.getChildren().contains(cdshake)) {
                group.getChildren().add(cdshake);
            }
        }
        else {
            group.getChildren().remove(cdshake);
        }
    }

    /**
     * Show level time continuously, Update with every timer tick
     */
//    private void showTime() {
//
//        //Calculate minutes::seconds from seconds
//        int minutes = (seconds % 3600) / 60;
//        int secondsC = seconds % 60;
//
//        //Update time in time label
//        String timeString = String.format("%02d:%02d", minutes, secondsC);
//        timeText.setText(timeString);
//    }
    /**
     * Increases the level of the game and updates the speed of the falling
     * shape accordingly.
     */
    private void levelUp() {
        // Reset the time for this level
        seconds = LEVEL_DURATION;

        // Increase the level by 1
        level++;

        // Increase the speed by 0.2
        speed = speed + 0.2;

        // Update the level display
        leveltext.setText("Level " + level);

        // Get the current timer task
        TimerTask task = getTimerTask();

        // Schedule the timer with the new speed
        gameTimer.schedule(task, 0, (int) (100 / speed));
        delta = 100;

        // Play a sound to indicate that the level has increased
        SoundManager.playLevel();
    }

    /**
     * Sets the key event handler for UP, DOWN, LEFT, RIGHT keys to move the
     * given shape accordingly
     *
     * @form The shape that is being moved
     */
    private void moveOnKeyPress(Shape form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Checks which key is pressed and moves the shape accordingly
                if (game == false || isStarted == false) {
                    return;
                }
                switch (event.getCode()) {
                    case RIGHT:
                    case D:
                        GameController.MoveToRight(form);
                        break;
                    case DOWN:
                        MoveDown(form);
                        break;
                    case CONTROL:
                        MoveDownImmediately(form);
                        break;
                    case LEFT:
                    case A:
                        GameController.MoveToLeft(form);
                        break;
                    case UP:
                    case S:
                        if (form.block == null) {
                            Shape.changeShape(form);
                        }
                        break;
                    case Z:
                        SetIndexPattern(0, form);
                        break;
                    case X:
                        SetIndexPattern(1, form);
                        break;
                    case C:
                        SetIndexPattern(2, form);
                        break;
                    case V:
                        SetIndexPattern(3, form);
                        break;
                    case B:
                        if (showShake) {
                            ShakeScreen(group);
                            showShake = false;
                        }
                        break;
                    case N:
                        if (showHammer) {
                            HammerScreen();
                            showHammer = false;
                        }
                        break;
                }
                SetHintShape(form);
            }
        });
    }

    /**
     * Select one pattern of next 4 patterns
     *
     * @param index
     */
    private void SetIndexPattern(int index, Shape form) {

        // remove current pattern
        group.getChildren().remove(form.sq1);
        group.getChildren().remove(form.sq2);
        group.getChildren().remove(form.sq3);
        group.getChildren().remove(form.sq4);
        if (form.bomb != null) {
            group.getChildren().remove(form.bomb);
        } else if (form.block != null) {
            group.getChildren().remove(form.block);
        } else if (form.flex != null) {
            group.getChildren().remove(form.flex);
        } else if (form.rocket != null) {
            group.getChildren().remove(form.rocket);
        } else if (form.water != null) {
            group.getChildren().remove(form.water);
        }

        // Set the current shape to the next shape in the queue, generate a new shape, and draw it in the "up next" section
        curShape = shapeQueue.remove(index);
        shapeQueue.add(ShapeFactory.getShape());
        drawUpNext(4);

        object = curShape;
        group.getChildren().addAll(curShape.sq1, curShape.sq2, curShape.sq3, curShape.sq4);

        if (curShape.bomb != null) {
            curShape.bomb.setX(curShape.sq3.getX());
            curShape.bomb.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.bomb);
        }
        if (curShape.block != null) {
            curShape.block.setX(curShape.sq3.getX());
            curShape.block.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.block);
        }
        if (curShape.flex != null) {
            curShape.flex.setX(curShape.sq3.getX());
            curShape.flex.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.flex);
        }
        if (curShape.rocket != null) {
            curShape.rocket.setX(curShape.sq3.getX());
            curShape.rocket.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.rocket);
        }
        if (curShape.water != null) {
            curShape.water.setX(curShape.sq3.getX());
            curShape.water.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.water);
        }

        // Start listening for key presses to move the new shape
        moveOnKeyPress(curShape);
    }

    /**
     * Removes rows from the game when the lines are full
     *
     * @param pane
     */
    private void RemoveRows(Pane pane) {

        // An ArrayList to store the indexes of completed lines
        ArrayList<Integer> lines = new ArrayList<Integer>();

        // Check each row in the GRID for filled squares
        int full = 0;
        for (int i = 0; i < GRID[0].length; i++) {
            for (int j = 0; j < GRID.length; j++) {
                if (GRID[j][i] > 0) {
                    full++;
                }
            }
            // If the row is full, add its index to the ArrayList
            if (full == GRID.length) {
                lines.add(i);
            }
            full = 0;
        }

        // when you delete lines many times within 5s, combo will increase
        if (lines.size() > 0) {
            COMBO_COUNT++;
            if (COMBO_COUNT == 1) {
                combotext.setText("1 combo!");
            } else {
                combotext.setText(Integer.toString(COMBO_COUNT) + " combos!");
                // add bonus score according to combo count
                if (COMBO_COUNT == 2) {
                    score += 100;
                } else if (COMBO_COUNT == 3) {
                    score += 300;
                } else if (COMBO_COUNT == 4) {
                    score += 600;
                } else if (COMBO_COUNT >= 5) {
                    score += 1000;
                }
            }
            comboTime = timeAdder;
        }
        // Remove the completed lines
        breakLines(pane, lines);
    }

    /**
     * Break the lines in the pane and adjust the game accordingly
     *
     * @param pane
     * @param lines
     */
    private void breakLines(Pane pane, ArrayList<Integer> lines) {
        if (lines.size() > 0) {
            SoundManager.playBreakSound();
            score += 50;
            linesNo += lines.size();

            ArrayList<Node> rects = new ArrayList<Node>();

            // While there are lines to break, remove the completed row, adjust the grid and score, and update the pane
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle) {
                        Rectangle a = (Rectangle) node;
                        // remove rects only in the tetris board
                        if ((int)a.getX() / SIZE < 12 && (int)a.getY() / SIZE < 24)
                            rects.add(node);
                    }
                }

                // Remove the completed row
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        pane.getChildren().remove(node);
                    } else if (a.getY() < lines.get(0) * SIZE) {
                        a.setY(a.getY() + SIZE);
                    }
                }

                for (int i = 0; i < GRID.length; i++) {
                    for (int j = lines.get(0); j > 0; j--) {
                        GRID[i][j] = GRID[i][j - 1];
                    }
                    GRID[i][0] = 0;
                }
                lines.remove(0);
                rects.clear();

            } while (lines.size() > 0);
        }
    }

    /**
     * Move the given shape down one step on the grid
     *
     * @param form
     */
    private void MoveDown(Shape form) {
        // Check if the shape has reached the bottom or collided with another shape
        if (form.sq1.getY() == MAXIMUM_Y - SIZE || form.sq2.getY() == MAXIMUM_Y - SIZE || form.sq3.getY() == MAXIMUM_Y - SIZE
            || form.sq4.getY() == MAXIMUM_Y - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {

            MoveDownImmediately(form);

        } // If the shape is not at the bottom, move it down one step if there are no obstructions
        else if (form.sq1.getY() + MOVE < MAXIMUM_Y && form.sq2.getY() + MOVE < MAXIMUM_Y && form.sq3.getY() + MOVE < MAXIMUM_Y
            && form.sq4.getY() + MOVE < MAXIMUM_Y) {

            int movea = GRID[(int) form.sq1.getX() / SIZE][((int) form.sq1.getY() / SIZE) + 1];
            int moveb = GRID[(int) form.sq2.getX() / SIZE][((int) form.sq2.getY() / SIZE) + 1];
            int movec = GRID[(int) form.sq3.getX() / SIZE][((int) form.sq3.getY() / SIZE) + 1];
            int moved = GRID[(int) form.sq4.getX() / SIZE][((int) form.sq4.getY() / SIZE) + 1];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                // Move the squares of the shape down by one step
                form.sq1.setY(form.sq1.getY() + MOVE);
                form.sq2.setY(form.sq2.getY() + MOVE);
                form.sq3.setY(form.sq3.getY() + MOVE);
                form.sq4.setY(form.sq4.getY() + MOVE);

                // If the shape has other optinos, move them down by one step as well
                if (form.bomb != null) {
                    form.bomb.setY(form.bomb.getY() + MOVE);
                } else if (form.block != null) {
                    form.block.setY(form.block.getY() + MOVE);
                } else if (form.flex != null) {
                    form.flex.setY(form.flex.getY() + MOVE);
                } else if (form.rocket != null) {
                    form.rocket.setY(form.rocket.getY() + MOVE);
                } else if (form.water != null) {
                    form.water.setY(form.water.getY() + MOVE);
                }
            }
            SetHintShape(form);
        }
    }

    // calculate hint shape from current shape
    private void SetHintShape(Shape form) {
        int y1, y2, y3, y4, dif = 23;
        
        // 4 y positions that grid value is 0, empty
        
        for (y1 = (int) form.sq1.getY() / SIZE + 1; y1 < 24; y1++) {
            if (GRID[(int) form.sq1.getX() / SIZE][y1] > 0) {
                dif = Math.min(dif, y1 - (int) form.sq1.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y1 - (int) form.sq1.getY() / SIZE);
        for (y2 = (int) form.sq2.getY() / SIZE + 1; y2 < 24; y2++) {
            if (GRID[(int) form.sq2.getX() / SIZE][y2] > 0) {
                dif = Math.min(dif, y2 - (int) form.sq2.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y2 - (int) form.sq2.getY() / SIZE);
        for (y3 = (int) form.sq3.getY() / SIZE + 1; y3 < 24; y3++) {
            if (GRID[(int) form.sq3.getX() / SIZE][y3] > 0) {
                dif = Math.min(dif, y3 - (int) form.sq3.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y3 - (int) form.sq3.getY() / SIZE);
        for (y4 = (int) form.sq4.getY() / SIZE + 1; y4 < 24; y4++) {
            if (GRID[(int) form.sq4.getX() / SIZE][y4] > 0) {
                dif = Math.min(dif, y4 - (int) form.sq4.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y4 - (int) form.sq4.getY() / SIZE);
        dif--;

        // set hint shape
        hintShape.sq1.setX(form.sq1.getX());
        hintShape.sq2.setX(form.sq2.getX());
        hintShape.sq3.setX(form.sq3.getX());
        hintShape.sq4.setX(form.sq4.getX());
        if (form.flex == null && form.water == null) {
            hintShape.sq1.setY(((int) form.sq1.getY() / SIZE + dif) * SIZE);
            hintShape.sq2.setY(((int) form.sq2.getY() / SIZE + dif) * SIZE);
            hintShape.sq3.setY(((int) form.sq3.getY() / SIZE + dif) * SIZE);
            hintShape.sq4.setY(((int) form.sq4.getY() / SIZE + dif) * SIZE);
        } else {
            // save grid values to temporary array
            
            int[][] newGrid = new int[12][24];
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 24; j++) {
                    newGrid[i][j] = GRID[i][j];
                }
            }

            // get 4 y positions which flex will go down
            if (form.flex != null) {
                int j;
                
                for (j = 1; j <= 23; j++) {
                    if (newGrid[(int) form.sq1.getX() / SIZE][j] > 0) {
                        newGrid[(int) form.sq1.getX() / SIZE][j - 1] = form.type;
                        hintShape.sq1.setY((j - 1) * SIZE);
                        break;
                    }
                }
                if (j == 24) {
                    newGrid[(int) form.sq1.getX() / SIZE][23] = form.type;
                    hintShape.sq1.setY(23 * SIZE);
                }
                
                for (j = 1; j <= 23; j++) {
                    if (newGrid[(int) form.sq2.getX() / SIZE][j] > 0) {
                        newGrid[(int) form.sq2.getX() / SIZE][j - 1] = form.type;
                        hintShape.sq2.setY((j - 1) * SIZE);
                        break;
                    }
                }
                if (j == 24) {
                    newGrid[(int) form.sq2.getX() / SIZE][23] = form.type;
                    hintShape.sq2.setY(23 * SIZE);
                }
                
                for (j = 1; j <= 23; j++) {
                    if (newGrid[(int) form.sq3.getX() / SIZE][j] > 0) {
                        newGrid[(int) form.sq3.getX() / SIZE][j - 1] = form.type;
                        hintShape.sq3.setY((j - 1) * SIZE);
                        break;
                    }
                }
                if (j == 24) {
                    newGrid[(int) form.sq3.getX() / SIZE][23] = form.type;
                    hintShape.sq3.setY(23 * SIZE);
                }
                
                for (j = 1; j <= 23; j++) {
                    if (newGrid[(int) form.sq4.getX() / SIZE][j] > 0) {
                        newGrid[(int) hintShape.sq4.getX() / SIZE][j - 1] = form.type;
                        hintShape.sq4.setY((j - 1) * SIZE);
                        break;
                    }
                }
                if (j == 24) {
                    newGrid[(int) form.sq4.getX() / SIZE][23] = form.type;
                    hintShape.sq4.setY(23 * SIZE);
                }

            } else if (form.water != null) {    //water
                int j;
                for (j = 23; j >= 0; j--) {
                    if (newGrid[(int) form.sq1.getX() / SIZE][j] == 0) {
                        newGrid[(int) form.sq1.getX() / SIZE][j] = form.type;
                        hintShape.sq1.setY(j * SIZE);
                        break;
                    }
                }
                for (j = 23; j >= 0; j--) {
                    if (newGrid[(int) form.sq2.getX() / SIZE][j] == 0) {
                        newGrid[(int) form.sq2.getX() / SIZE][j] = form.type;
                        hintShape.sq2.setY(j * SIZE);
                        break;
                    }
                }
                for (j = 23; j >= 0; j--) {
                    if (newGrid[(int) form.sq3.getX() / SIZE][j] == 0) {
                        newGrid[(int) form.sq3.getX() / SIZE][j] = form.type;
                        hintShape.sq3.setY(j * SIZE);
                        break;
                    }
                }
                for (j = 23; j >= 0; j--) {
                    if (newGrid[(int) form.sq4.getX() / SIZE][j] == 0) {
                        newGrid[(int) form.sq4.getX() / SIZE][j] = form.type;
                        hintShape.sq4.setY(j * SIZE);
                        break;
                    }
                }
            }
        }

    }

    /**
     * Check if moving square A down one step will result in a collision with an
     * obstacle
     *
     * @param form
     */
    private boolean moveA(Shape form) {
        return (GRID[(int) form.sq1.getX() / SIZE][((int) form.sq1.getY() / SIZE) + 1] >= 1);
    }

    /**
     * Check if moving square B down one step will result in a collision with an
     * obstacle
     *
     * @param form
     */
    private boolean moveB(Shape form) {
        return (GRID[(int) form.sq2.getX() / SIZE][((int) form.sq2.getY() / SIZE) + 1] >= 1);
    }

    /**
     * Check if moving square C down one step will result in a collision with an
     * obstacle
     *
     * @param form
     */
    private boolean moveC(Shape form) {
        return (GRID[(int) form.sq3.getX() / SIZE][((int) form.sq3.getY() / SIZE) + 1] >= 1);
    }

    /**
     * Check if moving square D down one step will result in a collision with an
     * obstacle
     *
     * @param form
     */
    private boolean moveD(Shape form) {
        return (GRID[(int) form.sq4.getX() / SIZE][((int) form.sq4.getY() / SIZE) + 1] >= 1);
    }

    /**
     * Move the given shape down to the bottom immediately
     *
     * @param form
     */
    private void MoveDownImmediately(Shape form) {
        int y1, y2, y3, y4, dif = 23;
        for (y1 = (int) form.sq1.getY() / SIZE + 1; y1 < 24; y1++) {
            if (GRID[(int) form.sq1.getX() / SIZE][y1] >= 1) {
                dif = Math.min(dif, y1 - (int) form.sq1.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y1 - (int) form.sq1.getY() / SIZE);
        for (y2 = (int) form.sq2.getY() / SIZE + 1; y2 < 24; y2++) {
            if (GRID[(int) form.sq2.getX() / SIZE][y2] >= 1) {
                dif = Math.min(dif, y2 - (int) form.sq2.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y2 - (int) form.sq2.getY() / SIZE);
        for (y3 = (int) form.sq3.getY() / SIZE + 1; y3 < 24; y3++) {
            if (GRID[(int) form.sq3.getX() / SIZE][y3] >= 1) {
                dif = Math.min(dif, y3 - (int) form.sq3.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y3 - (int) form.sq3.getY() / SIZE);
        for (y4 = (int) form.sq4.getY() / SIZE + 1; y4 < 24; y4++) {
            if (GRID[(int) form.sq4.getX() / SIZE][y4] >= 1) {
                dif = Math.min(dif, y4 - (int) form.sq4.getY() / SIZE);
                break;
            }
        }
        dif = Math.min(dif, y4 - (int) form.sq4.getY() / SIZE);
        dif--;

        // If the shape is not a bomb, mark the squares it occupies as filled and play a sound
        if (form.bomb == null && form.flex == null && form.water == null && form.rocket == null && form.block == null) {
            try {
                GRID[(int) form.sq1.getX() / SIZE][(int) form.sq1.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq2.getX() / SIZE][(int) form.sq2.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq3.getX() / SIZE][(int) form.sq3.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq4.getX() / SIZE][(int) form.sq4.getY() / SIZE + dif] = form.type;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            form.sq1.setY(form.sq1.getY() + dif * SIZE);
            form.sq2.setY(form.sq2.getY() + dif * SIZE);
            form.sq3.setY(form.sq3.getY() + dif * SIZE);
            form.sq4.setY(form.sq4.getY() + dif * SIZE);

            SoundManager.playPlacedSound();
        }

        if (form.bomb != null) {
            group.getChildren().remove(form.sq1);
            group.getChildren().remove(form.sq2);
            group.getChildren().remove(form.sq3);
            group.getChildren().remove(form.sq4);
            group.getChildren().remove(form.bomb);

            int[] gx = {-1, 0, 1, -1, 1, -1, 0, 1};
            int[] gy = {-1, -1, -1, 0, 0, 1, 1, 1};

            // delete all rects that bomb shape touched
            for (int i = 0; i < 8; i++) {
                int x, y;
                x = (int) form.sq1.getX() / SIZE + gx[i];
                y = (int) form.sq1.getY() / SIZE + gy[i] + dif;
                if (x >= 0 && x < 12 && y >= 0 && y < 24) {
                    if (GRID[x][y] > 0) {
                        for (int j = y; j > 0; j--) {
                            GRID[x][j] = GRID[x][j - 1];
                        }
                        GRID[x][0] = 0;
                    }
                }
                x = (int) form.sq2.getX() / SIZE + gx[i];
                y = (int) form.sq2.getY() / SIZE + gy[i] + dif;
                if (x >= 0 && x < 12 && y >= 0 && y < 24) {
                    if (GRID[x][y] > 0) {
                        for (int j = y; j > 0; j--) {
                            GRID[x][j] = GRID[x][j - 1];
                        }
                        GRID[x][0] = 0;
                    }
                }
                x = (int) form.sq3.getX() / SIZE + gx[i];
                y = (int) form.sq3.getY() / SIZE + gy[i] + dif;
                if (x >= 0 && x < 12 && y >= 0 && y < 24) {
                    if (GRID[x][y] > 0) {
                        for (int j = y; j > 0; j--) {
                            GRID[x][j] = GRID[x][j - 1];
                        }
                        GRID[x][0] = 0;
                    }
                }
                x = (int) form.sq4.getX() / SIZE + gx[i];
                y = (int) form.sq4.getY() / SIZE + gy[i] + dif;
                if (x >= 0 && x < 12 && y >= 0 && y < 24) {
                    if (GRID[x][y] > 0) {
                        for (int j = y; j > 0; j--) {
                            GRID[x][j] = GRID[x][j - 1];
                        }
                        GRID[x][0] = 0;
                    }
                }
            }

            UpdateScreen(group);

            SoundManager.playExplode();
        } else if (form.block != null) {
            group.getChildren().remove(form.block);

            try {
                GRID[(int) form.sq1.getX() / SIZE][(int) form.sq1.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq2.getX() / SIZE][(int) form.sq2.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq3.getX() / SIZE][(int) form.sq3.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq4.getX() / SIZE][(int) form.sq4.getY() / SIZE + dif] = form.type;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            form.sq1.setY(form.sq1.getY() + dif * SIZE);
            form.sq2.setY(form.sq2.getY() + dif * SIZE);
            form.sq3.setY(form.sq3.getY() + dif * SIZE);
            form.sq4.setY(form.sq4.getY() + dif * SIZE);
        } else if (form.flex != null) {
            group.getChildren().remove(form.flex);
            int j;
            for (j = 1; j <= 23; j++) {
                if (GRID[(int) form.sq1.getX() / SIZE][j] > 0) {
                    GRID[(int) form.sq1.getX() / SIZE][j - 1] = form.type;
                    form.sq1.setY((j - 1) * SIZE);
                    break;
                }
            }
            if (j == 24) {
                GRID[(int) form.sq1.getX() / SIZE][23] = form.type;
                form.sq1.setY(23 * SIZE);
            }
            for (j = 1; j <= 23; j++) {
                if (GRID[(int) form.sq2.getX() / SIZE][j] > 0) {
                    GRID[(int) form.sq2.getX() / SIZE][j - 1] = form.type;
                    form.sq2.setY((j - 1) * SIZE);
                    break;
                }
            }
            if (j == 24) {
                GRID[(int) form.sq2.getX() / SIZE][23] = form.type;
                form.sq2.setY(23 * SIZE);
            }
            for (j = 1; j <= 23; j++) {
                if (GRID[(int) form.sq3.getX() / SIZE][j] > 0) {
                    GRID[(int) form.sq3.getX() / SIZE][j - 1] = form.type;
                    form.sq3.setY((j - 1) * SIZE);
                    break;
                }
            }
            if (j == 24) {
                GRID[(int) form.sq3.getX() / SIZE][23] = form.type;
                form.sq3.setY(23 * SIZE);
            }
            for (j = 1; j <= 23; j++) {
                if (GRID[(int) form.sq4.getX() / SIZE][j] > 0) {
                    GRID[(int) form.sq4.getX() / SIZE][j - 1] = form.type;
                    form.sq4.setY((j - 1) * SIZE);
                    break;
                }
            }
            if (j == 24) {
                GRID[(int) form.sq4.getX() / SIZE][23] = form.type;
                form.sq4.setY(23 * SIZE);
            }
        } else if (form.rocket != null) {
            group.getChildren().remove(form.rocket);

            try {
                GRID[(int) form.sq1.getX() / SIZE][(int) form.sq1.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq2.getX() / SIZE][(int) form.sq2.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq3.getX() / SIZE][(int) form.sq3.getY() / SIZE + dif] = form.type;
                GRID[(int) form.sq4.getX() / SIZE][(int) form.sq4.getY() / SIZE + dif] = form.type;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            form.sq1.setY(form.sq1.getY() + dif * SIZE);
            form.sq2.setY(form.sq2.getY() + dif * SIZE);
            form.sq3.setY(form.sq3.getY() + dif * SIZE);
            form.sq4.setY(form.sq4.getY() + dif * SIZE);
        } else if (form.water != null) {
            group.getChildren().remove(form.water);
            int j;
            for (j = 23; j >= 0; j--) {
                if (GRID[(int) form.sq1.getX() / SIZE][j] == 0) {
                    GRID[(int) form.sq1.getX() / SIZE][j] = form.type;
                    form.sq1.setY(j * SIZE);
                    break;
                }
            }
            for (j = 23; j >= 0; j--) {
                if (GRID[(int) form.sq2.getX() / SIZE][j] == 0) {
                    GRID[(int) form.sq2.getX() / SIZE][j] = form.type;
                    form.sq2.setY(j * SIZE);
                    break;
                }
            }
            for (j = 23; j >= 0; j--) {
                if (GRID[(int) form.sq3.getX() / SIZE][j] == 0) {
                    GRID[(int) form.sq3.getX() / SIZE][j] = form.type;
                    form.sq3.setY(j * SIZE);
                    break;
                }
            }
            for (j = 23; j >= 0; j--) {
                if (GRID[(int) form.sq4.getX() / SIZE][j] == 0) {
                    GRID[(int) form.sq4.getX() / SIZE][j] = form.type;
                    form.sq4.setY(j * SIZE);
                    break;
                }
            }
        }

        // Remove any completed rows and check if the shape is a bomb, if so, remove it and play a sound
        RemoveRows(group);

        // Set the current shape to the next shape in the queue, generate a new shape, and draw it in the "up next" section
        curShape = shapeQueue.poll();
        shapeQueue.add(ShapeFactory.getShape());
        drawUpNext(4);
        object = curShape;
        group.getChildren().addAll(curShape.sq1, curShape.sq2, curShape.sq3, curShape.sq4);

        // game is over when you can't put current shape down
        if (GRID[(int) object.sq1.getX() / SIZE][(int) object.sq1.getY() / SIZE] > 0
            || GRID[(int) object.sq2.getX() / SIZE][(int) object.sq2.getY() / SIZE] > 0
            || GRID[(int) object.sq3.getX() / SIZE][(int) object.sq3.getY() / SIZE] > 0
            || GRID[(int) object.sq4.getX() / SIZE][(int) object.sq4.getY() / SIZE] > 0) {
            top = 1;
            return;
        }

        // If the current shape is a bomb, add it to the group
        if (curShape.bomb != null) {
            curShape.bomb.setX(curShape.sq3.getX());
            curShape.bomb.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.bomb);
        } else if (curShape.block != null) {
            curShape.block.setX(curShape.sq3.getX());
            curShape.block.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.block);
        } else if (curShape.flex != null) {
            curShape.flex.setX(curShape.sq3.getX());
            curShape.flex.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.flex);
        } else if (curShape.water != null) {
            curShape.water.setX(curShape.sq3.getX());
            curShape.water.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.water);
        } else if (curShape.rocket != null) {
            curShape.rocket.setX(curShape.sq3.getX());
            curShape.rocket.setY(curShape.sq3.getY());
            group.getChildren().add(curShape.rocket);
        }

        // Start listening for key presses to move the new shape
        moveOnKeyPress(curShape);
    }

    //Shaking screen function
    private void ShakeScreen(Pane pane) {
        int i, j, cnt;
        for (j = 23; j >= 0; j--) {
            int[] newGrid = new int[24];
            cnt = 0;
            for (i = 0; i < 12; i++) {
                if (GRID[i][j] > 0) {
                    newGrid[cnt] = GRID[i][j];
                    cnt++;
                }
            }
            if (cnt == 0) {
                break;
            } else {
                for (i = cnt; i < 12; i++) {
                    newGrid[i] = 0;
                }
                for (i = 0; i < 12; i++) {
                    GRID[i][j] = newGrid[i];
                }
            }
        }
        
        ArrayList<Node> rects = new ArrayList<Node>();
        for (Node node : pane.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle a = (Rectangle)node;
                if (a.getX() < 12 * SIZE && a.getY() < 24 * SIZE)
                    rects.add(node);
            }
        }
        for (Node node : rects) {
            Rectangle rect = (Rectangle) node;
            pane.getChildren().remove(rect);
        }

        ArrayList<String> names = new ArrayList<String>();
        names.add("j");
        names.add("l");
        names.add("o");
        names.add("s");
        names.add("t");
        names.add("z");
        names.add("i");
        names.add("h");

        for (i = 0; i < GRID.length; i++) {
            for (j = 0; j < GRID[i].length; j++) {
                if (GRID[i][j] > 0) {
                    Shape s = ShapeFactory.getShape();
                    s = new Shape(s.sq1, s.sq2, s.sq3, s.sq4, names.get(GRID[i][j] - 1));
                    Rectangle rect = s.sq1;
                    rect.setX(i * SIZE);
                    rect.setY(j * SIZE);
                    pane.getChildren().add(rect);
                }
            }
        }

        pane.getChildren().addAll(object.sq1, object.sq2, object.sq3, object.sq4);
        if (object.bomb != null) {
            pane.getChildren().remove(object.bomb);
            pane.getChildren().add(object.bomb);
        } else if (object.block != null) {
            pane.getChildren().remove(object.block);
            pane.getChildren().add(object.block);
        } else if (object.water != null) {
            pane.getChildren().remove(object.water);
            pane.getChildren().add(object.water);
        } else if (object.flex != null) {
            pane.getChildren().remove(object.flex);
            pane.getChildren().add(object.flex);
        } else if (object.rocket != null) {
            pane.getChildren().remove(object.rocket);
            pane.getChildren().add(object.rocket);
        }
    }

    // update tetris board with grid array
    private void UpdateScreen(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        for (Node node : pane.getChildren()) {
            if (node instanceof Rectangle) {
                rects.add(node);
            }
        }
        for (Node node : rects) {
            Rectangle rect = (Rectangle) node;
            if ((int)rect.getX() / SIZE < 12 && (int)rect.getY() / SIZE < 24)
                pane.getChildren().remove(rect);
        }

        // array to get shape type
        ArrayList<String> names = new ArrayList<String>();
        names.add("j");
        names.add("l");
        names.add("o");
        names.add("s");
        names.add("t");
        names.add("z");
        names.add("i");
        names.add("h");

        for (int i = 0; i < GRID.length; i++) {
            for (int j = 0; j < GRID[i].length; j++) {
                if (GRID[i][j] > 0) {
                    Shape s = ShapeFactory.getShape();
                    s = new Shape(s.sq1, s.sq2, s.sq3, s.sq4, names.get(GRID[i][j] - 1));
                    Rectangle rect = s.sq1;
                    rect.setX(i * SIZE);
                    rect.setY(j * SIZE);
                    pane.getChildren().add(rect);
                }
            }
        }
    }
}
