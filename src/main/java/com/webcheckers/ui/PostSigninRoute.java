package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import spark.*;

import com.webcheckers.util.Message;

/**
 * Route handler for {@code POST /signin}
 */
public class PostSigninRoute implements Route {

    //
    // Values used in the View-Model map for rendering the signin view after signin.
    //

    // Key used in HashMap to access player username entry
    static final String USER_INPUT = "username";

    //Key used in HashMap to access message to be displayed 
    static final String MESSAGE_ATTR = "message";

    //Key used in HashMap to store current player
    static final String RESERVED_NAME = "myName";

    //Message object used to display welcome message
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

    //
    // Attributes
    //

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;
    
    // PlayerLobby instance to access info about players and their games
    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param gameCenter     {@Link GameCenter} TODO
     * @param templateEngine template engine to use for rendering HTML page
     * @throws NullPointerException when the {@code gameCenter} or {@code templateEngine} parameter is null
     */
    public PostSigninRoute(GameCenter gameCenter, TemplateEngine templateEngine, PlayerLobby playerLobby) {
        // validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    /**
     * Redirect to home page if sign-in is successful, else go back to sign-in page for another try
     *
     * @param request - The HTTP request
     * @param response - The HTTP response
     */
    @Override
    public String handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        // retrieve the game object
        final Session session = request.session();
        // A null PlayerServices indicates a timed out session or an illegal request on this URL.
        // In either case, we will redirect back to home.
        // retrieve request parameter
        final String input = request.queryParams(USER_INPUT);

        // put the wanted user name and create the appropriate ModelAndView for rendering

        String msg = playerLobby.getUserMsg(input);
        if (PlayerLobby.ValidUsername.FREE == playerLobby.isValid()) {
            // if the name is valid, it becomes reserved for this user.
            vm.put(RESERVED_NAME, input);

            // Attaches a Player to this session.
            session.attribute(GetHomeRoute.PLAYER_KEY, playerLobby.getUser(input));


            // Welcome messages
            vm.put(GetHomeRoute.TITLE, "Welcome!");
            vm.put(MESSAGE_ATTR, WELCOME_MSG);
            vm.put(GetHomeRoute.NEW_PLAYER, session.attribute(GetHomeRoute.PLAYER_KEY));
            vm.put(GetHomeRoute.GET_PLAYERS, playerLobby.getUserNames());
            vm.put(GetHomeRoute.NUM_OF_USERS, playerLobby.getNumUsers());

            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(new ModelAndView(vm, GetHomeRoute.HOME));
        } else {
            // if the name is invalid, it gives error msg and stays at sign-in page

            vm.put(MESSAGE_ATTR, WELCOME_MSG.error(msg));

            // Sign-in messages
            vm.put(GetHomeRoute.TITLE, "Sign In");
            return templateEngine.render(new ModelAndView(vm, GetHomeRoute.SIGNIN));
        }




    }
}