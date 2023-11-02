package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/index.html" , "/web/assets/images/**" , "/web/assets/pages/login.html" ,
                        "/web/assets/js/index.js" , "/web/assets/js/login.js" , "/web/assets/style/style.css").permitAll()

                .antMatchers(HttpMethod.POST, "/api/clients" , "/api/login").permitAll()

                .antMatchers("/administrator/**" , "/h2-console/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/rest/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")

                .antMatchers("/web/**").hasAuthority("CLIENT")

                .antMatchers("/api/clients/current").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts" , "/api/clients/current/cards" , "/api/clients/current/transfers").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.GET, "/api/clients/current" , "/api/accounts/{id}" , "/api/clients/current/accounts").hasAuthority("CLIENT")


        .anyRequest().denyAll();


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.logout()
                .logoutUrl("/api/logout");

        http.csrf()
                .disable();

        http.headers()
                .frameOptions().disable();

        http.exceptionHandling()
                .authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin()
                .successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        http.formLogin()
                .failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
