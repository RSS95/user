// package in.home.user.ui.configuration;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
//
// import static org.springframework.security.config.Customizer.withDefaults;
//
// @EnableWebSecurity
// public class SecurityConfig {
//
//     @Autowired
//     private UserDetailsService userDetailsService;
//
//     @Bean
//     WebSecurityCustomizer webSecurityCustomizer() {
//         return (web) -> web.ignoring().antMatchers("/webjars/**");
//     }
//
//     // @formatter:off
//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.authorizeRequests(authorizeRequests ->
//                     authorizeRequests.antMatchers("/oauth2/**").permitAll())
//             .authorizeRequests(authorizeRequests ->
//                     authorizeRequests.antMatchers("/login").authenticated())
//             // .authorizeRequests(authorizeRequests ->
//             //         authorizeRequests.antMatchers("/oauth2/authorize").permitAll())
//             .authorizeRequests(authorizeRequests ->
//                             authorizeRequests.anyRequest().permitAll())
//             .oauth2Login(oauth2Login ->
//                     oauth2Login.loginPage("/oauth2/authorization/user"))
//             .oauth2Client(withDefaults());
//         return http.build();
//     }
//     // @formatter:on
//
//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//         auth.setUserDetailsService(userDetailsService);
//         auth.setPasswordEncoder(new BCryptPasswordEncoder());
//         return auth;
//     }
//
//     // @Override
//     // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//     //     auth.authenticationProvider(authenticationProvider());
//     // }
// }
