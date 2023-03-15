package ru.antelit.fiskabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessEventPublishingLogoutHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.setUsersByUsernameQuery("select username, password, enabled from security.users where username=?");
        manager.setAuthoritiesByUsernameQuery("select username, authority from security.authorities where username=?");
        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .antMatcher("/register")
//                .antMatcher("/webjars/**")
//                .antMatcher("/perform_login")
//                .anonymous()
//                .and()
                .authorizeRequests()
                .antMatchers("/home", "/")
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/log")
                .successForwardUrl("/home")
                .loginProcessingUrl("/auth")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/log");
        return http.build();
    }
}
