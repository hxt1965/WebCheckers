package com.webcheckers.model;


import java.util.function.BooleanSupplier;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import spark.Session;

/**
 * Player object to provide client-specific services to
 * the client who just connected to this application.
 */
public class Player {

    // The player's individual game ID
    private int gameID;
    // The playerLobby provides site wide features for the game and players.
    private final PlayerLobby playerLobby;
    // The name of the player
    private String name;
    // Boolean value denotes if player is in game or not
    private boolean activeGame = false;
    // 
    private GameView.Color color;

    // Prestige for the victors.
    private GameView.Prestige prestige;

    //Boolean value indicates if the player is in a game or not
    private Boolean playing = false;

    private Boolean isTurn = false;
    private Boolean invalidSelection = false;

    //
    // Constructors
    //

    /**
     * Constructs a new PlayerServices but waits for the player to want to start a game.
     * 
     * @param playerLobby - playerLobby object provides info
     * @param name - username of the player
     */
    public Player(PlayerLobby playerLobby, String name) {
        this.gameID = 0;
        this.playerLobby = playerLobby;
        this.name = name;
        this.color = null;
        this.prestige = GameView.Prestige.NONE;


    }

    //
    // Public methods
    //

    /**
     * Gets the name of the player
     *
     * @return - name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the id of the game of the player
     *
     * @return - id of the game of the player
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Sets the id of the game of the player
     */
    public void setID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Checks if player is in game
     *
     * @return - true/false
     */
    public Boolean isPlaying() {
        return this.playing;
    }

    /**
     * Player is now playing a game
     */
    public void setPlaying(Boolean tf) {
        this.playing = tf;
    }

    /**
     * Sets the player's turn
     */
    public void setIsTurn(Boolean tf) {
        this.isTurn = tf;
    }

    /**
     * Changes value when player is in a game or not
     */
    public void setGamestatus() {
        this.activeGame = !this.activeGame;
    }


    /**
     * Gets if the player recently made an invalid selection
     */
    public Boolean getSelection() {
        return this.invalidSelection;
    }

    /**
     * Sets whether the player recently made an invalid selection
     */
    public void setSelection(Boolean tf) {
        this.invalidSelection = tf;
    }

    /**
     * Sets player color
     */
    public void setColor(GameView.Color color) {
        this.color = color;
        setGamestatus();
    }

    /**
     * Get current Lobby count to return home after a game.
     * @return integer of the total number of people in the lobby.
     */
    public int getPlayerLobbyCount(){
        return this.playerLobby.getNumUsers();
    }

    /**
     * Sets Players prestige to determine if they won the last game.
     * @param prestige Enumeration of winners and losers.
     */
    public void setPrestige(GameView.Prestige prestige){
        this.prestige = prestige;
    }

    /**
     * Retrieves the Player's current prestige.
     * @return Enumeration of prestige to show off the player's victory or defeat.
     */
    public GameView.Prestige getPrestige() {
        return this.prestige;
    }

    /**
     * Returns the PlayerLobby to the Player after successfully playing a game.
     * @return PlayerLobby Player belongs to.
     */
    public PlayerLobby getPlayerLobby() {
        return this.playerLobby;
    }

    /**
     * Returns the color of the player
     *
     * @return the color of the player
     */
    public GameView.Color getColor() {
        return this.color;
    }

	public Boolean getGameStatus() {
		return activeGame;
	}
}



