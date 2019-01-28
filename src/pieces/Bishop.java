package pieces;
import mechanics.*;

import java.util.ArrayList;
import java.util.List;

/**
  *Class Bishop
  *
  * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
  */
public class Bishop extends Piece{

    /**
     * Constructor.
     * @param color PieceColor - what color this piece is intialized to.
     * @param position - Where on the board it's start position shall be.
     */
    public Bishop(PieceColor color, Coords position){
        super(color,position);
    }

    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    /**
     * Abstract method that calculates this objects possible moves
     * and returns an ArrayList containing Coords objects of where it can move.
     */
    @Override public void possibleMoves() {
        this.movesList = new ArrayList<>();
        List<Direction> bishopDir = new ArrayList<>();
        bishopDir.add(Direction.UPRIGHT);
        bishopDir.add(Direction.DOWNRIGHT);
        bishopDir.add(Direction.UPLEFT);
        bishopDir.add(Direction.DOWNLEFT);
       	for(Direction dir : bishopDir){
            for(int i=1; i<8; i++){
                Piece tempPiece = board.getPiece(position.getX()+i*dir.deltaX, position.getY()+i*dir.deltaY);
                if(tempPiece.getPieceType() == PieceType.OUTSIDE){
                    break;
                }
                else if(tempPiece.getPieceColor() == PieceColor.NOCOLOR){
                    this.movesList.add(new Coords(position.getX()+i*dir.deltaX,position.getY()+i*dir.deltaY));
                }
                else if (tempPiece.getPieceColor() == this.enemyColor){
                    this.movesList.add(new Coords(position.getX()+i*dir.deltaX,position.getY()+i*dir.deltaY));
                    break;
                }
                else {
                    break;
                }
            }
        }
    }
}

