package com.example.sst.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;

import java.util.List;

// Let's define the authentication manager in another config in order not to create a circular
// dependency
@Configuration
public class AuthenticationManagerConfig {
    @Bean
    public AuthenticationManager authenticationManager(
            List<AuthenticationProvider> authenticationProviders, ApplicationEventPublisher applicationEventPublisher) {
        ProviderManager providerManager = new ProviderManager(authenticationProviders);
        
        providerManager.setAuthenticationEventPublisher(
                new DefaultAuthenticationEventPublisher(applicationEventPublisher));

        return providerManager;
    }
}