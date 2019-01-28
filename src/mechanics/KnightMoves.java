package mechanics;
/**
 *Class KnightMoves
 *
 * Functionalities: Provides direction vectors for the Knight piece object
 */
public enum KnightMoves
{
    /**
     * down right
     */
    DOWNRIGHT(1,2),

    /**
     * up right
     */
    UPRIGHT(1,-2),

    /**
     * down left
     */
    DOWNLEFT(-1,2),

    /**
     * up left
     */
    UPLEFT(-1,-2),

    /**
     * up right 2
     */
    UPRIGHT2(2,-1),

    /**
     * down right 2
     */
    DOWNRIGHT2(2,1),

    /**
     * down left 2
     */
    DOWNLEFT2(-2,1),

    /**
     * up left 2
     */
    UPLEFT2(-2,-1);

    /**
     * Difference in x position.
     */
    public final int deltaKX;

    /**
     * Difference in y position.
     */
    public final int deltaKY;


    /**
     * Constructor.
     *
     * When you set a direction it will construct its correct deltaX and deltaY integers
     * to whatever is assosiated to it in the enum field.
     *
     * @param deltaKX Integer to set its deltaX to
     * @param deltaKY Integer to set its deltaY to
     */
    KnightMoves(final int deltaKX, final int deltaKY) {
	this.deltaKX = deltaKX;
	this.deltaKY = deltaKY;
    }
}
