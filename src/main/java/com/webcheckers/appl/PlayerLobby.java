package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Player Lobby provides sitewide features for the game and players.
 */
public class PlayerLobby {
   
    //Logger used for debugging 
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    //List of players
    private ArrayList<Player> currentUsers;

    //Status enums used for creating usernames
    //Vicky: passing around messages: smart
    public enum ValidUsername {TAKEN, INVALID, FREE}
    
    private ValidUsername lastUser = null;

    //Message strings 
    public static final String LOG_IN_SUCCESSFUL = "Log-in Successful!";
    public static final String USER_NAME_TAKEN = "That username is already taken. Please try a new name!";
    static final String NOT_A_VALID_INPUT = "Please enter valid alphanumeric characters.";
    static final String SIGN_OUT = "%s has successfully left the game.";

    //
    // Constructors
    //

    /**
     * Creates new Lobby object that contains 
     */
    public PlayerLobby() {
        this.currentUsers = new ArrayList<>();
    }

    //
    // Public methods
    //

    /**
     * Checks validity of username, returns message stating whether
     * log-in was successful or not. Sets validity.
     * @param user - username to check.
     * @return Message whether successful or not.
     */
    public synchronized String getUserMsg(String user){
        LOG.fine("Beginning getUserMsg");
        for (Player x:this.currentUsers) {
            LOG.fine(x.getName());
            if(x.getName().equals(user)){
                this.lastUser = ValidUsername.TAKEN;
                return USER_NAME_TAKEN;
            }
        }
        if(this.checkValid(user)){
            LOG.fine("user.isValid.");
            this.lastUser = ValidUsername.INVALID;
            return NOT_A_VALID_INPUT;
        }
        else{
            LOG.fine("Made it to else.");
            this.lastUser = ValidUsername.FREE;
            this.currentUsers.add(newPlayer(user));
            return LOG_IN_SUCCESSFUL;
        }
    }
    /**
     * Checks to see if the username has already been taken. If has not, adds
     * it to the list.
     * @return Boolean value, true if successfully logged in, false if invalid.
     */
    public synchronized ValidUsername isValid(){
        return this.lastUser;
    }

    /**
     * Checks to see if the username is valid.
     * @param user - username to be checked.
     * @return boolean value, true if valid, false if not.
     */
    public boolean checkValid(String user){
        for(int x = 0; x < user.length(); x++) {
            LOG.fine(String.format("This letter is: %c. Girth is: %d", user.charAt(x), user.length()));
            if(!Character.isAlphabetic(user.charAt(x)) && !Character.isDigit(user.charAt(x))){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the total number of users that are actively logged in.
     * @return integer with the number of users logged in.
     */
    public synchronized int getNumUsers(){
        return this.currentUsers.size();
    }


    /**
     * Gets list of usernames of those that are actively logged in.
     * @return List of usernames of those that are currently logged in.
     */
    public synchronized ArrayList<Player> getUserNames(){
        return this.currentUsers;
    }

    /**
     * Get a new Player object to provide client-specific services
     * to the client who just connected to this application.
     * 
     * @param name: Name of the new player 
     * @return a new PlayerServices
     */
    public Player newPlayer(String name){
        LOG.fine("New Player instance created.");
        return new Player(this, name);
    }

    /**
     * Gets player object from name provided
     * 
     * @param user The name of the player 
     * @return the player object if it exists
     */
    public Player getUser(String user){
        for (Player x:this.currentUsers) {
            if(x.getName().equals(user)){
                return x;
            }
        }
        return null;
    }

    /**
     * Signs out user from the server
     * 
     * @param player: player to be signed out
     * @return Sign-Out message 
     */
    public String removeUser(Player player){
        currentUsers.remove(player);
        return String.format(SIGN_OUT, player.getName());
    }
}
