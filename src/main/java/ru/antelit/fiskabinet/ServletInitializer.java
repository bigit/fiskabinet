package ru.antelit.fiskabinet;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.core.userdetails.UserDetails;

public class ServletInitializer extends SpringBootServletInitializer {

    UserDetails details;
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FiskabinetApplication.class);
    }

}
