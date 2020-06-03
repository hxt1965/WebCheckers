package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.logging.Logger;
import com.webcheckers.util.Message;

import java.util.Objects;

public class PostBackUpMove implements Route{
    // Creates logger for class.
    private static final Logger LOG = Logger.getLogger(PostBackUpMove.class.getName());


    //
    // Constants
    //

    //
    // Values used in the View-Model map for rendering the signout view after signout.
    //
    // Key in HashMap to get the player object
    static final String PLAYER_KEY = "key";

    //
    // Attributes
    //

    // Game ID associated with the current game.
    private int gameID;

    // GameView associated with the current game.
    private final GameCenter gameCenter;

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;


    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /backupMove} route handler.
     *
     * @param gameCenter - Contains data on all the games being played.
     * @param templateEngine - Freemarker template engine used to render ftl file

     */
    PostBackUpMove(final GameCenter gameCenter, final TemplateEngine templateEngine){
        // Validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        //
        LOG.config("PostBackupMoveRoute is initialized.");
    }

    /**
     * Renders the game page by providing updated information about
     * the view components, players and creates boardView for
     * respective player
     *
     * @param request: the HTTP request
     * @param response: the HTTP response
     *
     * @return The rendered FTL for the game page
     */
    @Override
    public String handle(Request request, Response response){
        LOG.fine("Back up move accessed.");
        final Session httpSession = request.session();
        Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        GameView gameView = gameCenter.getGame(player.getGameID());
        Message message;
        Gson gson = new Gson();

        if(!gameView.getMoveList().isEmpty()) {
            gameView.getMoveList().undoMove();
            message = Message.info("Successfully backed up that last move.");
        }
        else {
            message = Message.error("No moves left to undo.");
        }

        return gson.toJson(message);
    }
}
