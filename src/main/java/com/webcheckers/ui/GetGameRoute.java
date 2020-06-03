package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.webcheckers.model.GameView;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * The UI controller to GET the Game url
 */
public class GetGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    //
    // States for the state-based implementation.
    //
    private enum State {
        STARTING_PLAY_MODE, PLAYING_MY_TURN, WAITING_FOR_MY_TURN, WAITING_TO_EXIT_THE_GAME
    }

    //
    // Values used in the View-Model map for rendering the game view after choosing opponent.
    //

    // Key for HashMap for title.
    final static String TITLE = "title";

    // Key for gameview in HashMap (vm)
    final static String VIEW = "viewMode";

    // Key in HashMap to get the player object
    final static String PLAYER_KEY = "key";

    // Key for accesing JSON
    final static String JSON = "modeOptionsAsJSON";

    // Key for a checkers piece being red color
    final static String RED = "redPlayer";

    // Key for a checkers piece being white color
    final static String WHITE = "whitePlayer";

    // Key for the current color of the checkers piece 
    final static String COLOR = "activeColor";

    // Key for BoardView object
    final static String BOARD = "board";

    // Used to store begin game message
    final static String BEGIN = "Match is beginning";

    // Key used in HashMap to display Game Over.
    static final String GAME_OVER = "isGameOver";

    // Key used in HashMap to show Game Over message.
    static final String RESULTS = "gameOverMessage";

    // Key used in HashMap to show current user
    static final String CURRENT = "currentUser";


    //
    // Attributes
    //

    // Object later used to access GameView for calling player
    private GameCenter gameCenter;

    // Freemarker template engine used to render ftl file
    private TemplateEngine templateEngine;

    // Message object used to inform player that the game has begun
    private Message message;


    // Mapping of Strings and Objects for JSON.
    private Map<String, Object> modeOptions = new HashMap<>();


    //
    // Constructor
    //

    /**
     * Creates a route for handling information to the controller in JavaScript.
     *
     * @param gameCenter     the Game Center for the session.
     * @param templateEngine the HTML template rendering engine
     */
    GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "Template Engine must not be null");
        Objects.requireNonNull(gameCenter, "GameCenter must not be null");
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }

    //
    // Public Methods
    //

    /**
     * Renders the game page by providing information about
     * the view components, players and creates boardView for
     * respective player
     *
     * @param request:  the HTTP request
     * @param response: the HTTP response
     * @return The rendered FTL for the game page
     */
    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();
        LOG.fine("GetGameRoute is invoked.");
        Session httpSession = request.session();
        Player player = httpSession.attribute(PLAYER_KEY);
        LOG.fine(player.getGameID() + " is my game ID.");
        GameView gameView = this.gameCenter.getGame(player.getGameID());


//        switch (this.currentState) {
//            case STARTING_PLAY_MODE:


//            case PLAYING_MY_TURN:
//                break;
//
//            case WAITING_FOR_MY_TURN:
//
//                break;

//            case WAITING_TO_EXIT_THE_GAME:
//
//
//                break;
//
//            default:
//        if(gameView.isGameOver()){
//
//        }
//        else{
//            if(gameView.isActivePlayer(player)){
//                this.currentState = State.PLAYING_MY_TURN;
//            }
//            else if (gameView.isActivePlayer(gameView.getEnemyPlayer(player))){
//                this.currentState = State.WAITING_FOR_MY_TURN;
//            }
//        }

//                throw new IllegalStateException("There's nothing else in this game.");
//        }

        // Sends Player back to the Home Page.
        if(gameView == null)
        {
            vm.put(GetHomeRoute.TITLE, GetHomeRoute.HOME);
            vm.put(GetHomeRoute.NUM_OF_USERS, player.getPlayerLobbyCount());
            vm.put(GetGameRoute.CURRENT, player);
            vm.put(GetHomeRoute.GET_PLAYERS, player.getPlayerLobby());
            return templateEngine.render(new ModelAndView(vm, GetHomeRoute.HOME));
        }
        else if (gameView.isGameOver()){

            if(gameView.getCurrentPlayer().getPrestige() == GameView.Prestige.WINNER) {
                vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info(String.format("Congratulations %s!, you won!\n%s", player.getName(), gameView.gameOver(gameView.getEnemyPlayer(player), player))));
            }
            else{
                vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info(String.format("You have lost %s! Try Again!\n%s",player.getName(), gameView.gameOver(player, gameView.getEnemyPlayer(player)) )));
            }
        }
        BoardView board = gameView.getBoardView();
        gameView.setCurrentPlayer(player);

        gameView.getMoveList().clearMoves();
        if(gameView.isGameOver()) {
            Gson gson = new Gson();
            modeOptions.put(GAME_OVER, gameView.isGameOver());
            modeOptions.put(RESULTS, gameView.gameOver(gameView.getEnemyPlayer(player), player));
            vm.put(JSON, gson.toJson(modeOptions));
            player.setPlaying(!player.isPlaying());
            gameView.exit();
        }
        vm.put(TITLE, "Game");
        vm.put(CURRENT, gameView.getCurrentPlayer());
        vm.put(VIEW, gameView.getMode());
        vm.put(RED, gameView.getRedPlayer());
        vm.put(WHITE, gameView.getWhitePlayer());
        vm.put(COLOR, gameView.getActiveColor());
        vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info(String.format("Waiting on %s.....", gameView.getEnemyPlayer(player).getName())));
        board.initializeBoard();
        if (gameView.getWhitePlayer().equals(gameView.getCurrentPlayer())) {
            board.reverseBoard();
        }
        vm.put(BOARD, board);
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.GAME));

//        gameView.getMoveList().clearMoves();
//        vm.put(CURRENT, player);
//        vm.put(BOARD, gameView.getBoardView());
//        vm.put(VIEW, gameView.getMode());
//        vm.put(RED, gameView.getRedPlayer());
//        vm.put(WHITE, gameView.getWhitePlayer());
//        vm.put(TITLE, "Game");
//        vm.put(COLOR, gameView.getActiveColor());
//        vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info("I end my move."));
//        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.GAME));
 }
}
