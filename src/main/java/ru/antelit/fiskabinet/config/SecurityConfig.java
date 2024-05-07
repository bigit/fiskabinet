package ru.antelit.fiskabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.RequestedUrlRedirectInvalidSessionStrategy;
import ru.antelit.fiskabinet.service.UserInfoService;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
//@Profile("dev")
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcUserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.setAuthoritiesByUsernameQuery("select username, authority from security.authorities where username=?");
        manager.setCreateUserSql("insert into security.user " +
                "(username, password, first_name, father_name, last_name, phonenumber, email, enabled)" +
                "values (?, ?, ?, ?, ?, ?, ?, true)");
        return manager;
    }

    @Autowired
    public UserInfoService userInfoService;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userInfoService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .successHandler(successHandler())
                        .permitAll())
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll())
                .sessionManagement(mgmt -> mgmt.invalidSessionStrategy(new RequestedUrlRedirectInvalidSessionStrategy()))
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers("/register", "/css/**","/error/**", "/webjars/**", "/img/**")
                        .permitAll()
                        .requestMatchers(
                                "/home",
                                "/org/**",
                                "/code/**",
                                "/kkm/**",
                                "/application/**",
                                "/files/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/manager/**", "/import/**")
                        .hasRole("ADMIN")
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
//            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                request.getRequestDispatcher("/manager").forward(request, response);
//            } else {
//                request.getRequestDispatcher("/home").forward(request, response);
//            }
        };
    }

//    @Autowired
//    public void configureJdbcAuth(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select")
//    }

}
