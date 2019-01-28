package pieces;
import mechanics.*;

import java.util.ArrayList;
import java.util.List;
/**
 *Class Piece
 *
 * Functionalities: An abstract class to specify the shared properties of all the different subpieces.
 */
public abstract class Piece {
    /**
     * board object
     */
    public Board board;

    /**
     * position in Coorde object
     */
    public Coords position = new Coords();

    /**
     * PieceColor
     */
    public PieceColor pieceColor;

    /**
     * Boolean value of if it has moved or not
     */
    public boolean hasMoved = false;

    /**
     * Boolean value of if it is selected or not
     */
    public boolean selected = false;

    /**
     * PieceColor of enemy PieceColor
     */
    public PieceColor enemyColor = PieceColor.NOCOLOR;

    /**
     * The list containing this piece objects possible moves
     */
    public List<Coords> movesList = new ArrayList<>();

    /**
     * Super constructor.
     *
     * Every constructer in any subclass is refrensed back up to this main constructor
     *
     * @param pieceColor PieceColor - what color the instantiated piece object is.
     * @param position Coords - Where the intantiated piece is set.
     */
    protected Piece(PieceColor pieceColor, Coords position){
        this.pieceColor = pieceColor;
        this.position = position;

	if(pieceColor == PieceColor.WHITE){
	 enemyColor = PieceColor.BLACK;
	}
	else if(pieceColor == PieceColor.BLACK){
	 enemyColor = PieceColor.WHITE;
	}
    }

    /**
     * Move method
     *
     * @param coords where this piece object is being moved to
     */
    public void move(Coords coords) {
        this.hasMoved = true;
        this.position.setX(coords.getX());
        this.position.setY(coords.getY());
    }

    /***
     * Getter for this piece object's position
     *
     * @return Coords
     */
    public Coords getPosition() {
        return this.position;
    }

    /**
     * Getter for this piece object's PieceColor
     *
     * @return PieceColor
     */
    public PieceColor getPieceColor(){
        return pieceColor;
    }

    /**
     * Abstract method for each subclass to define its own PieceType
     *
     * @return PieceType
     */
    public abstract PieceType getPieceType();

    /**
     * Abstract class to calculate each individual piece's moveList depending what PieceType it is
     * and where it is located.
     */
    public abstract void possibleMoves();

    /**
     * Only one piece can be selected at a time.
     * This boolean property keeps track of that.
     * @return boolean
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * Selects this piece
     *
     * @param board Board
     */
    public void selectPiece(Board board){
        this.board = board;
        this.selected = !selected;
    }

    /**
     * In a lot of comparisons we want to know a pieces relative enemy PieceColor
     * This is a getter for that
     * @return PieceColor
     */
    public PieceColor getEnemyColor(){
        return this.enemyColor;
    }

    /**
     * The method that is called externally to look at this piece object's possible moves
     *
     * @param board Board
     * @return List collection containing Coords objects.
     */
    public List<Coords> getList(Board board){
        this.board = board;
        possibleMoves();
        return this.movesList;
    }

}

