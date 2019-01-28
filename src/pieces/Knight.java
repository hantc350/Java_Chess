package pieces;
import mechanics.*;

import java.util.ArrayList;
import java.util.List;

/**
 *Class Knight
 *
 * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
 */
public class Knight extends Piece {


    /**
     * Constructor.
     * @param color PieceColor - what color this piece is intialized to.
     * @param position - Where on the board it's start position shall be.
     */
    public Knight(PieceColor color, Coords position){
        super(color,position);
    }

    //Hard coding Knight moves according to below illustration and taking to account how where the Knight is positioned will limit its possible moves.
    //-1-2-
    //8---3
    //--k--
    //7---4
    //-6-5-

    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override
    public PieceType getPieceType(){
        return PieceType.KNIGHT;
    }

    /**
     * Abstract method that calculates this objects possible moves
     * and returns an ArrayList containing Coords objects of where it can move.
     * Hard coding Knight moves according to below illustration and taking to account how where the Knight is positioned will limit its possible moves.
         -1-2-
         8---3
         --k--
         7---4
         -6-5-
     */
    @Override
    public void possibleMoves() {
	this.movesList = new ArrayList<>();
	List<KnightMoves> knightDir = new ArrayList<>();
	knightDir.add(KnightMoves.DOWNRIGHT);
	knightDir.add(KnightMoves.UPRIGHT);
	knightDir.add(KnightMoves.DOWNLEFT);
	knightDir.add(KnightMoves.UPLEFT);
	knightDir.add(KnightMoves.DOWNRIGHT2);
	knightDir.add(KnightMoves.UPRIGHT2);
	knightDir.add(KnightMoves.DOWNLEFT2);
	knightDir.add(KnightMoves.UPLEFT2);


	for(KnightMoves dir : knightDir){ //dont let the knight run on OUTSIDE
	    int xOffSet = dir.deltaKX;
	    int yOffSet = dir.deltaKY;

	    if(xOffSet<0 && yOffSet<0){
		breakpoint:
		for(int i = -1; i >= xOffSet; i--){
		    for(int j = -1; j >= yOffSet; j--){
			if(itrFunc(i, j, xOffSet , yOffSet)){
			    break breakpoint;
			}
		    }
		}
	    }

	    if(xOffSet>0 && yOffSet<0){
		breakpoint:
		for(int i = 1; i <= xOffSet; i++){
		    for(int j = -1; j >= yOffSet; j--){
			if(itrFunc(i, j, xOffSet , yOffSet)){
			    break breakpoint;
			}
		    }
		}
	    }

	    if(xOffSet>0 && yOffSet>0){
		breakpoint:
		for(int i = 1; i <= xOffSet; i++){
		    for(int j = 1; j <= yOffSet; j++){
			if(itrFunc(i, j, xOffSet , yOffSet)){
			    break breakpoint;
			}
		    }
		}
	    }

	    if(xOffSet<0 && yOffSet>0){
		breakpoint:
		for(int i = -1; i >= xOffSet; i--){
		    for(int j = 1; j <= yOffSet; j++){
			if(itrFunc(i, j, xOffSet , yOffSet)){
			    break breakpoint;
			}
		    }
		}
	    }
	}
    }

    private boolean itrFunc(int i,int j, int xOffSet, int yOffSet){
	Piece tempPiece = board.getPiece(this.position.getX()+i, this.position.getY()+j);
	if(tempPiece.getPieceType() == PieceType.OUTSIDE){
	    return true;
	}
	else if(i == xOffSet && j == yOffSet){
	    if(tempPiece.getPieceType() == PieceType.NONE || tempPiece.getPieceColor() == this.enemyColor){
		this.movesList.add(new Coords(tempPiece.getPosition().getX(),tempPiece.getPosition().getY()));
	    }
	}
	return false;
    }
}