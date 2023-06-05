package ru.antelit.fiskabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.antelit.fiskabinet.service.UserInfoService;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
//@Profile("dev")
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcUserDetailsManager userDetailsService() {
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
//
//    @Autowired
//    public void authManager(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth")
                .successForwardUrl("/home")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/register", "/error/**", "/webjars/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/home", "/org/**")
                .hasAuthority("READ");
        return http.build();
    }

//    @Autowired
//    public void configureJdbcAuth(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select")
//    }

}
