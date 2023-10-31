package tetris;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/*
 * Represent the shape of the blocks
 * Each shape is made up of 4 Rectangles and an optional bomb
 */
public class Shape extends GameObj {

    Rectangle sq1; //Rectangle 1
    Rectangle sq2; //Rectangle 2
    Rectangle sq3; //Rectangle 3
    Rectangle sq4; //Rectangle 4
    ImageView bomb = null; //Bomb
    ImageView water = null;
    ImageView rocket = null;
    ImageView flex = null;
    ImageView block = null;

    //Color of shape and its name
    Color color;
    private String name;

    //Current rotation of the shape
    public int form = 1;

    //grid value of this shape
    public int type = 0;

    /*
	 * Create new shape using 4 rectangles
     */
    public Shape(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        super();
        this.sq1 = a;
        this.sq2 = b;
        this.sq3 = c;
        this.sq4 = d;
        
        this.rocket = null;
        this.bomb = null;
        this.water = null;
        this.flex = null;
        this.block = null;
    }

    /*
	 * Create new shape using 4 rectangles and a shape name
     */
    public Shape(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        super();
        this.sq1 = a;
        this.sq2 = b;
        this.sq3 = c;
        this.sq4 = d;
        this.name = name;
        
        this.rocket = null;
        this.bomb = null;
        this.water = null;
        this.flex = null;
        this.block = null;

        //Change shape color according to its name
        switch (name) {
            case "j":
                color = Color.rgb(0, 174, 239);
                type = 1;
                break;
            case "l":
                color = Color.rgb(247, 148, 29);
                type = 2;
                break;
            case "o":
                color = Color.rgb(0, 169, 157);
                type = 3;
                break;
            case "s":
                color = Color.rgb(236, 0, 140);
                type = 4;
                break;
            case "t":
                color = Color.rgb(141, 198, 63);
                type = 5;
                break;
            case "z":
                color = Color.rgb(168, 100, 168);
                type = 6;
                break;
            case "i":
                color = Color.rgb(255, 97, 97);
                type = 7;
                break;
            case "h":
                color = Color.web("0xFFFFFF", 0.25);
                type = 100;
                break;
        }

        //Make rounded corners all 4 rectangles
        sq1.setArcWidth(7.0);
        sq1.setArcHeight(7.0);
        sq2.setArcWidth(7.0);
        sq2.setArcHeight(7.0);
        sq3.setArcWidth(7.0);
        sq3.setArcHeight(7.0);
        sq4.setArcWidth(7.0);
        sq4.setArcHeight(7.0);

        //Create a new gradient fill for the rectangles
        LinearGradient linearGrad = new LinearGradient(
            0, // start X 
            0, // start Y
            0, // end X
            1, // end Y
            true, // proportional
            CycleMethod.NO_CYCLE, // cycle colors
            // stops
            new Stop(0.1f, color),
            new Stop(1.0f, color.brighter().darker()),
            new Stop(1.0f, color.brighter().brighter()),
            new Stop(1.0f, color.brighter().brighter().brighter()),
            new Stop(1.0f, color.brighter().brighter().brighter().brighter()));

        //Fill shapes with gradient
        this.sq1.setFill(linearGrad);
        this.sq2.setFill(linearGrad);
        this.sq3.setFill(linearGrad);
        this.sq4.setFill(linearGrad);
    }

    public String getName() {
        return name;
    }

