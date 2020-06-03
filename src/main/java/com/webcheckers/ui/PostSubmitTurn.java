package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.webcheckers.util.Message;

import java.util.Objects;

public class PostSubmitTurn implements Route{
    // Creates logger for class.
    private static final Logger LOG = Logger.getLogger(PostSubmitTurn.class.getName());

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


    // GameView associated with the current game.
    private final GameCenter gameCenter;

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;

    // Message object confirming Move was proper.
    private Message message;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /submitTurn} route handler.
     *
     * @param gameCenter - Contains data on all the games being played.
     * @param templateEngine - Freemarker template engine used to render ftl file

     */
    PostSubmitTurn(final GameCenter gameCenter, final TemplateEngine templateEngine){
        // Validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
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
        LOG.fine("Submit Turn accessed.");

        // Retrieve Http session.
        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        GameView gameView = this.gameCenter.getGame(player.getGameID());
        Gson gson = new Gson();

        if(!gameView.getMoveList().isEmpty()){

            gameView.makeMoves();
            gameView.getMoveList().clearMoves();
            gameView.endTurn();
            this.message = Message.info(String.format("Submitting turn! It is now %s's turn!", gameView.getCurrentPlayer().getName()));
        }
        else {
            this.message = Message.error(String.format("%s has no moves to submit!", gameView.getCurrentPlayer().getName()));
        }

        if(!gameView.hasMove(gameView.getRedPlayer()))
        {
            gameView.getRedPlayer().setPrestige(GameView.Prestige.LOSER);
            gameView.getWhitePlayer().setPrestige(GameView.Prestige.WINNER);
        }
        else if (!gameView.hasMove(gameView.getWhitePlayer()))
        {
            gameView.getRedPlayer().setPrestige(GameView.Prestige.WINNER);
            gameView.getWhitePlayer().setPrestige(GameView.Prestige.LOSER);
        }

        return gson.toJson(this.message);
    }
}
