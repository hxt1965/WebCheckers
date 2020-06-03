package com.webcheckers.model;

public class Position {

    //
    // Attributes
    //

    private int row;
    private int col;

    //
    // Constructor
    //

    /**
     * Creates Position object
     * @param row Int value representing row
     * @param col Int val representing column
     */
    public Position(int row, int col){
       this.row = row;
       this.col = col;
    }

    //
    // Public methods
    //

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    @Override
    public boolean equals(Object pos){
        if(pos instanceof Position){
            if (((Position) pos).getRow() == this.row &&
                    ((Position) pos).getCol() == this.col){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Row: " + getRow() + "\nCol: " + getCol();
    }
}
