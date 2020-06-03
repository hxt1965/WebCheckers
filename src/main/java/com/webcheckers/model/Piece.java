package com.webcheckers.model;

/**
 * Represents a Checkers piece on the Board
 */
public class Piece {

    //
    // Constants
    //

    // enums for type of checkers piece 
    public enum Type {SINGLE, KING}

    // enums for color of checkers piece 
    public enum Color {RED, WHITE}

    //
    // Attributes
    //

    // type of checkers piece 
    private Type type;

    //color of checkers piece 
    private Color color;


    //
    // Constructor
    //

    /**
     * Creates an instance of the Piece model  
     */
    public Piece(Type type, Color color){
        this.type = type;
        this.color = color;
    }

    // Public Methods

    /**
     * Gets the type of piece 
     * 
     * @return - the type of the piece 
     */
    public Type getType(){
        return this.type;
    }

    /**
     * Gets the color of piece 
     * 
     * @return - the color of the piece 
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Sets the TYPE value of this Piece to King.
     */
    public void setKing(){
        this.type = Type.KING;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Piece){
            Piece piece = (Piece) object;
            if(color.equals(piece.color)){
                if(type.equals(piece.type)){
                    return true;
                }
            }
        }
        return false;
    }
}
