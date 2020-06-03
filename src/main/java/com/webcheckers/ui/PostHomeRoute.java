package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * The Route handler for {@code POST /home}
 */
public class PostHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostHomeRoute.class.getName());

    //
    // Attributes
    //

    // Game Center contains all information about games being played
    private GameCenter gameCenter;

    // Player lobby contains all information about players and them playing games
    private PlayerLobby playerLobby;

    
    // Key used in HashMap to refer to opponent player object 
    static final String OPPONENT = "opponent";

    // Message object used to warn player that an online player is already in a game
    private static final Message WARNING_MSG = Message.info("This player is currently unavailable. Try again later!");
    
    // Warning message for player unavailability
    private static final String WARNING = "This player is currently unavailable. Try again later!";

    /**
     * The constructor for the {@code POST /home} route handler.
     *
     * @param gameCenter - tracks players and games
     * @param playerLobby - keeps track of players
     */
    public PostHomeRoute(GameCenter gameCenter, PlayerLobby playerLobby) {
        Objects.requireNonNull(gameCenter, "gameCenter cannot be null.");
        Objects.requireNonNull(playerLobby, "playerLobby cannot be null.");
        this.gameCenter = gameCenter;
        this.playerLobby = playerLobby;
    }

    /**
     * Handles PostHomeRoute requests by redirecting to game page
     *
     * @param request The HTTP request
     * @param response The HTTP response 
     * @return TemplateEngine rendered
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.fine("PostHomeRoute is invoked.");
        // get the selected opponent
        final String oppoName = request.queryParams(OPPONENT);
        final Player opponent = playerLobby.getUser(oppoName);
        LOG.fine(oppoName + " is my enemy");
        // retrieve the player
        final Session httpSession = request.session();
        final Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final Map<String, Object> vm = new HashMap<>();

        if (opponent.isPlaying()) {
            //  display warning message when clicking unavailable player
            player.setSelection(true);
            response.redirect(WebServer.HOME_URL);
        } else {
            LOG.fine("newGame is created.");
            gameCenter.newGame(opponent, player);
            response.redirect(WebServer.GAME_URL);
        }
        return null;
    }
}