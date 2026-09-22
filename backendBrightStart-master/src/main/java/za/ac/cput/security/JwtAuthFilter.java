package za.ac.cput.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtTokenUtil;

    public JwtAuthFilter(JwtUtils jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            String email = jwtTokenUtil.extractEmail(token);

            if (email != null && jwtTokenUtil.validateToken(token, email)) {
                String role = jwtTokenUtil.extractRole(token);

                List<SimpleGrantedAuthority> authorities;
                if ("ADMIN".equals(role)) {
                    authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
                } else {
                    authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                new User(email, "", authorities),
                                null,
                                authorities
                        );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            System.err.println("JWT Filter error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
