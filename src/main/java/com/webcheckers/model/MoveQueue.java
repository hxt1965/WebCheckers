package com.webcheckers.model;

import java.util.ArrayList;

public class MoveQueue {

    private static ArrayList<Move> moveQueue;

    public MoveQueue(){
        this.moveQueue = new ArrayList<>();
    }
    public void addMove(Move move){
        this.moveQueue.add(move);
    }
    public BoardView makeMoves(){
        BoardView boardView = new BoardView(this.moveQueue.get(0).getBoardView());
        for(Move move : this.moveQueue){
            boardView = move.movePiece();
        }
        return boardView;
    }
    public Move getLastMove(){
        return this.moveQueue.get(this.moveQueue.size() - 1);
    }
    public Move undoMove(){
        return this.moveQueue.remove(this.moveQueue.size() - 1);

    }

    public void clearMoves(){
        this.moveQueue.clear();
    }

    public boolean isEmpty(){
        return this.moveQueue.isEmpty();
    }

}
