package com.notabarrista.gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.RedirectView;

/**
 * Controller for redirecting the user to Okta login page or the root app URL if already authenticated
 *
 * @author jozsef.sas
 *
 */
@Controller
public class RedirectController {

    @RequestMapping("/authenticate")
    public RedirectView checkAuth(@AuthenticationPrincipal OidcUser oidcUser, Model model,
                                 @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
        return new RedirectView("/");
    }

}
