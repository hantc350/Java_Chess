package pieces;
import mechanics.*;

import java.util.ArrayList;
import java.util.List;
/**
 *Class Queen
 */
public class Queen extends Piece {

    //Constructor
    public Queen(PieceColor color, Coords position){
        super(color,position);
    }

    //Overriding the methods declared in the super abstract class

    @Override
    public PieceType getPieceType(){
        return PieceType.QUEEN;
    }

    @Override
        public void possibleMoves() {
	this.movesList = new ArrayList<>();
	List<Direction> queenDir = new ArrayList<>();
	queenDir.add(Direction.UP);
	queenDir.add(Direction.DOWN);
	queenDir.add(Direction.LEFT);
	queenDir.add(Direction.RIGHT);
	queenDir.add(Direction.UPRIGHT);
	queenDir.add(Direction.DOWNRIGHT);
	queenDir.add(Direction.UPLEFT);
	queenDir.add(Direction.DOWNLEFT);
	for (Direction dir : queenDir) {
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