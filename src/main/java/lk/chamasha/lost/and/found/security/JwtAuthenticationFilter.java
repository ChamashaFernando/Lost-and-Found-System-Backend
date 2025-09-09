package lk.chamasha.lost.and.found.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        if (isPublicEndpoint(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        Map<String, Object> claims = jwtService.getAllClaimsByToken(jwt);
        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Extract roles
        List<String> roles = Collections.emptyList();
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            roles = ((List<?>) rolesObj).stream()
                    .filter(obj -> obj instanceof String)
                    .map(obj -> ((String) obj).toUpperCase()) // Uppercase for @RolesAllowed match
                    .collect(Collectors.toList());
        }

        // ✅ Debug log (Optional)
        System.out.println("✅ JWT Roles: " + roles);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // ✅ Extract username (from "sub")
        Object usernameObj = claims.get("sub");
        if (usernameObj == null) {
            usernameObj = claims.get("username");
        }

        if (usernameObj == null) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = new User(usernameObj.toString(), "", authorities);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        return (method.equalsIgnoreCase("POST") && (path.equals("/api/users/register") || path.equals("/api/users/login")))
                || (method.equalsIgnoreCase("GET") && (
                path.startsWith("/api/lost") ||
                        path.startsWith("/api/found")
        ));
    }
}
