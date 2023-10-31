package tetris;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class ShapeFactory {

    private static Random rand = new Random();

    // This method generates a new shape for the game
    public static Shape getShape() {

        // Randomly select a number between 0 and 99
        int block = (int) (Math.random() * 100);

        // Declare a string variable to hold the name of the shape
        String name;

        // Create four new rectangle objects to form the shape
        Rectangle sq1 = new Rectangle(Game.SIZE - 1, Game.SIZE - 1);
        Rectangle sq2 = new Rectangle(Game.SIZE - 1, Game.SIZE - 1);
        Rectangle sq3 = new Rectangle(Game.SIZE - 1, Game.SIZE - 1);
        Rectangle sq4 = new Rectangle(Game.SIZE - 1, Game.SIZE - 1);

        // Determine which shape to create based on the random number generated
        if (block < 15) {
            // Shape J
            sq1.setX((double) Game.MAXIMUM_X / 2 - Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq2.setY(Game.SIZE);
            sq3.setX(Game.MAXIMUM_X / 2);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq4.setY(Game.SIZE);
            name = "j";
        } else if (block < 30) {
            // Shape L
            sq1.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq2.setY(Game.SIZE);
            sq3.setX(Game.MAXIMUM_X / 2);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq4.setY(Game.SIZE);
            name = "l";
        } else if (block < 45) {
            // Shape O
            sq1.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2);
            sq3.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2);
            sq4.setY(Game.SIZE);
            name = "o";
        } else if (block < 60) {
            // Shape S
            sq1.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2);
            sq3.setX(Game.MAXIMUM_X / 2);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq4.setY(Game.SIZE);
            name = "s";
        } else if (block < 75) {
            // Shape T
            sq1.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2);
            sq3.setX(Game.MAXIMUM_X / 2);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            name = "t";
        } else if (block < 90) {
            // Shape Z
            sq1.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2);
            sq3.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            sq3.setY(Game.SIZE);
            sq4.setX(Game.MAXIMUM_X / 2 + Game.SIZE + Game.SIZE);
            sq4.setY(Game.SIZE);
            name = "z";
        } else {
            // Shape I
            sq1.setX(Game.MAXIMUM_X / 2 - Game.SIZE - Game.SIZE);
            sq2.setX(Game.MAXIMUM_X / 2 - Game.SIZE);
            sq3.setX(Game.MAXIMUM_X / 2);
            sq4.setX(Game.MAXIMUM_X / 2 + Game.SIZE);
            name = "i";
        }

        Shape newShape = new Shape(sq1, sq2, sq3, sq4, name); // create a new Shape object with input parameters

        // randomly decide whether to add a bomb image to the Shape
        int random = rand.nextInt(15);
        if (random == 7) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/imgs/bomb2.png")); // load the bomb image from file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView bombView = new ImageView();
            bombView.setImage(image); // set the bomb image as the image for the ImageView

            // position the bomb ImageView based on the Shape's name
            bombView.setX(sq3.getX());
            bombView.setY(sq3.getY());
            bombView.setFitWidth(Game.SIZE - 1);
            bombView.setFitHeight(Game.SIZE - 1);
            bombView.setPreserveRatio(true);

            newShape.bomb = bombView; // set the Shape's bomb attribute to the bomb ImageView
        } else if (random == 6) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/imgs/block2.png")); // load the bomb image from file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView view = new ImageView();
            view.setImage(image); // set the bomb image as the image for the ImageView

            // position the bomb ImageView based on the Shape's name
            
            view.setX(sq3.getX());
            view.setY(sq3.getY());
            view.setFitWidth(Game.SIZE - 1);
            view.setFitHeight(Game.SIZE - 1);
            view.setPreserveRatio(true);

            newShape.block = view; // set the Shape's bomb attribute to the bomb ImageView
        } else if (random == 5) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/imgs/flex2.png")); // load the bomb image from file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView view = new ImageView();
            view.setImage(image); // set the bomb image as the image for the ImageView

            // position the bomb ImageView based on the Shape's name
            
            view.setX(sq3.getX());
            view.setY(sq3.getY());
            view.setFitWidth(Game.SIZE -1);
            view.setFitHeight(Game.SIZE - 1);
            view.setPreserveRatio(true);

            newShape.flex = view; // set the Shape's bomb attribute to the bomb ImageView
        } else if (random == 4) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/imgs/rocket2.png")); // load the bomb image from file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView view = new ImageView();
            view.setImage(image); // set the bomb image as the image for the ImageView

            // position the bomb ImageView based on the Shape's name
            
            view.setX(sq3.getX());
            view.setY(sq3.getY());
            view.setFitWidth(Game.SIZE - 1);
            view.setFitHeight(Game.SIZE - 1);
            view.setPreserveRatio(true);

            newShape.rocket = view; // set the Shape's bomb attribute to the bomb ImageView
        } else if (random == 3) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/imgs/water2.png")); // load the bomb image from file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView view = new ImageView();
            view.setImage(image); // set the bomb image as the image for the ImageView

            // position the bomb ImageView based on the Shape's name
            
            view.setX(sq3.getX());
            view.setY(sq3.getY());
            view.setFitWidth(Game.SIZE - 1);
            view.setFitHeight(Game.SIZE - 1);
            view.setPreserveRatio(true);

            newShape.water = view; // set the Shape's bomb attribute to the bomb ImageView
        }

        return newShape;
    }
}
