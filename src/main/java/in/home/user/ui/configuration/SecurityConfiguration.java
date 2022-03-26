package in.home.user.ui.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,
 * <li>Restrict access to the application, allowing only logged-in users,
 * <li>Set up the login form
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/oauth2/authorization/user-oidc";
    private static final String LOGIN_FAILURE_URL = "/oauth2/authorization/user-oidc";
    private static final String LOGIN_URL = "/oauth2/authorization/user-oidc";
    private static final String LOGIN_SUCCESS_URL = "/";
    private static final String LOGOUT_SUCCESS_URL = "/oauth2/authorization/user-oidc";
    private static final String LOGOUT_URL = "/logout";

    // @Autowired
    // private UserDetailsService userDetailsService;

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf()
                .disable()
                // Register our CustomRequestCache, that saves unauthorized access attempts, so
                // the user is redirected after login.
                // .requestCache()
                // .requestCache(new CustomRequestCache())
                // Restrict access to our application.
                // .and()
                .authorizeRequests()
                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest)
                .permitAll()
                // Allow all requests by logged-in users.
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                // Configure the login page.
                .and()
                // .oauth2Login(oauth2Login -> oauth2Login.loginPage(LOGIN_URL))
                // .oauth2Client(withDefaults())
                // .logout()
                // .deleteCookies("remove")
                // .invalidateHttpSession(true)
                // .logoutSuccessUrl(LOGOUT_SUCCESS_URL);

                // Configure logout
                .logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                // Configure the login page.
                .and()
                .oauth2Login()
                .loginPage(LOGIN_URL)
                .permitAll();

        // http.
        //         .loginProcessingUrl(LOGIN_PROCESSING_URL)
        //     .failureUrl(LOGIN_FAILURE_URL)
        //     .defaultSuccessUrl(LOGIN_SUCCESS_URL, true)
        //     // Configure logout
        //     .and()
        //     .logout()
        //     .deleteCookies("remove")
        //     .invalidateHttpSession(true)
        //     .logoutSuccessUrl(LOGOUT_SUCCESS_URL);

        // .oauth2Login(oauth2Login ->
        //         oauth2Login.loginPage("/oauth2/authorization/messaging-client-oidc"))
        //         .oauth2Client(withDefaults())

        // http.csrf()
        //     .disable()
        //     // Register our CustomRequestCache, that saves unauthorized access attempts, so
        //     // the user is redirected after login.
        //     .requestCache()
        //     .requestCache(new CustomRequestCache())
        //     // Restrict access to our application.
        //     .and()
        //     .authorizeRequests()
        //     // Allow all flow internal requests.
        //     .requestMatchers(SecurityUtils::isFrameworkInternalRequest)
        //     .permitAll()
        //     .anyRequest()
        //     .authenticated()
        //     .and()
        //     .oauth2Login(oauth2Login -> oauth2Login.loginPage(
        //             "/oauth2/authorization/user"))
        //     .oauth2Client(withDefaults());
    }

    // @Bean
    // public DaoAuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    //     auth.setUserDetailsService(userDetailsService);
    //     auth.setPasswordEncoder(new BCryptPasswordEncoder());
    //     return auth;
    // }
    //
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.authenticationProvider(authenticationProvider());
    // }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        // Vaadin Flow static resources
                        "/VAADIN/**",
                        // the standard favicon URI
                        "/favicon.ico",
                        // the robot's exclusion standard
                        "/robots.txt",
                        // web application manifest
                        "/manifest.webmanifest", "/sw.js", "/offline-page.html",
                        // icons and images
                        "/icons/**", "/images/**",
                        // (development mode) static resources
                        "/frontend/**",
                        // (development mode) webjars
                        "/webjars/**",
                        // (development mode) H2 debugging console
                        "/h2-console/**",
                        // (production mode) static resources
                        "/frontend-es5/**", "/frontend-es6/**");
    }
}
