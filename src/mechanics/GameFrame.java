package mechanics;

import gui.*;
import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Class GameFrame
 *
 * Functionalities: Majority of game logic and behaviours and handeling inputs from player along with
 * creating and maintaing a java frame.
 */
public class GameFrame extends JFrame implements MouseListener {
    //Logic fields
    private Board board;
    private int clickCounter = 0;
    private Piece savedSelectedPiece = new None(PieceColor.NOCOLOR, new Coords());
    private Piece currentSelectedPiece = new None(PieceColor.NOCOLOR, new Coords());
    private List<Coords> list = new ArrayList<>();
    private List<Coords> checkList = new ArrayList<>(); // The selected piece possible moves
    private Piece killer = new None(PieceColor.NOCOLOR, new Coords());
    private java.util.List<Coords> killerPath = new ArrayList<>();
    private boolean checkState = false;
    private int turn = 0;

    //Jframe fields
    private static JLabel playerLabel = null;
    private static JLabel checkLabel = null;
    private static JLabel checkmateLabel = null;
    /**
     * The time label
     */
    public static JLabel timeLabel = null;

    /**
     * The time counter
     */
    public static int timeCounter = 0;

    /**
     * Constructor.
     *
     * Defines Frame layour
     *
     * @param board Board object which is created in main class.
     */
    public GameFrame(Board board){
        super("Chess");
        this.board = board;
        setFocusable(true);
        final GameComponent gameComponent = new GameComponent(board);
        this.setLayout(new BorderLayout());
        this.add(gameComponent,BorderLayout.CENTER);
        createLabelPanel();
        createMenu();
        this.pack();
        this.setVisible(true);
        addMouseListener(this);
    }

