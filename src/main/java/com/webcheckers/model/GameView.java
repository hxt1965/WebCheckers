package com.webcheckers.model;


import com.webcheckers.appl.GameCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Represents a Game being played.
 */
public class GameView implements Control {
    private static final Logger LOG = Logger.getLogger(GameView.class.getName());

    // Game can have different statuses
    public enum Mode {PLAY, SPECTATOR, REPLAY}
    public enum Color {RED, WHITE}
    public enum GameOver{NONE, RESIGN, NO_PIECES, NO_MOVES}
    public enum Prestige{WINNER, LOSER, NONE}

    //
    // Constants
    //




    //
    // Attributes
    //

    // Creates a variable to store boardView associated with game.
    private BoardView boardView;

    //the player bieng challenged 
    private Player whitePlayer;

    //the player who starts the game 
    private Player redPlayer;

    // Current Player
    private Player currentPlayer;

    // The active color in this session.
    private Color activeColor;

    //id of the game bieng played 
    private static int gameID = 1;

    // Status of the game view
    private Mode mode;

    // Game over status.
    private GameOver gameOver;

    // Game over string.
    private String over;

    // Movement this round.
    private MoveQueue moves;

    // Prestige awards to victors and the defeated.
    private Prestige prestige;

    private GameCenter gameCenter;

    //
    // Constructor
    //

    /**
     * Creates an instance of the Game View Model 
     * 
     * @param enemyPlayer - the enemy player
     * @param protagonist - player who started the game
     * @param gameCenter - keeps track of players and games
     */
    public GameView(Player enemyPlayer, Player protagonist, Mode mode, GameCenter gameCenter){
        this.gameID += 1;
        this.redPlayer = protagonist;
        this.whitePlayer = enemyPlayer;
        LOG.fine(whitePlayer.getName() + " is my opponent's name");
        setColor();
        setGameID();
        this.mode = mode;
        this.boardView = null;
        this.activeColor = Color.RED;
        this.moves = new MoveQueue();
        this.gameOver = GameOver.NONE;
        this.gameCenter = gameCenter;


    }

    //
    // Public Methods
    //

    /**
     * At end of turn, swap the color of the active color.
     */
    public void endTurn(){
        if(this.activeColor == Color.RED) {
            this.activeColor = Color.WHITE;
        }
        else {
            this.activeColor = Color.RED;
        }
    }

    /**
     * Tests to see if the Player that's signed in is the active Player.
     * @param player - current Player in game.
     * @return Boolean value indicating if it's the Player's turn.
     */
    public boolean isActivePlayer(Player player){
        return (getActiveColor() ==  getColor(player));
    }

    /**
     * Returns the active color in the game.
     * @return An enumeration of Color depending on turn state.
     */
    public Color getActiveColor() {
        return this.activeColor;
    }


    public boolean hasGame(Player player){
        return this.redPlayer.equals(player) | this.whitePlayer.equals(player);
    }

