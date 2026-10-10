package com.eventhub.api_gateway.Filter;

import com.eventhub.api_gateway.Util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Paths that don't need a token at all
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/signup",
            "/auth/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("Gateway request path: " + path);
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing or malformed Authorization header\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateAndExtractClaims(token);

            // Inject validated identity as headers for downstream services.
            // Booking Service should now read X-User-Id from the header,
            // not trust a userId typed into the request body.
            HeaderInjectingWrapper wrappedRequest = new HeaderInjectingWrapper(request);
            wrappedRequest.putHeader("X-User-Id", String.valueOf(claims.get("userId")));
            wrappedRequest.putHeader("X-User-Role", String.valueOf(claims.get("role")));

            filterChain.doFilter(wrappedRequest, response);

        } catch (Exception ex) {
        logger.error("JWT validation failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
    }
    }
}