package mechanics;

/**
 * Class Direction.
 *
 * Functionalities: Predefines the different directions any piece (not Knight) can move.
 * These apply to any Rook, Bishop and Queen piece. Basically any vector possible to move
 * from a position on the board - up, down, left, right, diagonally respectively.
 *
 * These enums have these vectors constructed and accessable.
 */
public enum Direction
{
    /**
     *  up
     */
    UP(0,-1),
    /**
     *  down
     */
    DOWN(0,1),
    /**
     *  left
     */
    LEFT(-1,0),
    /**
     *  right
     */
    RIGHT(1,0),
    /**
     *  up right
     */
    UPRIGHT(1,-1),
    /**
     *  up left
     */
    UPLEFT(-1,-1),
    /**
     *  down right
     */
    DOWNRIGHT(1,1),
    /**
     *  down left
     */
    DOWNLEFT(-1,1);

    /**
     * Difference in x value
     */
    public final int deltaX;

    /**
     * Difference in y value
     */
    public final int deltaY;

    /**
     * Constructor.
     *
     * When you set a direction it will construct its correct deltaX and deltaY integers
     * to whatever is assosiated to it in the enum field.
     *
     * @param deltaX Integer to set its deltaX to
     * @param deltaY Integer to set its deltaY to
     */
    Direction(final int deltaX, final int deltaY) {
	this.deltaX = deltaX;
	this.deltaY = deltaY;
    }
}
