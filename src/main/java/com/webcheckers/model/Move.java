package com.webcheckers.model;

import static java.lang.Math.abs;

public class Move {

    //
    // Attributes
    //

    // Starting position.
    private Position startPos;

    // Ending position.
    private Position endPos;

    // Board to be maniupulating.
    private BoardView boardView;

    // Piece to be moved.
    private Piece piece;



    //
    // Constructor
    //

    /**
     * Mimics the JavaScript Movement.
     * @param startPos Position of the starting point.
     * @param endPos Position of the ending point.
     */
    public Move(Position startPos, Position endPos){
        this.startPos = startPos;
        this.endPos = endPos;
    }

    //
    // Public Methods
    //

    /**
     * Sets the current state of the Board.
     * @param boardView Current board.
     */
    public void setBoardView(BoardView boardView){
        this.boardView = boardView;
    }

    /**
     * Gets the last Position of the Move.
     * @return Position containing the end of Move.
     */
    public Position getEndPos() {
        return endPos;
    }

    /**
     * Gets the starting Position of the Move
     * @return Position containing the end of the Move.
     */
    public Position getStartPos() {
        return startPos;
    }

    /**
     *  Creates a replica of the current Board to be manipulated.
     * @return BoardView with the current status of Pieces.
     */
    public BoardView getBoardView() {
        return new BoardView(this.boardView);
    }

    /**
     * Tests to see if the movement was valid.
     * @return Boolean dictating whether the move is valid.
     */
    public boolean isValid(){
        switch (this.boardView.getRow(this.startPos.getRow()).getSpace(this.endPos.getCol()).getPiece().getColor()){
            case RED:
                if((this.endPos.getRow() - this.startPos.getRow() == 1)|| (this.endPos.getRow() - this.startPos.getRow() == 2)){
                   return this.boardView.getRow(this.endPos.getRow()).getSpace(this.endPos.getCol()).isValid();
                }
            case WHITE:
                if((this.endPos.getRow() - this.startPos.getRow() == -1) || (this.endPos.getRow()- this.startPos.getRow() == -2)){
                    return this.boardView.getRow(this.endPos.getRow()).getSpace(this.endPos.getCol()).isValid();
                }
        }
        if(this.boardView.getRow(this.startPos.getRow()).getSpace(this.endPos.getCol()).getPiece().getType() == Piece.Type.KING){
            if((abs(this.endPos.getRow() - this.startPos.getRow()) == 1) || abs(this.endPos.getRow() - this.startPos.getRow()) == 2){
                return this.boardView.getRow(this.endPos.getRow()).getSpace(this.endPos.getCol()).isValid();
            }
        }
        return false;
    }

    /**
     * Moves the Piece on the BoardView and updates the model BoardView.
     * @return BoardView containing updated Pieces and their new positions.
     */
    public BoardView movePiece(){

        // Copy the Board.
        BoardView tempBoard = getBoardView();

        // Get Piece object at the start position.
        this.piece = tempBoard.getRow(this.startPos.getRow()).getSpace(this.startPos.getCol()).getPiece();

        // Remove Piece at starting position.
        tempBoard.getRow(this.startPos.getRow()).getSpace(this.startPos.getCol()).removePiece();

        // Place Piece at ending position.
        tempBoard.getRow(this.endPos.getRow()).getSpace(this.endPos.getCol()).setPiece(this.piece);

        if (this.isJump()){
            this.capturePiece();
        }

        switch (this.piece.getColor()){
            case RED:
                if(this.endPos.getRow() == 7){
                    makeKing();
                }
            case WHITE:
                if (this.endPos.getRow() == 0){
                    makeKing();
                }
        }
        return tempBoard;
    }

    /**
     * Makes piece king if qualifications are met.
     */
    private void makeKing(){
        if (this.piece.getType() == Piece.Type.SINGLE){
            this.piece.setKing();
        }
    }

    /**
     * Tests to see if the Move was a jump.
     * @return
     */
    public boolean isJump(){
        switch (this.piece.getColor()){
            case RED:
                if (this.piece.getType() == Piece.Type.SINGLE){
                    if(this.endPos.getCol() - this.startPos.getCol() == 2){
                        return true;
                    }

                }
            case WHITE:
                if (this.piece.getType() == Piece.Type.SINGLE){
                    if(this.endPos.getCol() - this.startPos.getCol() == -2){
                        return true;
                    }
                }
            default:
                if(this.piece.getType() == Piece.Type.KING){
                    if((this.endPos.getCol() - this.startPos.getCol() == 2) || this.endPos.getCol() - this.startPos.getCol() == 2){
                        return true;
                    }
                }
                return false;

        }

    }

    /**
     * Sets the position of Piece to be captured.
     */
    private void capturePiece(){
        int row = (this.startPos.getRow() + this.endPos.getRow())/2;
        int col = (this.startPos.getCol() + this.endPos.getCol())/2;
        this.boardView.getRow(row).getSpace(col).removePiece();
    }

}
