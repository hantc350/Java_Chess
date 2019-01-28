package mechanics;
import pieces.*;

/**
 * Class Board
 *
 * Functionalities: Instantiate a board and manipulates it as the game goes by
 */

public class Board {
    private int width;
    private int height;

    /**
     * Board object
     */
    public Piece[][] board;

    /**
     * Getter for the boards width
     * @return width
     */
    public int getWidth() {  //get width of the board
        return width;
    }

    /**
     * Getter for the boards height
     * @return height
     */
    public int getHeight() { //get height of the board
        return height;
    }

    /**
     * Constructor of Board.
     *
     * @param height int
     * @param width int
     */
    public Board(int height, int width) {  //the board, initiate a board
        this.width = width;
        this.height = height;
        board = new Piece[width][height];

        for ( int row = 0; row<height; row++){//makes all squares as OUTSIDE
            for (int column = 0; column<width; column++){
                board[column][row]= new Outside(PieceColor.NOCOLOR, new Coords(column,row));
            }
        }
        for ( int row = 1; row<height-1; row++){//makes inner squares as EMPTY
        for (int column = 1; column<width-1; column++){
                board[column][row]= new None(PieceColor.NOCOLOR, new Coords(column, row));
            }
        }
        board[1][1] = new Rook(PieceColor.BLACK, new Coords(1,1));
        board[2][1] = new Knight(PieceColor.BLACK, new Coords(2,1));
        board[3][1] = new Bishop(PieceColor.BLACK, new Coords(3,1));
        board[4][1] = new King(PieceColor.BLACK, new Coords(4,1));
        board[5][1] = new Queen(PieceColor.BLACK, new Coords(5,1));
        board[6][1] = new Bishop(PieceColor.BLACK, new Coords(6,1));
        board[7][1] = new Knight(PieceColor.BLACK, new Coords(7,1));
        board[8][1] = new Rook(PieceColor.BLACK, new Coords(8,1));
        for (int col = 1;col<width-1;col++){
            board[col][2] = new Pawn(PieceColor.BLACK, new Coords(col,2));
        }

        board[1][8] = new Rook(PieceColor.WHITE, new Coords(1,8));
        board[2][8] = new Knight(PieceColor.WHITE, new Coords(2,8));
        board[3][8] = new Bishop(PieceColor.WHITE, new Coords(3,8));
        board[4][8] = new King(PieceColor.WHITE, new Coords(4,8));
        board[5][8] = new Queen(PieceColor.WHITE, new Coords(5,8));
        board[6][8] = new Bishop(PieceColor.WHITE, new Coords(6,8));
        board[7][8] = new Knight(PieceColor.WHITE, new Coords(7,8));
        board[8][8] = new Rook(PieceColor.WHITE, new Coords(8,8));
        for (int col = 1;col<width-1;col++){
            board[col][7] = new Pawn(PieceColor.WHITE, new Coords(col,7));
        }
    }

    /**
     * Getter for a piece at position (x,y)
     *
     * @param x integer x - what x position to get piece form
     * @param y integer y - what y position to get piece form
     * @return Piece ojbect
     */
    public Piece getPiece(int x, int y){
        return board[x][y];
    }

    /**
     * Setter for a piece at position (x,y)
     *
     * @param x integer x - what x position to set piece
     * @param y integer y - what y position to set piece
     * @param piece The piece object to be set on said position (x,y)
     */
    public void setPiece(int x, int y, Piece piece){
        board[x][y] = piece;
    }

    /**
     * Remove piece object at position (x,y) and replace with a new Empty object (PieceType.NONE)
     *
     * @param x integer x - what x position the piece to remove is located
     * @param y integer y - what y position the piece to remove is located
     */
    public void removePiece(int x, int y){
        board[x][y] = new None(PieceColor.NOCOLOR, new Coords(x, y));
    }

}

