package com.notabarrista.gateway.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public RedirectView checkAuth(@RequestParam(required = false) String redirectUrl, @AuthenticationPrincipal OidcUser oidcUser, Model model,
                                  @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/";
        } else if (!redirectUrl.startsWith("/")) {
            redirectUrl = "/" + redirectUrl;
        }

        return new RedirectView(redirectUrl);
    }

}
