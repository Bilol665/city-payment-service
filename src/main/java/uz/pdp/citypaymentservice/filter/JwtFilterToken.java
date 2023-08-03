package uz.pdp.citypaymentservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.citypaymentservice.service.jwt.JwtService;
import uz.pdp.citypaymentservice.service.user.AuthenticationService;

import java.io.IOException;

@AllArgsConstructor
public class JwtFilterToken extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        token = token.substring(7);
        Jws<Claims> claimsJws = jwtService.extractToken(token);
        authenticationService.Authenticate(claimsJws.getBody(),request);

        filterChain.doFilter(request,response);
    }
}
