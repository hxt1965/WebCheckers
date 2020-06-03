package com.webcheckers.model;

/**
 * Represents a cell on the Checkers board
 */
public class Space {
    //
    // Constants
    //

    //
    // Attributes
    //

    // Location of this piece is defined by row and cellIdx
    private int row;
    private int cellIdx;


    // Boolean containing value of the freedom of this space.
    private boolean valid;

    // Piece object residing on Space
    private Piece piece;

    //
    // Constructor
    //
    public Space(int row, int col){
        this.row = row;
        this.cellIdx = col;
        this.valid = false;
    }

    public Space(Piece piece, int cellIdx, boolean valid){
        this.piece = piece;
        this.cellIdx = cellIdx;
        this.valid = valid;
    }

    public Space(Space copy){
        this.piece = copy.piece;
        this.cellIdx = copy.cellIdx;
        this.valid = copy.valid;
    }

    //
    // Public Methods
    //

    /**
     * Returns row of space
     * 
     * @return - the row idx of space
     */
    public int getRow(){
        return this.row;
    }

    /**
     * Returns Column of space
     * 
     * @return - the column idx of space
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * Checks if space is a valid one or not
     */
    public boolean isValid(){
        return this.valid;
    }

    /**
     * Initializes all the spaces with pieces and colors
     */
    public void initializeSpace() {
        if ((this.cellIdx % 2 != 0 && this.row % 2 == 0) || (this.cellIdx % 2 == 0 && this.row % 2 != 0)) {
            if (this.row > 4) {
                this.piece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
            } else if (this.row < 3) {
                this.piece = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);
            }
            else {
                this.valid = true;
            }
        }
    }

    /**
     * Returns piece residing on space
     * 
     * @return - piece residing on space
     */
    public Piece getPiece(){
        return this.piece;
    }

    /**
     * Called when updating the destination space with the piece that resides in the start space.
     * @param piece - Piece of the start movement.
     */
    public void setPiece(Piece piece){
        this.piece = piece;
        this.valid = false;
    }

    /**
     * Called when updating the source space when conducting a move.
     */
    public void removePiece(){
        this.piece = null;
        this.valid = true;
    }


}
