package lk.chamasha.lost.and.found.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String SECRET_KEY = "kljk455njk45fvgbjme7899bkmvvhlkffofmm239bjhnkjm7922bhbdew3i8094njnnklvf";

    /** ✅ Extract username from token */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** ✅ Extract language from token */
    public String extractLanguage(String token) {
        return (String) extractAllClaims(token).get("language");
    }

    /** ✅ Generic claim extractor */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    /** ✅ Validate token */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /** ✅ Get all claims as Map */
    public Map<String, Object> getAllClaimsByToken(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> claimMap = new HashMap<>();
        claims.forEach(claimMap::put);
        return claimMap;
    }

    /** ✅ Check if token is expired */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** ✅ Parse token and return claims */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** ✅ Generate token with extra claims (roles, language) */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        // Roles in uppercase for @RolesAllowed match
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", "").toUpperCase())
                .collect(Collectors.toList());

        extraClaims.put("roles", roles);
        extraClaims.putIfAbsent("language", "ENGLISH");

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 50)) // 50 years
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
