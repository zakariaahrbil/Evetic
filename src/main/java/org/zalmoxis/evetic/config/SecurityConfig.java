package org.zalmoxis.evetic.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.zalmoxis.evetic.filters.UserFilter;

@Configuration
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserFilter filter, UserFilter userFilter) throws Exception{

        http.
                authorizeHttpRequests(authorize ->
                        // Allow everybody
                        authorize.
                                anyRequest().
                                authenticated()
                ).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                ).addFilterAfter(userFilter, BearerTokenAuthenticationFilter.class);

        return http.build();

    }
}
