package tetris;
//This class is used to issue draw calls to a Canvas using a buffer.

import javafx.scene.canvas.GraphicsContext;
//The Image class represents graphical images and is used for loading images from a specified URL.
import javafx.scene.image.Image;

// The GameObj class is a superclass for objects in the game
class GameObj {
    // The image and location of the object are stored as attributes

    protected Image image;
    protected double locX, locY;
    // The GraphicsContext is used to draw the object on the screen
    protected GraphicsContext gc;

    public GameObj() {
        super();
    }

    // Constructor for creating a new GameObj with a given GraphicsContext and location
    public GameObj(GraphicsContext gc, double x, double y) {
        this.gc = gc;
        this.locX = x;
        this.locY = y;
    }

    // Updates the object's position on the screen
    public void update() {
        // If the object has an image, draw it on the screen using the GraphicsContext
        if (image != null) {
            gc.drawImage(image, locX, locY, 30, 30);
        }
    }
}