    public void changeShape() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }

    /*

     */
    public static void changeShape(Shape form) {
        int f = form.form;
        Rectangle a = form.sq1;
        Rectangle b = form.sq2;
        Rectangle c = form.sq3;
        Rectangle d = form.sq4;
        switch (form.getName()) {
            case "j":
                if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
                    MoveRight(form.sq1);
                    MoveDown(form.sq1);
                    MoveDown(form.sq3);
                    MoveLeft(form.sq3);
                    MoveDown(form.sq4);
                    MoveDown(form.sq4);
                    MoveLeft(form.sq4);
                    MoveLeft(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    MoveLeft(form.sq4);
                    MoveLeft(form.sq4);
                    MoveUp(form.sq4);
                    MoveUp(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
                    MoveLeft(form.sq1);
                    MoveUp(form.sq1);
                    MoveUp(form.sq3);
                    MoveRight(form.sq3);
                    MoveUp(form.sq4);
                    MoveUp(form.sq4);
                    MoveRight(form.sq4);
                    MoveRight(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    MoveRight(form.sq4);
                    MoveRight(form.sq4);
                    MoveDown(form.sq4);
                    MoveDown(form.sq4);
                    form.changeShape();
                    break;
                }
                break;
            case "l":
                if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                    MoveRight(form.sq1);
                    MoveDown(form.sq1);
                    MoveUp(form.sq3);
                    MoveRight(form.sq3);
                    MoveUp(form.sq2);
                    MoveUp(form.sq2);
                    MoveRight(form.sq2);
                    MoveRight(form.sq2);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveRight(form.sq2);
                    MoveRight(form.sq2);
                    MoveDown(form.sq2);
                    MoveDown(form.sq2);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
                    MoveLeft(form.sq1);
                    MoveUp(form.sq1);
                    MoveDown(form.sq3);
                    MoveLeft(form.sq3);
                    MoveDown(form.sq2);
                    MoveDown(form.sq2);
                    MoveLeft(form.sq2);
                    MoveLeft(form.sq2);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveLeft(form.sq2);
                    MoveLeft(form.sq2);
                    MoveUp(form.sq2);
                    MoveUp(form.sq2);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    form.changeShape();
                    break;
                }
                break;
            case "o":
                break;
            case "s":
                if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    MoveUp(form.sq4);
                    MoveUp(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    MoveDown(form.sq4);
                    MoveDown(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    MoveUp(form.sq4);
                    MoveUp(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    MoveDown(form.sq4);
                    MoveDown(form.sq4);
                    form.changeShape();
                    break;
                }
                break;
            case "t":
                if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveDown(form.sq4);
                    MoveLeft(form.sq4);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
                    MoveRight(form.sq1);
                    MoveDown(form.sq1);
                    MoveLeft(form.sq4);
                    MoveUp(form.sq4);
                    MoveUp(form.sq3);
                    MoveRight(form.sq3);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveUp(form.sq4);
                    MoveRight(form.sq4);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
                    MoveLeft(form.sq1);
                    MoveUp(form.sq1);
                    MoveRight(form.sq4);
                    MoveDown(form.sq4);
                    MoveDown(form.sq3);
                    MoveLeft(form.sq3);
                    form.changeShape();
                    break;
                }
                break;
            case "z":
                if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.sq2);
                    MoveRight(form.sq2);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    MoveLeft(form.sq4);
                    MoveLeft(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(form.sq2);
                    MoveLeft(form.sq2);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    MoveRight(form.sq4);
                    MoveRight(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.sq2);
                    MoveRight(form.sq2);
                    MoveLeft(form.sq3);
                    MoveUp(form.sq3);
                    MoveLeft(form.sq4);
                    MoveLeft(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(form.sq2);
                    MoveLeft(form.sq2);
                    MoveRight(form.sq3);
                    MoveDown(form.sq3);
                    MoveRight(form.sq4);
                    MoveRight(form.sq4);
                    form.changeShape();
                    break;
                }
                break;
            case "i":
                if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(form.sq1);
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveRight(form.sq1);
                    MoveUp(form.sq2);
                    MoveRight(form.sq2);
                    MoveDown(form.sq4);
                    MoveLeft(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(form.sq1);
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveLeft(form.sq1);
                    MoveDown(form.sq2);
                    MoveLeft(form.sq2);
                    MoveUp(form.sq4);
                    MoveRight(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(form.sq1);
                    MoveUp(form.sq1);
                    MoveRight(form.sq1);
                    MoveRight(form.sq1);
                    MoveUp(form.sq2);
                    MoveRight(form.sq2);
                    MoveDown(form.sq4);
                    MoveLeft(form.sq4);
                    form.changeShape();
                    break;
                }
                if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(form.sq1);
                    MoveDown(form.sq1);
                    MoveLeft(form.sq1);
                    MoveLeft(form.sq1);
                    MoveDown(form.sq2);
                    MoveLeft(form.sq2);
                    MoveUp(form.sq4);
                    MoveRight(form.sq4);
                    form.changeShape();
                    break;
                }
                break;
        }
        
        if (form.rocket != null) { form.rocket.setX(form.sq3.getX()); form.rocket.setY(form.sq3.getY()); }
        if (form.block != null) { form.block.setX(form.sq3.getX()); form.block.setY(form.sq3.getY()); }
        if (form.water != null) { form.water.setX(form.sq3.getX()); form.water.setY(form.sq3.getY()); }
        if (form.flex != null) { form.flex.setX(form.sq3.getX()); form.flex.setY(form.sq3.getY()); }
        if (form.bomb != null) { form.bomb.setX(form.sq3.getX()); form.bomb.setY(form.sq3.getY()); }
    }

    private static void MoveDown(Rectangle rect) {
        if (rect.getY() + Game.MOVE < Game.MAXIMUM_Y) {
            rect.setY(rect.getY() + Game.MOVE);
        }

    }

    private static void MoveRight(Rectangle rect) {
        if (rect.getX() + Game.MOVE <= Game.MAXIMUM_X - Game.SIZE) {
            rect.setX(rect.getX() + Game.MOVE);
        }
    }

    private static void MoveLeft(Rectangle rect) {
        if (rect.getX() - Game.MOVE >= 0) {
            rect.setX(rect.getX() - Game.MOVE);
        }
    }

    private static void MoveUp(Rectangle rect) {
        if (rect.getY() - Game.MOVE > 0) {
            rect.setY(rect.getY() - Game.MOVE);
        }
    }

    /*
	Check if a rectangle with coordinates (x,y) is inside the game board
	and doesn't collide with other rectangles in the grid.
	param rect - the rectangle to be checked.
	param x - the x-coordinate to move the rectangle.
	param y - the y-coordinate to move the rectangle.
	return true if the move is valid, false otherwise.
     */
    private static boolean cB(Rectangle rect, int x, int y) {
        boolean xb = false;
        boolean yb = false;
        if (x >= 0) {
            xb = rect.getX() + x * Game.MOVE <= Game.MAXIMUM_X - Game.SIZE;
        }
        if (x < 0) {
            xb = rect.getX() + x * Game.MOVE >= 0;
        }
        if (y >= 0) {
            yb = rect.getY() - y * Game.MOVE > 0;
        }
        if (y < 0) {
            yb = rect.getY() + y * Game.MOVE < Game.MAXIMUM_Y;
        }
        return xb && yb && Game.GRID[((int) rect.getX() / Game.SIZE) + x][((int) rect.getY() / Game.SIZE) - y] == 0;
    }

}
