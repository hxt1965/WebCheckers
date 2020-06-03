package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;



/**
 * The UI Controller to GET the Home page.
 */
public class GetHomeRoute implements Route {
  
  //Logger used for debugging
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  // Initial home page message before sign in
  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  
  //
  // Values used in the View-Model map for rendering the home view.
  //

  // Key in HashMap to get list of players in from gameCenter
  static final String GET_PLAYERS = "foes";

  // Key in HashMap to get the player object
  static final String PLAYER_KEY = "key";

  // Key in HashMap to get number of users
  static final String NUM_OF_USERS = "numberusers";

  //Key in HashMap to assign and access the current Player object
  static final String NEW_PLAYER = "currentUser";

  // Key in HashMap to access the title of the page 
  static final String TITLE = "title";

  static final String TIMEOUT_KEY = "watchdog";

  static final String ENEMY = "enemy";
  static final String CHALLENGE = "challenge";

  // Key in HashMap to access message to be displayed on page 
  static final String MESSAGE_ATTR = "message";

  // Key in HashMap to figure out whether current Player is the only one logged in
  static final String NO_OTHER_PLAYERS = "lonely";

  // Length of the session timeout in seconds
  static final int TIMEOUT_PERIOD = 120;

  static final String ALONE = "There are no other players available to play at this time.";
  static final String INVALID_PLAYER = "This player is currently playing a game. Try again later!";


  //
  //  Navigation
  //

  // Creates a bunch of constants to make navigation easier.
  static final String GAME = "game.ftl";
  static final String HOME = "home.ftl";
  static final String SIGNIN = "signin.ftl";


  //
  //  Attributes
  //

  private final GameCenter gameCenter;

  // Freemarker template engine used to render ftl file
  private final TemplateEngine templateEngine;

  // PlayerLobby instance to access info about players and their games
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param gameCenter
   *   the GameCenter for the application
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
    // validation
    Objects.requireNonNull(gameCenter, "gameCenter is required");
    Objects.requireNonNull(templateEngine, "templateEngine is required");
    Objects.requireNonNull(playerLobby, "playerLobby is required");

    this.gameCenter = gameCenter;
    this.templateEngine = templateEngine;
    this.playerLobby = playerLobby;
  
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request: the HTTP request
   * @param response: the HTTP response
   *
   * @return the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.fine("GetHomeRoute is invoked.");

    // start building the View-Model
    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE, "Welcome!");

    // display a user message in the Home page
    vm.put(MESSAGE_ATTR, WELCOME_MSG);
    vm.put(NEW_PLAYER, null);

    vm.put(GET_PLAYERS, playerLobby.getUserNames());
    Session session = request.session();
    session.attribute(ENEMY, vm.get(CHALLENGE));

    //if a player has logged in, a Player object session attribute is created
    if (session.attribute(PLAYER_KEY) != null) {
      vm.put(NEW_PLAYER, session.attribute(PLAYER_KEY));
      final Player player = session.attribute(PLAYER_KEY);
      // if the player had recently made an invalid selection of opponent
      // who is already in a game, display error message.
      if (player.getSelection()) {
        vm.put(MESSAGE_ATTR, WELCOME_MSG.error(INVALID_PLAYER));
        // reset the invalid selection now that the player has been notified.
        player.setSelection(false);
      }
      if (player.isPlaying()) {
        response.redirect(WebServer.GAME_URL);
        return null;
      }
    }


    vm.put(NUM_OF_USERS, playerLobby.getNumUsers());

    return templateEngine.render(new ModelAndView(vm, HOME));
  }
}
