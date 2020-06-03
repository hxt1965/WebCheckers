package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The UI controller to GET the Sign-In url
 */
public class GetSigninRoute implements Route {

    // Freemarker template engine used to render ftl file
    private final TemplateEngine templateEngine;

    /**
     * Creates UI controller instance for sign in ftl
     * 
     * @param templateEngine - Freemarker template engine used to render ftl file
     */
    GetSigninRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * Renders the sign in page 
     * 
     * @return the rendered ftl for sign-in page
     */
    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE, "Sign In");

        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.SIGNIN));
    }

}
