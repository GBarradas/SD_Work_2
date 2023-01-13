package sd.rentRoom.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import sd.rentRoom.auth.security.RentRoomAuthorizationFilter;

@SpringBootApplication
public class RentRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentRoomApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().cors().disable()
                    .addFilterAfter(new RentRoomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/anuncios/listar**", "/anuncios**",
                            "/anuncios/{aid}","/msg/see**").permitAll()  // esta zona esta aberta para facilitar geracao de tokens - DEMONSTRACAO
                    .antMatchers(HttpMethod.POST, "/anuncios/user","/anuncios/novo","/msg/send","/user" ).permitAll()
                    //.antMatchers(HttpMethod.POST,"/gestao","/gestao/").access("hasRole('ADMIN')")
                    .anyRequest().authenticated();
        }
    }

}

