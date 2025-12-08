package org.zalmoxis.evetic.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.repositories.UserRepo;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFilter
        extends OncePerRequestFilter
{

    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof Jwt jwt)
        {
           UUID userId = UUID.fromString(jwt.getSubject());
           if(!userRepo.existsById(userId))
           {
               User newUser = new User();
                newUser.setId(userId);
                newUser.setUsername(jwt.getClaimAsString("preferred_username"));
                newUser.setEmail(jwt.getClaimAsString("email"));
                userRepo.save(newUser);
           }
           filterChain.doFilter(request, response);
        }

    }
}
