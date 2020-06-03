package com.webcheckers.ui;


import com.webcheckers.model.Player;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Objects;
import java.util.logging.Logger;

public class SessionTimeoutWatchdog implements HttpSessionBindingListener {
    private static final Logger LOG = Logger.getLogger(SessionTimeoutWatchdog.class.getName());
    private final Player player;

    public SessionTimeoutWatchdog(final Player player){
        LOG.fine(String.format("%s has a new watch dog! His name is Simon!", player.getName()));
        this.player = Objects.requireNonNull(player);
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event){
        // Ignore me!
        LOG.fine(String.format("%s session started!", this.player.getName()));
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event){
        // The session is over, and the doggy is gone!
    //    player.endSession();

        LOG.fine("Player has left the game.");
    }
}
