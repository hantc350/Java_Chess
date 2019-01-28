package pieces;
import mechanics.*;

import java.util.ArrayList;

/**
  *Class Pawn
  *
  * Functionalities: Almost identical to other piece subobjects, overrides its abstract class Piece's abstract methods
  */
public class Pawn extends Piece{

    /**
     * Constructor.
     * @param color PieceColor - what color this piece is intialized to.
     * @param position - Where on the board it's start position shall be.
     */
    public Pawn(PieceColor color, Coords position){
        super(color,position);
    }

    /**
     * The pawn has the opportunity to move two steps if it is its first time moving therefore
     * this attribute is interesting, this is basically a getter of its hasMoves field
     *
     * @return boolean
     */
    public boolean hasMoved(){
        return this.hasMoved;
    }

    /**
     * Abstract Getter for this piece's PieceType
     *
     * @return PieceType - This object's PieceType
     */
    @Override
    public PieceType getPieceType(){
        return PieceType.PAWN;
    }

    /**
     * Abstract method that calculates this objects possible moves
     * and returns an ArrayList containing Coords objects of where it can move.
     */
    @Override
    public void possibleMoves(){
        this.movesList = new ArrayList<>();
        int step = 0;
        int bigStep;

        if(this.pieceColor == PieceColor.WHITE){
            step = Direction.UP.deltaY;
        }
        else if(this.pieceColor == PieceColor.BLACK){
            step = Direction.DOWN.deltaY;
        }

        bigStep = step*2;

        //Pawns can only kill an enemy piece if they are diagonally 1 step ahead of them so we iterate the numbers -1,0,1 and look at them seperatly
        //when i is -1 or 1 it can kill the piece, if i is 0 it can only move if it is an empty spot
        for (int i = -1; i < 2; i++){
            if(i==0){
                if (this.board.getPiece(position.getX() + i, position.getY() + step).getPieceType() == PieceType.NONE) {
                    this.movesList.add(new Coords(position.getX() +i, position.getY() + step));
                }
            }
            else {
                if (this.board.getPiece(position.getX() + i, position.getY() + step).getPieceColor() == enemyColor) {
                    this.movesList.add(new Coords(position.getX() + i, position.getY() + step));
                }
            }
        }

        //The first move a pawn makes it has the option to jump two spots, we introduced a boolean variable to solve that issue
        if (!this.hasMoved() && this.board.getPiece(position.getX(), position.getY() + bigStep).getPieceType() == PieceType.NONE) {
            if (this.board.getPiece(position.getX(), position.getY() + step).getPieceType() == PieceType.NONE) {
                this.movesList.add(new Coords(position.getX(), position.getY() + bigStep));
            }
        }
    }
}