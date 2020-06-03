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

public class PostResignGame implements Route{
    // Creates logger for class.
    private static final Logger LOG = Logger.getLogger(PostResignGame.class.getName());

    //
    // Constants
    //

    //
    // Values used in the View-Model map for rendering the signout view after signout.
    //

    // Key in HashMap to get the player object
    static final String PLAYER_KEY = "key";

    // Key used in HashMap to display Game Over.
    static final String GAME_OVER = "isGameOver";


    //
    // Attributes
    //


    // GameView associated with the current game.
    private  GameCenter gameCenter;

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;


    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameCenter - Contains data on all the games being played.
     * @param templateEngine - Freemarker template engine used to render ftl file

     */
    PostResignGame(final GameCenter gameCenter, final TemplateEngine templateEngine){
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
        LOG.fine("Resign Game accessed.");
        final Session httpSession = request.session();
        Gson gson = new Gson();
        Message message;
        Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        GameView gameView = gameCenter.getGame(player.getGameID());
        gameView.setGameOver(GameView.GameOver.RESIGN);
        if (gameCenter.hasGame(player.getGameID())){
            message = Message.info(gameView.gameOver(player, gameView.getEnemyPlayer(player)));
        }
        else {
            message = Message.error(String.format("Could not find game associated with %s", player.getName()));
        }


        return gson.toJson(message);
    }
}
