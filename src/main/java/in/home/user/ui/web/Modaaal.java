package in.home.user.ui.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

@Data
@AllArgsConstructor
public class Modaaal {
    private OAuth2AuthorizedClient oAuth2AuthorizedClient;
}
