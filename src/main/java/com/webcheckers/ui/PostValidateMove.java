package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.GameView;
import com.webcheckers.model.Player;
import com.webcheckers.model.Move;
import spark.*;

import java.util.HashMap;
import java.util.logging.Logger;
import com.webcheckers.util.Message;

import java.util.Objects;

public class PostValidateMove implements Route{
    private static final Logger LOG = Logger.getLogger(PostValidateMove.class.getName());

    //
    // Constants
    //

    //
    // Values used in the View-Model map for rendering game after validating move.
    //

    // Key in HashMap to get the player object
    static final String PLAYER_KEY = "key";

    // Key used in hashMap to refer to player that is taking their turn.
    static final String CURRENT = "currentUser";

    // Key for HashMap for title.
    private final static String TITLE = "title";

    // Key for gameview in HashMap (vm)
    private final static String VIEW = "viewMode";

    // Key for accesing JSON
    private final static String JSON = "modeOptionsAsJSON";

    // Key for a checkers piece being red color
    private final static String RED = "redPlayer";

    // Key for a checkers piece being white color
    private final static String WHITE = "whitePlayer";

    // Key for the current color of the checkers piece
    private final static String COLOR = "activeColor";

    // Key for BoardView object
    private final static String BOARD = "board";

    // Used to store begin game message
    private final static String BEGIN = "Let the games begin!";

    // Key in HashMap to access message to be displayed on page
    static final String MESSAGE_ATTR = "message";

    // Key for Move data.
    static final String MOVE = "actionData";



    //
    // Attributes
    //

    // Game ID associated with the Game.
    private int gameID;
    

    // Move object associated with a Piece's movement.
    private Move move;

    // GameView associated with the current game.
    private final GameCenter gameCenter;

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;


    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /validateMove} route handler.
     *
     * @param gameCenter - Contains data on all the games being played.
     * @param templateEngine - Freemarker template engine used to render ftl file

     */
    PostValidateMove(final GameCenter gameCenter, final TemplateEngine templateEngine){
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

        LOG.fine("Validate Move accessed.");
        final Session httpSession = request.session();

        // Sets a default of false.
        Message message = Message.error("This move is not a proper move.");

        Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        GameView gameView = gameCenter.getGame(player.getGameID());
        BoardView board = gameView.getBoardView();
        Gson gson = new Gson();

        this.move = gson.fromJson(request.queryParams(MOVE), Move.class);
        LOG.fine(String.format("Start: %s\nEnd: %s", this.move.getStartPos(), this.move.getEndPos()));

        if(gameView.getMoveList().isEmpty()){
            BoardView boardView = new BoardView(board);
            move.setBoardView(boardView);


            if(gameView.hasJump(player)) {
                if (this.move.isJump()) {
                    message = Message.info("Valid movement made.");
                    gameView.setMoveList(this.move);
                } else {
                    message = Message.error("Must Jump");
                }
            }
            else{
                if(move.isValid()) {
                    message = Message.info("Valid movement made.");
                    gameView.setMoveList(move);
                }
            }
        }else if (gameView.getMoveList().getLastMove().isJump()){
            BoardView makeMoves = new BoardView(gameView.getMoveList().makeMoves());
            move.setBoardView(makeMoves);
            if(move.isJump() && gameView.getMoveList().getLastMove().getEndPos().equals(move.getStartPos())){

                if(move.isValid()){
                    message = Message.info("Valid movement made");
                    gameView.setMoveList(move);
                }
            }

        }
        return gson.toJson(message);
    }
}
