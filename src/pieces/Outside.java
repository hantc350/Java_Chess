package pieces;
import mechanics.*;

import java.util.ArrayList;

/**
 *Class Outside
 *
 * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
 * but is empty in all its regards.
 */
public class Outside extends Piece{

    /**
     * Constructor.
     * @param color PieceColor - set to PieceColor.NOCOLOR externally.
     * @param position - Which in this case is using the Coords() constructer without any parameters.
     */
    public Outside(PieceColor color, Coords position){
        super(color,position);
    }


    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override
    public PieceType getPieceType(){
        return PieceType.OUTSIDE;
    }

    /**
     * Sets this movesList to an empty ArrayList.
     */
    @Override
    public void possibleMoves(){
        this.movesList = new ArrayList<>();
    }

}