    /**
     * Constructor of label panel.
     *
     *  Creating a label panel and intantiating labels
     */
    private void createLabelPanel(){
        final int panelX = 195;
        final int panelY = 200;
        JPanel labelPanel = new JPanel(new BorderLayout());
        playerLabel = new JLabel("Player: White");
        checkLabel = new JLabel("Not check");
        checkmateLabel = new JLabel("Not checkmate");
        timeLabel = new JLabel("Time Elapsed: " + timeCounter);

        //Set borders to labels
        playerLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        checkLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        timeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        checkmateLabel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));

        labelPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        labelPanel.setOpaque(true);
        labelPanel.setBackground(Color.WHITE);
        this.add(labelPanel, BorderLayout.EAST);

        BoxLayout boxLayout = new BoxLayout(labelPanel, BoxLayout.Y_AXIS); // top to bottom
        labelPanel.setLayout(boxLayout);
        labelPanel.add(playerLabel);
        labelPanel.add(timeLabel);
        labelPanel.add(checkLabel);
        labelPanel.add(checkmateLabel);

        labelPanel.setPreferredSize(new Dimension(panelX,panelY));
    }

    //Simple exit option in file tab

    /**
     * Constructor of menu
     *
     *A simple menu item that consists of a exit option.
     */
    private void createMenu(){
        final JMenu file = new JMenu("File");
        JMenuItem exit = file.add(new JMenuItem("exit"));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "exit", "choose", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        file.add(exit);
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        this.setJMenuBar(menuBar);
    }

    /**
     * Incrementing turn field value.
     */
    private void nextTurn(){
	this.turn += 1;
    }

    /**
     * Tick function
     */
    public static void tick(){ //function of every tick (timer)
	timeCounter++;
	timeLabel.setText("Time Elapsed: "+GameFrame.timeCounter);
    }


    /**
     * Updating labels as the game goes by. Switching labels
     * depending whos turn it is etc.
     */
    public void updateLabel() {
        if(this.turn % 2 == 0){
            playerLabel.setText("Player: Black");
        }
        else{
            playerLabel.setText("Player: White");
        }
        if(checkState) {
	    checkLabel.setText("Check");
	}
        if(!checkState){
            checkLabel.setText("Not check");
        }
        boardChanged();
    }

    /**
     * Java mouse handler which handles all mouse click events
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e){
        final int boardX = 600;
        final int boardY = 660;
        if (e.getX() < boardX && e.getY() < boardY){
            int x = e.getX();
            int y = e.getY();
            final int squareSize = 60;
            GameComponent.clickedX = (x / squareSize);
            GameComponent.clickedY = (y / squareSize) - 1;

            this.currentSelectedPiece = this.board.getPiece(GameComponent.clickedX, GameComponent.clickedY);

            //Clear possible moves
            this.list = new ArrayList<>();

            //Decide correct players turn
            if(this.turn % 2 == 0){
                clickFunc(PieceColor.WHITE);
            }
            else{clickFunc(PieceColor.BLACK);}
        }
        boardChanged();
    }

    /**
     * Function that moves a piece on the board object.
     * @param from Piece object
     * @param to Piece object
     */
    private void movePiece(Piece from, Piece to){
        this.board.setPiece(to.getPosition().getX(), to.getPosition().getY(), from);
        this.board.removePiece(from.getPosition().getX(), from.getPosition().getY());
        from.move(to.getPosition());
    }

    /**
     * Figures out what the player is trying to do with his/her clicks. If they want to select a piece,
     * if the want to select a piece, if they want to deselect a piece, if they want to kill a piece, check the
     * opponent or win the game. All this is depenendant of whos turn it is to play, what state the clickCounter is in
     * if they are allowed to move there etc.
     *
     * @param color PieceColor which decides what color is allowed to be pressed etc.
     */
    private void clickFunc(PieceColor color){
        //Click on piece (not NONE nor OUTSIDE)
        if (currentSelectedPiece.getPieceType() != PieceType.NONE && currentSelectedPiece.getPieceType() != PieceType.OUTSIDE){

            //First click and on a piece -> Selecting piece
            if(clickCounter == 0 && currentSelectedPiece.getPieceColor() == color){
                //Select piece and calculate new possiblemoves
                //this.currentSelectedPiece.selectPiece(this.board);
                selectPiece();

            }
            //(A piece is previously selected and now we are clicking on a piece again)
            else if(clickCounter==1){
                //Iterate checklist
                for(int i=0; i<checkList.size(); i++){
                    //If next click is in checklist, then it's an OK move
                    if(checkList.get(i).equals(currentSelectedPiece.getPosition())){
                        //Move
                        //Make sure that move doesnt check your own king
                        if(checkSelfCheck(savedSelectedPiece, currentSelectedPiece)){

                            /*this.board.setPiece(currentSelectedPiece.getPosition().getX(), currentSelectedPiece.getPosition().getY(), savedSelectedPiece);
                            this.board.removePiece(savedSelectedPiece.getPosition().getX(), savedSelectedPiece.getPosition().getY());
                            savedSelectedPiece.move(currentSelectedPiece.getPosition());*/

                            movePiece(savedSelectedPiece, currentSelectedPiece);

                            //See if pawn moves across the board to switch it for a new queen
                            if(savedSelectedPiece.getPieceType()==PieceType.PAWN){
                                if(savedSelectedPiece.getPieceColor() == PieceColor.WHITE &&
                                   savedSelectedPiece.getPosition().getY() == 1 ||
                                   savedSelectedPiece.getPieceColor() == PieceColor.BLACK &&
                                   savedSelectedPiece.getPosition().getY() == 8){
                                    pawnToQueen(savedSelectedPiece);
                                }
                            }

                            //See if you are checking enemy king
                            if(checkEnemyCheck(savedSelectedPiece)){
                                GameComponent.killer = this.killer;

                                //See if you are check mating enemy
                                if(checkCheckMate(savedSelectedPiece)){
                                }
                                this.killer = new None(PieceColor.NOCOLOR, new Coords());
                            }

                            else if(!checkEnemyCheck(savedSelectedPiece)){
                                this.killer = new None(PieceColor.NOCOLOR, new Coords());
                                GameComponent.killer = this.killer;
                                this.checkState = false;
                            }

                            updateLabel();
                            GameComponent.selectedPiece.selectPiece(this.board);
                            nextTurn();
                            clearPiece();
                            break;
                        }

                        //Don't move if you are trying to move to a spot whre your own king would end up in check, reset
                        else if(!checkSelfCheck(savedSelectedPiece, currentSelectedPiece)){
                            clearPiece();
                            break;
                        }
                    }

                    //finished iterating through checklist
                    else if(i==checkList.size()-1){
                        clearPiece();
                    }
                }
                if(currentSelectedPiece.getPieceColor() == color){
                    selectPiece();
                }
            }

            //Reset any other scenario (when pressing on a oposite colored piece when clickcounter is 0 piece)
            else{
                clearPiece();
            }
        }

        //click on empty boardspace
        else if (currentSelectedPiece.getPieceType() == PieceType.NONE){
            //clicking on an empty spot will not do anything
            if(clickCounter == 0){
                clearPiece();
            }
            //You have a piece selected and you want to move it to an empty spot
            else if (currentSelectedPiece.getPieceType() != PieceType.OUTSIDE && clickCounter == 1){
                //See if the empty spot is in the checklist
                for(int i=0; i<checkList.size(); i++){
                    //If spot is in checklist, try to move in spot
                    if(checkList.get(i).equals(currentSelectedPiece.getPosition())){
                        //If you can move to the spot without putting your own king at check, move to empty spot
                        if(checkSelfCheck(savedSelectedPiece, currentSelectedPiece)){
                            movePiece(savedSelectedPiece, currentSelectedPiece);
                            //See if pawn moves across the board to switch it for a new queen
                            if(savedSelectedPiece.getPieceType()==PieceType.PAWN){
                                if(savedSelectedPiece.getPieceColor() == PieceColor.WHITE &&
                                   savedSelectedPiece.getPosition().getY() == 1 ||
                                   savedSelectedPiece.getPieceColor() == PieceColor.BLACK &&
                                   savedSelectedPiece.getPosition().getY() == 8){
                                    pawnToQueen(savedSelectedPiece);
                                }
                            }
                            //See if you are checking enemy king
                            if(checkEnemyCheck(savedSelectedPiece)){
                                GameComponent.killer = this.killer;
                                //See if you are check mating the enemy
                                if(checkCheckMate(savedSelectedPiece)){
                                }
                                this.killer = new None(PieceColor.NOCOLOR, new Coords());
                            }
                            //reset check logic if not check
                            else if(!checkEnemyCheck(savedSelectedPiece)){
                                this.killer = new None(PieceColor.NOCOLOR, new Coords());
                                GameComponent.killer = this.killer;
                                this.checkState = false;
                            }
                            updateLabel();
                            GameComponent.selectedPiece.selectPiece(this.board);
                            nextTurn();
                            clearPiece();
                            break;
                        }
                        //Don't move if you are putting your own king at check if you make that move
                        else if(!checkSelfCheck(savedSelectedPiece, currentSelectedPiece)){
                            clearPiece();
                            break;
                        }
                    }
                    //Finished iterating through checklist
                    else if(i==checkList.size()-1){
                        clearPiece();
                    }
                }

            }
        }
    }

    /**
     *  This function is there to Remove any of the possible moves the king piece could originally move to as per its
     *  possiblemove algoritm but can't because it would put itself in check. This solved our orignal problem of an infinte loop
     *  where we had to check all other pieces possible moves, find a king check its possible moves which ment looking at all
     *  possible moves again etc.
     *
     * @param piece Piece object - always King object
     * @param kingList List collection which is the possible moves the king piece can move
     * @return List<Coords> A new List containing the same or less Coords
     */
    private List<Coords> fixKingList(Piece piece, List<Coords> kingList){
        List<Coords> removedCoords = new ArrayList<>();
        //Iterate through board
        for(int i=1; i<9; i++) {
            for (int j = 1; j < 9; j++) {
                //Look at oposite colored pieces
                Piece tempPiece = this.board.getPiece(j, i);
                if (tempPiece.getPieceColor() == piece.getEnemyColor() && !kingList.isEmpty()){
                    //Case of any piece but pawn
                    if(tempPiece.getPieceType() != PieceType.PAWN){
                        List<Coords> tempList = tempPiece.getList(board);
                        //Iterate through the tempList and eventually returning kingList
                        for(int kingItr=0; kingItr<kingList.size(); kingItr++){
                            for(int l=0; l<tempList.size(); l++){
                                //Remove the spot from the kings existing checklist IF the templist has a spot on any of the ones in checklist AND the removedCoords not already has it
                                if(!kingList.isEmpty() && !removedCoords.contains(kingList.get(kingItr))){
                                    if(tempList.get(l).equals(kingList.get(kingItr)) || kingList.contains(tempPiece.getPosition()) && checkSelfCheck(piece, tempPiece)){
                                       removedCoords.add(kingList.get(kingItr));
                                       kingList.remove(kingItr);
                                       //To not get iteration error
                                       if(kingItr!=0){
                                           kingItr -= 1;
                                       }
                                       else{
                                           kingItr=0;
                                       }
                                    }
                                }
                            }
                        }
                    }
                    //Check with enemy pawns
                    else{
                        //Iterate through the update kingslist
                        for(int kingItr=0; kingItr<kingList.size(); kingItr++){
                            //See if that pawns potential moves collides with any of the spots in kingslist
                            int direction = 0;
                            if(piece.getPieceColor()==PieceColor.WHITE){
                                direction = 1;
                            }
                            else if(piece.getPieceColor()==PieceColor.BLACK){
                                direction = -1;
                            }
                            for (int p = -1; p < 2; p++){
                                int xPos = this.board.getPiece(j, i).getPosition().getX()+p;
                                int yPos = this.board.getPiece(j, i).getPosition().getY()+direction;
                                Piece tempPawnPiece = this.board.getPiece(xPos, yPos);
                                if(p!=0 && tempPawnPiece.getPieceType() != PieceType.OUTSIDE){
                                    if(!kingList.isEmpty() && !removedCoords.contains(kingList.get(kingItr))){
                                        if(kingList.get(kingItr).getX()== xPos &&
                                            kingList.get(kingItr).getY()== yPos || kingList.contains(tempPawnPiece.getPosition()) && checkSelfCheck(piece, tempPawnPiece)){
                                            removedCoords.add(kingList.get(kingItr));
                                            kingList.remove(kingItr);
                                            //To not get iteration error
                                            if(kingItr!=0){
                                                kingItr -= 1;
                                            }
                                            else{
                                                kingItr=0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return kingList;
    }


    /**
     * A submethod to the above clickFunc algoritm which basically selects a piece and does all whats intended
     * post selecting
     */
    private void selectPiece(){
        this.list = this.currentSelectedPiece.getList(board);
        //Remove non eligeble moves from Kings possible moves list
        if(currentSelectedPiece.getPieceType()==PieceType.KING){
            this.list = fixKingList(currentSelectedPiece, this.list);
        }
        this.clickCounter++;
        this.savedSelectedPiece = currentSelectedPiece;
        savedSelectedPiece.selectPiece(this.board);
        GameComponent.selectedPiece = savedSelectedPiece;
        GameComponent.list = this.list;
        this.checkList = this.list;
        //If you are trying to select a piece that doesnt have anything to do,start over and deselect
        if (checkList.isEmpty()){
            clearPiece();
        }
    }


    /**
     * A method that is called everytime a piece is moved from the above clickFunc() algoritm
     * and checks if that move will result in enemy king being set in check
     *
     * @param piece Piece object that is moving
     * @return boolean - true if enemy king is in check, false if enemy king isn't in check.
     */
    private boolean checkEnemyCheck(Piece piece){
        boolean returnValue = false;
        //Iterate through board
        for(int i=1; i<9; i++) {
            for(int j=1; j<9; j++) {
                Piece tempPiece = this.board.getPiece(j, i);
                //Find all same colored pieces as the one that is moving
                if(tempPiece.getPieceColor() == piece.getPieceColor()){
                    //tempPiece.possibleMoves();
                    List<Coords> tempList = tempPiece.getList(board);
                    //Iterate through every same colored piece
                    for(int k=0; k<tempList.size(); k++){
                        int tempX = tempList.get(k).getX();
                        int tempY = tempList.get(k).getY();

                        //If they have a King, that means they this move will result in a check. (They will never have a king of their own color)
                        if(this.board.getPiece(tempX, tempY).getPieceType() == PieceType.KING){
                            this.checkState = true;
                            returnValue = true;
                            this.killer = tempPiece;
                            //If current iterated piece is any piece but knight or pawn we can start finding the killer path
                            if(tempPiece.getPieceType() != PieceType.KNIGHT && tempPiece.getPieceType() != PieceType.PAWN){
                                int diffX, diffY; //Difference in X,Y axis
                                diffX = j-tempX;
                                diffY = i-tempY;

                                //Bishop or Queen, make sure its diagnoally positioned with King
                                if(tempPiece.getPieceType() == PieceType.BISHOP || (tempPiece.getPieceType() == PieceType.QUEEN && (j!=tempX || i!=tempY))){
                                    //Depending of where the bishop or queen is positioned relative to the king, iteration is done differently
                                    if(diffX<0&&diffY<0){
                                        for(int l=-1; l>diffX; l--) {
                                            for(int m=-1; m>diffY; m--) {
                                                //As for the killer path, it will always be the l and m where they are the same
                                                if(abs(l) == abs(m)){
                                                    this.killerPath.add(new Coords(tempX+l, tempY+m));
                                                }
                                            }
                                        }
                                    }
                                    else if(diffX>0&&diffY<0){
                                        for(int l=1; l<diffX; l++) {
                                            for(int m=-1; m>diffY; m--) {
                                                if(abs(l) == abs(m)){
                                                    this.killerPath.add(new Coords(tempX+l, tempY+m));
                                                }
                                            }
                                        }
                                    }
                                    else if(diffX<0&&diffY>0){
                                        for(int l=-1; l>diffX; l--) {
                                            for(int m=1; m<diffY; m++) {
                                                if(abs(l) == abs(m)){
                                                    this.killerPath.add(new Coords(tempX+l, tempY+m));
                                                }
                                            }
                                        }
                                    }
                                    else if(diffX>0&&diffY>0){
                                        for(int l=1; l<diffX; l++) {
                                            for(int m=1; m<diffY; m++) {
                                                if(abs(l) == abs(m)){
                                                    this.killerPath.add(new Coords(tempX+l, tempY+m));
                                                }
                                            }
                                        }
                                    }
                                }

                                //Rook or Queen, make sure they are alligned with the king
                                else if(tempPiece.getPieceType() == PieceType.ROOK || (tempPiece.getPieceType() == PieceType.QUEEN && (j==tempX || i==tempY))){
                                    //Here the killerpath logic is accordingly
                                    if(diffX==0&&diffY<0){
                                        for(int m=-1; m>diffY; m--) {
                                            this.killerPath.add(new Coords(tempX, tempY+m));
                                        }

                                    }
                                    else if(diffX==0&&diffY>0){
                                        for(int l=1; l<diffX; l++) {
                                            for(int m=1; m>diffY; m++) {
                                                this.killerPath.add(new Coords(tempX, tempY+m));
                                            }
                                        }
                                    }
                                    else if(diffX<0&&diffY==0){
                                        for(int l=-1; l>diffX; l--) {
                                            this.killerPath.add(new Coords(tempX+l, tempY));
                                        }
                                    }
                                    else if(diffX>0&&diffY==0){
                                        for(int l=1; l<diffX; l++) {
                                            this.killerPath.add(new Coords(tempX+l, tempY));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * A method that also is called every move you make from clickFunc algoritm.
     * It checks to see if a move is viable which it wouldn't be if you king is set to check
     * if you try to move it.
     *
     * Comments in function will explain how we did it.
     *
     * @param piece Piece object that is moving
     * @param movePiece Piece object the first parameter piece is moving to
     * @return boolean - true if your king is set to check if you try that move, false if you are allowed to move there
     */
    private boolean checkSelfCheck(Piece piece, Piece movePiece){
        //Here we want to temporarily change the board with the attempted move and see if the resulting
        //board wouuld be a check on your own king. So we temporarily save the piece we are overriding to put back later
        //Piece is the piece trying to move, movePiece is the piece on the spot piece is trying to move to (sometimes it's empty)
        Piece savePiece = this.board.getPiece(movePiece.getPosition().getX(), movePiece.getPosition().getY());
        Coords saveCoordsForPiece = piece.getPosition();
        this.board.setPiece(piece.getPosition().getX(), piece.getPosition().getY(), new None(PieceColor.NOCOLOR, new Coords(piece.getPosition().getX(), piece.getPosition().getY())));
        this.board.setPiece(movePiece.getPosition().getX(), movePiece.getPosition().getY(), piece);

        //Iterate through board
        for(int i=1; i<9; i++){
            for(int j=1; j<9; j++){
                //check oposite colors relatively to piece
                Piece tempPiece = this.board.getPiece(j,i);
                if(tempPiece.getPieceColor() == piece.getEnemyColor()){
                    List<Coords> tempList = tempPiece.getList(board);
                    for(int k=0; k<tempList.size(); k++){
                        //Does any of the oposite color pieces have your own king in their movelist after this potential move was made? If so, that move is not possible to make
                        if (this.board.getPiece(tempList.get(k).getX(),tempList.get(k).getY()).getPieceType() == PieceType.KING  && this.board.getPiece(tempList.get(k).getX(),tempList.get(k).getY()).getPieceColor() == piece.getPieceColor()){
                            this.board.setPiece(piece.getPosition().getX(), piece.getPosition().getY(), piece);
                            this.board.setPiece(movePiece.getPosition().getX(), movePiece.getPosition().getY(), savePiece);
                            return false;
                        }
                    }
                }
            }
        }
        //Return to old boardstate
        this.board.setPiece(saveCoordsForPiece.getX(),saveCoordsForPiece.getY(), piece);
        this.board.setPiece(movePiece.getPosition().getX(), movePiece.getPosition().getY(), savePiece);
        return true;
    }

    /**
     * A method which is called everytime checkEnemyCheck() is called
     * to further see if the enemy king also is set to checkmat which means
     * the player that moved won the game.
     *
     * @param piece Piece object that is moving
     * @return boolean - true if the enemy king is set to checkmate.
     */
    private boolean checkCheckMate(Piece piece){
        boolean kingListSizeIsNull = true;
        boolean checkMate = true;
        //Iterate through board
        for(int i=1; i<9; i++) {
            for (int j = 1; j < 9; j++) {
                Piece tempPiece = this.board.getPiece(j, i);
                //Look at oposite colored pieces relatively to piece
                if (tempPiece.getPieceColor() == piece.getEnemyColor()) {
                    //tempPiece.possibleMoves();
                    List<Coords> tempList = tempPiece.getList(board);
                    for(int k=0; k<tempList.size(); k++){
                        //If any piece that isn't knight or pawn, check if they have a spot in their possible move to counter the check against their own king
                        if(tempPiece.getPieceType() != PieceType.KNIGHT && tempPiece.getPieceType() != PieceType.PAWN && tempPiece.getPieceType() != PieceType.KING){
                            for(int l=0; l<killerPath.size(); l++){
                                if(tempList.get(k).equals(killerPath.get(l))){
                                    //Make sure that move doesn't put your own king at check
                                    if(checkSelfCheck(tempPiece,this.board.getPiece(tempList.get(k).getX(),tempList.get(k).getY()))){
                                        checkMate = false;
                                    }
                                }
                            }
                        }
                        //If piece is knight or pawn, see if it can interupt the check mate
                        else if(tempPiece.getPieceType() == PieceType.KNIGHT || tempPiece.getPieceType() == PieceType.PAWN){
                            if(tempList.get(k).equals(this.killer.getPosition())){
                                checkMate = false;
                            }
                        }
                        //Eventually the enemy king piece will be found in the iteration, in this iteration we want to make sure the king has no where to move
                        else if(tempPiece.getPieceType() == PieceType.KING){
                            List<Coords> kingList = fixKingList(tempPiece, tempPiece.getList(board));
                            //Since the size isn't changed we have to be sure that for all index in kingslist, they have to be (-1,-1)
                            for(int c=0; c<kingList.size(); c++){
                                if(kingList.get(c).getX()!= -1){
                                    kingListSizeIsNull = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        //Return true if checkMate==false AND kinglistsizeisnull == 0
        if(kingListSizeIsNull && checkMate){
            return true;
        }
        return false;
    }

    /**
     * A generic method to clear the state of what piece is selected
     */
    private void clearPiece(){
        //this.savedSelectedPiece = new None(new Coords(currentSelectedPiece.getPosition().getX(), currentSelectedPiece.getPosition().getY()));
        //GameComponent.selectedPiece = new None(new Coords(currentSelectedPiece.getPosition().getX(), currentSelectedPiece.getPosition().getY()));

        this.savedSelectedPiece = new None(PieceColor.NOCOLOR, new Coords());
        GameComponent.selectedPiece = new None(PieceColor.NOCOLOR, new Coords());
        this.clickCounter = 0;
        this.list = new ArrayList<>();
        GameComponent.list = this.list;
        this.checkList = new ArrayList<>();
    }

    /**
     * The parameter piece is the pawn which is positioned on the oposite side of the board
     * and is being replaced by a new Queen object.
     *
     * @param piece Piece object
     */
    private void pawnToQueen(Piece piece){
        this.board.removePiece(piece.getPosition().getX(), piece.getPosition().getY());
        this.board.setPiece(piece.getPosition().getX(), piece.getPosition().getY(), new Queen(piece.getPieceColor(), piece.getPosition()));
    }


    /**
     * Mouseevent handlers.
     * @param e MouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent e){
    }
    @Override
    public void mouseExited(MouseEvent e){
    }
    @Override
    public void mousePressed(MouseEvent e){
    }
    @Override
    public void mouseReleased(MouseEvent e){
    }

    /**
     * Any time an action is made to change the state of the board,
     * this method is called so that it can be repainted.
     */

    public void boardChanged() {
        repaint();
    }

}
