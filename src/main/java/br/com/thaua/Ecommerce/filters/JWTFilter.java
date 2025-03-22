package br.com.thaua.Ecommerce.filters;

import br.com.thaua.Ecommerce.services.JWTService;
import br.com.thaua.Ecommerce.services.MyUserDetailsService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if(header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            email = jwtService.extractEmail(token);
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MyUserDetails myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(email);

            if(myUserDetails != null && jwtService.validateToken(token)) {
                Authentication authenticateUser = new UsernamePasswordAuthenticationToken(myUserDetails.getTypeUser(), myUserDetails.getPassword(), myUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticateUser);
            }
        }

        filterChain.doFilter(request, response);
    }
}
