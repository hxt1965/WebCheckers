//package com.webcheckers.util;
//
//
//import com.webcheckers.appl.GameCenter;
//import com.webcheckers.model.Move;
//import com.webcheckers.model.Position;
//
//public class QueryParser {
//
//    //
//    // Attributes
//    //
//
//    private int startRow;
//    private int startCol;
//    private int endRow;
//    private int endCol;
//    private String startPos;
//    private String endPos;
//    private String movement;
//    private Move move;
//
//    //
//    //
//    //
//    public QueryParser(String movement){
//
//        this.movement = movement;
//        this.move = null;
//        this.parsePos();
//
//    }
//
//    private void parsePos(){
//        this.startPos = movement.split("},\"")[0];
//        this.endPos = movement.split("},\"")[1];
//        this.parseInt();
//    }
//    private void parseInt(){
//
//        String[] x = this.startPos.split(",\"");
//        this.startRow = x[0].charAt(x[0].length() -  1);
//        this.startCol = x[1].charAt(x[1].length() - 1);
//
//        String[] y = this.endPos.split(",\"");
//        this.endRow = y[0].charAt(y[0].length() -  1);
//        this.endCol = y[1].charAt(y[1].length() - 1);
//
//        this.setMove();
//    }
//
//    public Move getMove(){
//        return this.move;
//    }
//
//    private void setMove(){
//        this.move = new Move(new Position(this.startRow, this.startCol), new Position(this.endRow, this.endCol));
//    }
//}
