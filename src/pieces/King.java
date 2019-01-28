package pieces;
import mechanics.*;

import java.util.ArrayList;

/**
 *Class King
 *
 * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
 */
public class King extends Piece {

    /**
     * Constructor.
     * @param color PieceColor - what color this piece is intialized to.
     * @param position - Where on the board it's start position shall be.
     */
    public King(PieceColor color, Coords position){
        super(color,position);
    }

    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override
    public PieceType getPieceType(){
        return PieceType.KING;
    }

    /**
     * Abstract method that calculates this objects possible moves
     * and returns an ArrayList containing Coords objects of where it can move.
     */
    @Override
    public void possibleMoves() {
        this.movesList = new ArrayList<>();
        for(int i=-1; i<2; i++){
            for(int j=-1; j<2; j++){
                if(!(i==0 && j==0)){
                    //If spot isnt outside and not same piece color, then add to list
                    if(board.getPiece(position.getX()+i,position.getY()+j).getPieceType() != PieceType.OUTSIDE &&
                       (board.getPiece(position.getX()+i,position.getY()+j).getPieceColor() == this.enemyColor
                       || board.getPiece(position.getX()+i,position.getY()+j).getPieceColor() == PieceColor.NOCOLOR)){
                        this.movesList.add(new Coords(position.getX()+i, position.getY()+j));
                    }
                }
            }
        }
    }
}

