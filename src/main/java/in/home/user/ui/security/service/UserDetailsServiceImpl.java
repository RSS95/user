// package in.home.user.ui.security.service;
//
// import in.home.user.api.model.Role;
// import in.home.user.api.model.User;
// import in.home.user.ui.feign.client.UserClient;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;
//
// import java.util.Collection;
// import java.util.stream.Collectors;
//
// @Service
// public class UserDetailsServiceImpl implements UserDetailsService {
//
//     private final UserClient userClient;
//
//     public UserDetailsServiceImpl(UserClient userClient) {
//         super();
//         this.userClient = userClient;
//     }
//
//     public User save(User user) {
//         user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//         return userClient.save(user);
//     }
//
//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//         User user = userClient.loadUserByUsernameWithPassword(username);
//         if (user == null) {
//             throw new UsernameNotFoundException("Invalid username or password.");
//         }
//         return new org.springframework.security.core.userdetails.User(user.getUserName(),
//                 user.getPassword(),
//                 mapRolesToAuthorities(user.getRoleSet()));
//     }
//
//     private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
//         return roles.stream()
//                 .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
//                 .collect(Collectors.toList());
//     }
// }
