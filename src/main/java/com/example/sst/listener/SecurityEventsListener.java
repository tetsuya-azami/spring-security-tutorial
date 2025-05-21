package com.example.sst.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityEventsListener {
    @EventListener(AuthenticationSuccessEvent.class)
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("Authentication SUCCESS event received. Authentication: {}", event.getAuthentication());
    }

    // Be careful with the authentication failure listener, because the authentication event will
    // contain Authentication with credentials (as the authenticated authentication was not created),
    // so avoid logging it in the production
    @EventListener(AbstractAuthenticationFailureEvent.class)
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        log.warn("Authentication FAILURE event received. Authentication: {}", event.getAuthentication());
    }
}
