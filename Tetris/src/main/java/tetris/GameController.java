package tetris;

/**
 * The GameController class contains methods that handle the movement of the
 * falling blocks on the game grid. It provides static variables that represent
 * the game grid size and the maximum values for the x and y coordinates of the
 * game grid. It also provides a 2D integer array that represents the game's
 * grid.
 */
public class GameController {

    public static final int MOVE = Game.MOVE;
    public static final int SIZE = Game.SIZE;
    public static int XMAX = Game.MAXIMUM_X;
    public static int YMAX = Game.MAXIMUM_Y;
    public static int[][] GAME_GRID = Game.GRID;

    /**
     * This method moves a shape to the left by one square if there is space to
     * do so. It checks if each square of the shape can be moved to the left and
     * if there is no obstacle in the way. If the move is possible, the method
     * updates the x-coordinate of each square and the bomb (if there is one).
     *
     * @param form - The shape to move.
     */
    public static void MoveToLeft(Shape form) {
        if (form.sq1.getX() - MOVE >= 0 && form.sq2.getX() - MOVE >= 0 && form.sq3.getX() - MOVE >= 0 && form.sq4.getX() - MOVE >= 0) {
            int moveSq1 = GAME_GRID[((int) form.sq1.getX() / SIZE) - 1][((int) form.sq1.getY() / SIZE)];
            int moveSq2 = GAME_GRID[((int) form.sq2.getX() / SIZE) - 1][((int) form.sq2.getY() / SIZE)];
            int moveSq3 = GAME_GRID[((int) form.sq3.getX() / SIZE) - 1][((int) form.sq3.getY() / SIZE)];
            int moveSq4 = GAME_GRID[((int) form.sq4.getX() / SIZE) - 1][((int) form.sq4.getY() / SIZE)];
            if (moveSq1 == 0 && moveSq1 == moveSq2 && moveSq2 == moveSq3 && moveSq3 == moveSq4) {
                form.sq1.setX(form.sq1.getX() - MOVE);
                form.sq2.setX(form.sq2.getX() - MOVE);
                form.sq3.setX(form.sq3.getX() - MOVE);
                form.sq4.setX(form.sq4.getX() - MOVE);

                if (form.bomb != null) {
                    form.bomb.setX(form.bomb.getX() - MOVE);
                } else if (form.block != null) {
                    form.block.setX(form.block.getX() - MOVE);
                } else if (form.flex != null) {
                    form.flex.setX(form.flex.getX() - MOVE);
                } else if (form.rocket != null) {
                    form.rocket.setX(form.rocket.getX() - MOVE);
                } else if (form.water != null) {
                    form.water.setX(form.water.getX() - MOVE);
                }
            }
        }
    }

    /**
     * This method moves a shape to the right by one square if there is space to
     * do so. It checks if each square of the shape can be moved to the right
     * and if there is no obstacle in the way. If the move is possible, the
     * method updates the x-coordinate of each square and the bomb (if there is
     * one).
     *
     * @param form - The shape to move.
     */
    public static void MoveToRight(Shape form) {
        if (form.sq1.getX() + MOVE <= XMAX - SIZE && form.sq2.getX() + MOVE <= XMAX - SIZE && form.sq3.getX() + MOVE <= XMAX - SIZE && form.sq4.getX() + MOVE <= XMAX - SIZE) {
            int moveSq1 = GAME_GRID[((int) form.sq1.getX() / SIZE) + 1][((int) form.sq1.getY() / SIZE)];
            int moveSq2 = GAME_GRID[((int) form.sq2.getX() / SIZE) + 1][((int) form.sq2.getY() / SIZE)];
            int moveSq3 = GAME_GRID[((int) form.sq3.getX() / SIZE) + 1][((int) form.sq3.getY() / SIZE)];
            int moveSq4 = GAME_GRID[((int) form.sq4.getX() / SIZE) + 1][((int) form.sq4.getY() / SIZE)];
            if (moveSq1 == 0 && moveSq1 == moveSq2 && moveSq2 == moveSq3 && moveSq3 == moveSq4) {
                form.sq1.setX(form.sq1.getX() + MOVE);
                form.sq2.setX(form.sq2.getX() + MOVE);
                form.sq3.setX(form.sq3.getX() + MOVE);
                form.sq4.setX(form.sq4.getX() + MOVE);

                if (form.bomb != null) {
                    form.bomb.setX(form.bomb.getX() + MOVE);
                } else if (form.block != null) {
                    form.block.setX(form.block.getX() + MOVE);
                } else if (form.flex != null) {
                    form.flex.setX(form.flex.getX() + MOVE);
                } else if (form.rocket != null) {
                    form.rocket.setX(form.rocket.getX() + MOVE);
                } else if (form.water != null) {
                    form.water.setX(form.water.getX() + MOVE);
                }
            }
        }
    }

}
