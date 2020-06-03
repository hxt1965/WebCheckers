package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Representation of a Checkers board 
 */

public class BoardView implements Iterable<Row>{

    //
    // Constants
    //
    final static int MAX_SIZE = 8;


    //
    // Attributes
    //
    private ArrayList<Row> rows;

    //
    // Constructor
    //

    /**
     * Creates a new instance of BoardView
     */
    public BoardView(){
        this.rows = new ArrayList<>(MAX_SIZE);
        this.rows.add(new Row(0));
        this.rows.add(new Row(1));
        this.rows.add(new Row(2));
        this.rows.add(new Row(3));
        this.rows.add(new Row(4));
        this.rows.add(new Row(5));
        this.rows.add(new Row(6));
        this.rows.add(new Row(7));
    }
//    public BoardView(BoardView boardView){
//       this.rows = boardView.rows;
//    }

    /**
     * Creates a Board with rows defined already.
     * @param row1 first row.
     * @param row2 second row.
     * @param row3 third row.
     * @param row4 fourth row.
     * @param row5 fifth row.
     * @param row6 sixth row.
     * @param row7 seventh row.
     * @param row8 eighth row.
     */
    public BoardView(Row row1, Row row2, Row row3, Row row4, Row row5, Row row6, Row row7, Row row8){
        this.rows = new ArrayList<>(MAX_SIZE);
        this.rows.add(row1);
        this.rows.add(row2);
        this.rows.add(row3);
        this.rows.add(row4);
        this.rows.add(row5);
        this.rows.add(row6);
        this.rows.add(row7);
        this.rows.add(row8);
    }

    /**
     * Constructs a replica of current BoardView.
     * @param copy Board to be copied.
     */
    public BoardView(BoardView copy){
        this.rows = new ArrayList<>(MAX_SIZE);
        for (Row row: copy){
            this.rows.add(row);
        }
    }

    /**
     * Iterator to go traverse through list of rows
     * 
     * @return Iterator for row objects
     */
    @Override
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }

    /**
     * Creates spaces on each row for checkers pieces to reside in 
     */
    public void initializeBoard() {
        for (Iterator<Row> iter = iterator(); iter.hasNext();) {
            Row currRow = iter.next();
            currRow.initializeRow();
        }
    }

    public Row getRow(int index){
        return this.rows.get(index);
    }

    /**
     * Creates a reversed view of the board
     */
    public void reverseBoard() {
        Collections.reverse(this.rows);
        for (Iterator<Row> iter = iterator(); iter.hasNext();) {
            Row row = iter.next();
            row.reverseRow();
        }
    }

    public ArrayList<Row> getRows() {
        return rows;
    }
}
