package application.todoapp.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final static String SIGNING_KEY="546A576E5A7234753778214125432A462D4A614E645267556B58703273357638";
    private final Long TOKEN_EXPIRATION_TIME = Integer.toUnsignedLong(24*60*60*1000);
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    public Date extractExpirationDate(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claim_resolver){
        final Claims claims = extractAllClaims(token);
        return claim_resolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(Map<String, Object> extra_claims, UserDetails user_details){
        return Jwts.builder().setClaims(extra_claims).setSubject(user_details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRATION_TIME))
                .signWith( SignatureAlgorithm.HS256,getSignInKey()).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username  = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] key_bytes= Base64.getDecoder().decode(SIGNING_KEY);
        return Keys.hmacShaKeyFor(key_bytes);
    }


}
