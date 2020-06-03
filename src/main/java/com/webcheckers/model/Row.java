package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Represents a Row on BoardView
 */
public class Row implements Iterable<Space>{
    //
    // Constants
    //

    final static int MAX_SIZE = 8;

    //
    // Attributes
    //

    //index of row on board
    private int index;

    //Spaces for checkers pieces to reside in
    private ArrayList<Space> spaces = new ArrayList<>();

    //
    // Constructors
    //

    /**
     * Creates an instance of the row
     *
     * @param index location of row on board
     */
    public Row(int index){
        if (index < 0 || index > 7) {
            throw new IllegalArgumentException(index + " is not a valid row. Must be between 0 and 7.");
        } else {
            this.index = index;
            this.spaces.add(new Space(index, 0));
            this.spaces.add(new Space(index, 1));
            this.spaces.add(new Space(index, 2));
            this.spaces.add(new Space(index, 3));
            this.spaces.add(new Space(index, 4));
            this.spaces.add(new Space(index, 5));
            this.spaces.add(new Space(index, 6));
            this.spaces.add(new Space(index, 7));

        }
    }

    //
    // Public Methods
    //

    /**
     * Gets the index of the row
     *
     * @return - the index of the row
     */
    public int getIndex(){
        return this.index;
    }

    public Space getSpace(int index){
        return this.spaces.get(index);
    }
    /**
     * Iterator to help traverse through Spaces
     */
    @Override
    public Iterator<Space> iterator() {
        return this.spaces.iterator();
    }

    /**
     * Adds Space object to each row
     */
    public void initializeRow() {
        for (Iterator<Space> iter = iterator(); iter.hasNext();) {
            Space space = iter.next();
            space.initializeSpace();
        }
    }

    /**
     * Reverses the entire row
     */
    public void reverseRow() {
        Collections.reverse(this.spaces);
    }
}

