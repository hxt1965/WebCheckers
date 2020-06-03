package com.webcheckers.appl;


import com.webcheckers.model.BoardView;
import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author <a href ='mailto:jrv@se.rit.edu'>Paul Woods</a>
 */

public class GameCenter {

    //used for error checking
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    //list of GameView objects 
    private ArrayList<GameView> gameViews;

    //stores gameId and boardViews for all players 
    private HashMap<Integer, BoardView> games;

    // Assume the key is the player who starts the game.
    private HashMap<Player, Player> activeGames;

    private Player player;




    //
    // Constructors
    //

    public GameCenter(){
        this.gameViews = new ArrayList<>();
        this.games = new HashMap<>();
        this.activeGames = new HashMap<>();
    }

    ///
    /// Public methods
    ///

    /**
     * Instantiates a GameView and adds it to the list of games.
     * The player objects are updated to show that they are in-game
     * 
     * @param enemy: the player who was challenged
     * @param you the player who starts the game
     */
    public void newGame(Player enemy, Player you){
        LOG.fine("Entered new game creation zone.");
        GameView gameView = new GameView(enemy, you, GameView.Mode.PLAY, this);
        gameView.setBoardView(new BoardView());
        gameViews.add(gameView);
        this.games.put(gameView.getGameID(), gameView.getBoardView());
        enemy.setID(gameView.getGameID());
        you.setID(gameView.getGameID());
        enemy.setPlaying(true);
        you.setPlaying(true);
        this.activeGames.put(you, enemy);
    }

    /**
     * Gets opponent of the player
     *
     * @param me: The calling player
     * @return my opponent
     */
    public Player getOpponent(Player me) {
        ArrayList<Player> firstPlayers = new ArrayList<Player>(this.activeGames.keySet());
        Player wanted = null;
        for (Player pl: firstPlayers) {
            if (pl.getName().equals(me.getName())) {
                wanted =  pl;
            }
        }
        for (Player pl: firstPlayers) {
            if (this.activeGames.get(pl).getName().equals(me)) {
                wanted = this.activeGames.get(pl);
            }
        }
        return wanted;
    }


    /**
     * Returns the HashMap contaning all the BoardViews
     * 
     * @return the map with BoardView values 
     */
    public HashMap<Integer, BoardView> getGames(){
        return this.games;
    }

    /**
     * Returns the Game that the player is playing according to gameID assigned
     * if it exists, else returns null
     * 
     * @param gameID: gameID pointing towards the GameView being used
     * @return the game view object   
     */
    public GameView getGame(int gameID){
        for (GameView x:gameViews) {
            if(x.getGameID() == gameID){
                return x;
            }
        }
        return null;
    }

    /**
     * Checks to see if game still exists. If true, destroy.
     * @param gameID gameID of player finishing game.
     * @return Boolean value dictating destruction of Game.
     */
    public boolean hasGame(int gameID){
        for(GameView x: gameViews){
            if(x.getGameID() == gameID){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * Finds the particular game based off the Player object and removes it from gameviews ArrayList.
     * @param gameView Player leaving the game.
     */
    public void removeGame(GameView gameView){
        this.gameViews.remove(getGame(player.getGameID()));
    }

    /**
     * used for debugging and locate file
     */
    public void whereAmI(String file){
        LOG.fine(file);
    }
}
