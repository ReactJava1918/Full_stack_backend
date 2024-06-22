package com.codewithsiva.fullstackbackend.security;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codewithsiva.fullstackbackend.security.jwt.AuthTokenFilter;
import com.codewithsiva.fullstackbackend.security.jwt.JwtAuthEntryPoint;
import com.codewithsiva.fullstackbackend.security.user.RegUsersDetailsService;

import lombok.RequiredArgsConstructor;


/**
 * @author Simpson Alfred
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {
    private final RegUsersDetailsService regUsersDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter(){
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(regUsersDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /*
     * 
     * 

1. `@Bean`: This annotation tells Spring that the method annotated with it will return an object that should be managed by the Spring container as a bean.

2. `public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {`: This method is declared to return a `SecurityFilterChain` object and accepts an `HttpSecurity` object as a parameter. The `HttpSecurity` object is used to configure how Spring Security should behave.

3. `http.csrf(AbstractHttpConfigurer::disable)`: This line disables Cross-Site Request Forgery (CSRF) protection. CSRF protection is a security feature that helps prevent unauthorized commands being transmitted from a user that the web application trusts.

4. `.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))`: This line configures how Spring Security should handle exceptions related to authentication. Specifically, it sets an authentication entry point using the `jwtAuthEntryPoint` bean. An authentication entry point handles authentication failures and challenges the user to authenticate.

5. `.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`: This line configures the session management strategy for Spring Security. In this case, it sets the session creation policy to `STATELESS`, meaning Spring Security won't create an HTTP session and will rely entirely on tokens for authentication.

6. `.authorizeHttpRequests(auth -> { ... })`: This line starts the configuration of authorization rules for different types of HTTP requests.

7. `auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()`: This line permits access to static resources that Spring Security recognizes as common locations (e.g., CSS, JS files) without requiring authentication.

8. `.antMatchers("/auth/**").permitAll()`: This line permits access to any URL starting with "/auth/" without requiring authentication. This is often used for login or authentication-related endpoints.

9. `.antMatchers("/roles/**").hasRole("ADMIN")`: This line restricts access to URLs starting with "/roles/" to users who have the role "ADMIN". Users without this role will be denied access.

10. `.anyRequest().authenticated()`: This line specifies that any other request not matching the previous rules should be authenticated. In other words, users must be authenticated to access any other part of the application.

11. `http.authenticationProvider(authenticationProvider())`: This line sets the authentication provider for the application. The `authenticationProvider()` method returns a bean that defines how authentication should be handled.

12. `http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)`: This line adds a custom authentication token filter before the standard UsernamePasswordAuthenticationFilter. This filter will handle authentication tokens sent with requests.

13. `return http.build()`: Finally, this line builds the `HttpSecurity` object configured with the specified settings and returns it.

These configurations collectively define how Spring Security should handle authentication, authorization, session management, and exception handling for your application.
     * 
     * 
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 http.csrf(AbstractHttpConfigurer :: disable)
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .antMatchers("/auth/**").permitAll()
                        .antMatchers("/roles/**").hasRole("ADMIN")
                        .anyRequest().authenticated();
                });
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }







}
