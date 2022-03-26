package in.home.user.ui.feign.configuration;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.CollectionUtils;


@Slf4j
@Configuration
public class FeignConfiguration {

    private static final String AUTHORIZATION_SERVER_NAME = "user-authorization-code";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    // @Bean
    // RequestInterceptor oauth2FeignRequestInterceptor() {
    //     return (requestTemplate -> {
    //         SecurityContext securityContext = SecurityContextHolder.getContext();
    //         Authentication authentication = securityContext.getAuthentication();
    //         authorizedClientService.loadAuthorizedClient(AUTHORIZATION_SERVER_NAME, "user");
    //
    //         if (authentication != null &&
    //             authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
    //             OAuth2AuthenticationDetails details
    //                     = (OAuth2AuthenticationDetails) authentication.getDetails();
    //             requestTemplate.header(AUTHORIZATION_HEADER,
    //                     String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
    //         }
    //     });
    // }

    @Bean
    @Lazy
    public RequestInterceptor feignClientInterceptor() {
        return (requestTemplate -> {
            if (CollectionUtils.isEmpty(requestTemplate.headers()
                    .get("Authorization"))) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, getAuthenticationToken());
            }
        });
    }

    public String getAuthenticationToken() {
        OAuth2AuthorizedClient authorizedClient
                =
                this.authorizedClientService.loadAuthorizedClient(((OAuth2AuthenticationToken) SecurityContextHolder.getContext()
                        .getAuthentication()).getAuthorizedClientRegistrationId(),
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName());
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient)
                .principal(SecurityContextHolder.getContext()
                        .getAuthentication())
                .build();
        // OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId(
        //                 AUTHORIZATION_SERVER_NAME)
        //         .principal(SecurityContextHolder.getContext()
        //                 .getAuthentication())
        //         .build();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientManager.authorize(request);
        String token = "Bearer ";
        if (oAuth2AuthorizedClient != null) {
            final OAuth2AccessToken oAuth2AccessToken = oAuth2AuthorizedClient.getAccessToken();
            token = token + oAuth2AccessToken.getTokenValue();
            log.debug("ACCESS TOKEN TYPE: {}",
                    oAuth2AccessToken.getTokenType()
                            .getValue());
            log.debug("ACCESS TOKEN VALUE: {}", oAuth2AccessToken.getTokenValue());
            log.debug("ACCESS TOKEN SCOPES: {}", oAuth2AccessToken.getScopes());
            log.debug("ACCESS TOKEN EXPIRES TIME: {}", oAuth2AccessToken.getExpiresAt());
            log.debug("ACCESS TOKEN ISSUED TIME: {}", oAuth2AccessToken.getIssuedAt());
        }
        log.debug("BEARER ACCESS TOKEN: {}", token);
        return token;
    }
}