    public boolean hasMove(Player player){

        Piece.Color color = Piece.Color.WHITE;
        if(this.redPlayer.equals(player)){
            color = Piece.Color.RED;
        }

        //Look through each valid space.
        for(int row = 0; row < 8; row++){
            for (int space = (row + 1) %2; space < 8; space+=2){

                // Look for a piece.
                if(this.boardView.getRow(row).getSpace(space).getPiece() != null){

                    // Verify color
                    if(this.boardView.getRow(row).getSpace(space).getPiece().getColor() == color){

                        Position startPos = new Position(row, space);

                        // Check every Move possible. Return true if a move is a valid.
                        // Checks right first.
                        if((row + 1 < 8) && (space + 1 <8)) {
                            Position endPos = new Position((row + 1), (space + 1));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }
                        // Checks left.
                        if((row + 1 < 8) && (space - 1 >0)) {
                            Position endPos = new Position((row + 1), (space - 1));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }

                        // Checks right white.
                        if((row - 1 > 0 ) && (space + 1 <8)) {
                            Position endPos = new Position((row - 1), (space + 1));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }
                        // Checks left white.
                        if((row - 1 > 0) && (space - 1 >0)) {
                            Position endPos = new Position((row - 1), (space - 1));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }

                        // Checks right first.
                        if((row + 2 < 8) && (space + 2 <8)) {
                            Position endPos = new Position((row + 2), (space + 2));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }
                        // Checks left king.
                        if((row + 2 < 8) && (space - 2 >0)) {
                            Position endPos = new Position((row + 2), (space - 2));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }

                        // Checks right white King.
                        if((row - 2 > 0 ) && (space + 2 <8)) {
                            Position endPos = new Position((row - 2), (space + 2));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }
                        // Checks left White King
                        if((row - 2 > 0) && (space - 2 >0)) {
                            Position endPos = new Position((row - 2), (space - 2));
                            Move move = new Move(startPos, endPos);
                            move.setBoardView(this.boardView);
                            if (move.isValid()) return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    private boolean checkJump(Position position){

        // Jump to the left.
        if((position.getRow() + 2 < 8) && (position.getCol() + 2 < 8)){
            Position endPos = new Position(position.getRow() + 2, position.getCol() + 2);
            Move move = new Move(position, endPos);
            move.setBoardView(this.boardView);
            if(move.isJump()) return  true;
        }

        // Jump to the right.
        if((position.getRow() + 2 < 8) && (position.getCol() - 2 > 0)){
            Position endPos = new Position(position.getRow() + 2, position.getCol() - 2);
            Move move = new Move(position, endPos);
            move.setBoardView(this.boardView);
            if(move.isJump()) return  true;
        }

        //Jump to the right.
        if((position.getRow() - 2 > 0) && (position.getCol() + 2 < 8)){
            Position endPos = new Position(position.getRow() -2, position.getCol() + 2);
            Move move = new Move(position, endPos);
            move.setBoardView(this.boardView);
            if(move.isJump()) return  true;
        }
        // Jump to the left.
        if((position.getRow() - 2 > 0) && (position.getCol() -2 > 0)){
            Position endPos = new Position(position.getRow() - 2, position.getCol() - 2);
            Move move = new Move(position, endPos);
            move.setBoardView(this.boardView);
            if(move.isJump()) return  true;
        }
        return false;


    }

    public boolean hasJump (Player player){
        Piece piece;

        Piece.Color color = Piece.Color.RED;
            if(this.whitePlayer.equals(player)){
                color = Piece.Color.WHITE;
            }
            // Look through each valid space
            for (int row = 0; row < 8; row ++){
                for(int space = (row + 2)%2; space < 8; space+=2){

                    // Verifies Piece exists.
                    if(this.boardView.getRow(row).getSpace(space).getPiece() != null){

                        piece = this.boardView.getRow(row).getSpace(space).getPiece();
                        // Check if Piece is ours
                        if(piece.getColor() == color){
                            if(this.checkJump(new Position(row, space))) {
                                return true;
                            }
                        }
                    }
                }
            }
        return false;
    }

    /**
     * Returns the Player object who turn it currently is.
     * @return
     */
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }

    /**
     * Returns the color of the player
     * 
     * @param player - the player object who's color is needed
     */
    public Color getColor(Player player){
        return player.getColor();
    }

    /**
     * Sets color to the players
     */
    public void setColor(){
        this.redPlayer.setColor(Color.RED);
        this.whitePlayer.setColor(Color.WHITE);
    }

    /**
     * Sets the same game ID to both players
     */
    public void setGameID(){
        this.redPlayer.setID(this.gameID);
        this.whitePlayer.setID(this.gameID);
    }

    /**
     * Returns mode of the GameView
     * 
     * @return - the mode of the Game View
     */
    public Mode getMode(){
        return this.mode;
    }

    /**
     * Sets the mode of the GameView 
     */
    public void setMode(Mode mode){
        this.mode = mode;
    }

    /**
     * Returns the red player
     * 
     * @return - the red player
     */
    public Player getRedPlayer(){
        return this.redPlayer;
    }

    /**
     * Returns the white player
     * 
     * @return - the white player
     */
    public Player getWhitePlayer(){
        return this.whitePlayer;
    }

    /**
     * Returns the game ID
     * 
     * @return - the game ID
     */
    public int getGameID(){
        return this.gameID;
    }

    public void backUp(){
        //TODO
    }
    public void submit(){
        //TODO
    }
    public void resign(){

    }
    public void exit(){
        this.redPlayer =null;
        this.currentPlayer = null;
        this.boardView = null;
        this.whitePlayer = null;
        this.moves = null;
        this.gameCenter.removeGame(this);
    }

    /**
     * Creates a method that takes and enum of what conditions cause the game to end.
     * @param gameOver - an Enumeration of GameOver.
     */
    public void setGameOver(GameOver gameOver){
        this.gameOver = gameOver;
    }

    /**
     * Creates a formmatted String that will display to the Players at the end of the game.
     * @param loser - Losing Player.
     * @param winner - Winning Player.
     * @return - String designed to give a detailed explanation on the causes of victory/defeat.
     */
    public String gameOver(Player loser, Player winner){
        switch (this.gameOver){
            case RESIGN:
                this.over =  String.format("%s has forfeited the game. The makes %s the winner!", loser.getName(), winner.getName());
            case NO_MOVES:
                this.over = String.format("%s can no longer move! %s is victorious!", loser.getName(), winner.getName());
            case NO_PIECES:
                this.over = String.format("%s has captured all of %s's pieces!", winner.getName(), loser.getName());
                default:
                    break;
        }
        loser.setPrestige(Prestige.LOSER);
        winner.setPrestige(Prestige.WINNER);
        return this.over;
    }

    /**
     * Tests to see if it's Game Over.
     * @return boolean value dictating if the game has ended.
     */
    public boolean isGameOver(){
        if(this.gameOver == GameOver.NONE){
            return false;
        }
        return true;
    }

    /**
     * Returns the enemy Player.
     * @param player - the user Player object
     * @return the opponent Player object
     */
    public Player getEnemyPlayer(Player player){
        if(this.getWhitePlayer().equals(player)){
            return getRedPlayer();
        }
        else if (this.getRedPlayer().equals(player)){
            return getWhitePlayer();
            } else{
            return null;

        }
    }

    /**
     * Creates a list of valid Move actions taken this turn and stores them.
     * @param move Move action active Player is taking.
     */
    public void setMoveList(Move move){
        this.moves.addMove(move);
    }

    public MoveQueue getMoveList(){
        return this.moves;
    }
    /**
     * Sets the attribute Boardview in GameView
     * @param boardView - BoardView object associated with this GameView.
     */
    public void setBoardView(BoardView boardView){
        this.boardView = boardView;
    }

    /**
     * Returns the current BoardView associated with this game.
     * @return Boardview of this game.
     */
    public BoardView getBoardView() {
        return this.boardView;
    }


    public void makeMoves(){
        this.boardView = this.getMoveList().makeMoves();
    }

    /**
     * Checks if two gameViews are the same
     * 
     * @param obj - the gameView to be compared to this one
     * @return - if gameView is the same or not 
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this) 
            return true;
        if (!(obj instanceof GameView)) 
            return false;
        final GameView that = (GameView) obj;
        return this.gameID == that.gameID && 
            this.redPlayer.equals(that.redPlayer) && 
            this.whitePlayer.equals(that.whitePlayer) && 
            this.mode.equals(that.mode);
    }

    @Override
    public int hashCode(){
        return this.hashCode();
    }

}
