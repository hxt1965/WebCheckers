package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;


/**
 * Route handler for {@code POST /signout}
 */
public class PostSignOutRoute implements Route{

    //
    // Values used in the View-Model map for rendering the signout view after signout.
    //

    // Key used in hashMap to refer to player that is signing out 
    static final String SIGNOUT = "currentUser";

    // Key used in HashMap to display successful message
    static final String SUCCESS = "message";


    //
    // Attributes
    //

    // Player lobby contains all information about players and them playing games
    private final PlayerLobby playerLobby;

    // Message object thanking Player for playing
    private final Message goodBye = Message.info("Thank you for playing!");

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signin} route handler.
     * 
     * @param gameCenter - tracks players and games
     * @param playerLobby - keeps track of players
     */
    public PostSignOutRoute(GameCenter gameCenter, PlayerLobby playerLobby){
        // Validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.playerLobby = playerLobby;
    }

    /**
     * Signs out user and redirects to home page 
     * 
     * @param request - The HTTP request
     * @param response - The HTTP response
     */
    @Override
    public String handle(Request request, Response response){
        final Session httpSession = request.session();
        // Start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        //if a player is signed in, sign out 
        if (player != null) {
            vm.put(SIGNOUT, playerLobby.removeUser(player));
            httpSession.removeAttribute(GetHomeRoute.PLAYER_KEY);
        }
        response.redirect(WebServer.HOME_URL);
        return null;

    }
}
