package pieces;
import mechanics.*;

import java.util.ArrayList;
import java.util.List;
/**
  *Class Rook
  *
  * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
  */
public class Rook extends Piece {

    /**
     * Constructor.
     *
     * @param color PieceColor - what color this piece is intialized to.
     * @param position - Where on the board it's start position shall be.
     */
    public Rook(PieceColor color, Coords position){
        super(color,position);
    }

    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override
    public PieceType getPieceType(){
        return PieceType.ROOK;
    }

    /**
     * Abstract method that calculates this objects possible moves
     * and returns an ArrayList containing Coords objects of where it can move.
     */
    @Override
    public void possibleMoves() {
	this.movesList = new ArrayList<>();
	List<Direction> rookDir = new ArrayList<>();
	rookDir.add(Direction.UP);
	rookDir.add(Direction.DOWN);
	rookDir.add(Direction.LEFT);
	rookDir.add(Direction.RIGHT);
	for(Direction dir : rookDir){
	    for(int i=1; i<8; i++){
		 Piece tempPiece = board.getPiece(position.getX()+i*dir.deltaX, position.getY()+i*dir.deltaY);
		 if(tempPiece.getPieceType() == PieceType.NONE){
		     this.movesList.add(new Coords(tempPiece.getPosition().getX(),tempPiece.getPosition().getY()));
		 }
		 else if(tempPiece.getPieceColor() == this.enemyColor){
		     this.movesList.add(new Coords(tempPiece.getPosition().getX(),tempPiece.getPosition().getY()));
		     break;
		 }
		 else{
		     break;
		 }
	    }
	}
    }
}