// package in.home.user.ui.web;
//
// import in.home.user.api.model.User;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
// import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.reactive.function.client.WebClient;
//
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//
// import static org.springframework.security.oauth2.client.web.reactive.function.client
// .ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
//
// @RestController
// public class MessagesController {
//
//     @Autowired
//     private WebClient webClient;
//
//     @Value("${messages.base-uri}")
//     private String messagesBaseUri;
//
//     @GetMapping(value = "/")
//     public void authenticate(@RegisteredOAuth2AuthorizedClient("user-authorization-code")
//     OAuth2AuthorizedClient authorizedClient, HttpServletResponse response)
//             throws IOException {
//         this.webClient.get()
//                 .uri(messagesBaseUri)
//                 .attributes(oauth2AuthorizedClient(authorizedClient))
//                 .retrieve()
//                 .bodyToMono(User[].class)
//                 .block();
//         response.sendRedirect("/dashboard");
//     }
// }
