package com.example.springzuul;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.tokenExtractor(new GrafanaCookieTokenExtractor());
    }


    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests().
                antMatchers("/authorization/oauth/**").
                permitAll().
                antMatchers("/authorization/status").
                authenticated().
                antMatchers("/blog/**").permitAll().
                antMatchers("/grafana/public/**").permitAll().
//                antMatchers("/grafana/**").authenticated();
                antMatchers("/grafana/**").permitAll();
    }

}