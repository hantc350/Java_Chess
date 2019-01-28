package mechanics;

/**
 * Class Coords
 *
 * Functionalities: All pieces location on the board is of an Coords object type which consists of a x-coordinate
 * and a y-coordinate along with simple methods such as getters, setters and equals.
 */
public class Coords {
    private int x = 0;
    private int y = 0;

    /**
     * Constructor.
     * Instantiated with a x- and y-coordinate.
     * @param x integer x - what the x-coordinate is set to
     * @param y integer y - what the y-coordinate is set to
     */
    public Coords(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor.
     *
     * Instantiated without a x- and y-coordinate.
     *
     * This is to make it simple to create empty piece objects that
     * doesnt care of where its located as it will be overridden later on.
     */
    public Coords(){
    }

    /**
     * Getter for x-coordinate.
     * @return int
     */
    public int getX(){
        return x;
    }

    /**
     * Getter for y-coordinate.
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter for x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Compares two Coords Objects to see if they have the same position coordinates.
     *
     * @param compCoords Coords object - to be compared with this Coords object.
     * @return boolean
     */
    public boolean equals(Coords compCoords){
        if (compCoords.x == this.x && compCoords.y == this.y){
            return true;
        }
        return false;
    }

}
