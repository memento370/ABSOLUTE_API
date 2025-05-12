package com.example.absoluteweb.config;

import com.example.absoluteweb.forum.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Генерація секретного ключа для підпису. Зазвичай зберігається у безпечному місці (Vault, налаштуваннях тощо).
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Key keyForum = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username, String role) {
        // Приклад: токен з `sub` = username, додатковим "role" і строком дії 1 година
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 год
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // Наприклад, прострочений токен чи невірний підпис
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }

    public String generateTokenForum(User user) {
        return Jwts.builder()
                .setSubject(user.getNick())
                .claim("status", user.getStatus())
                .claim("role", user.getRole())
                .claim("title", user.getTitle())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 год
                .signWith(keyForum)
                .compact();
    }

    public boolean validateTokenForum(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyForum).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    public String getNickForumFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyForum).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTitleForumFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyForum).build()
                .parseClaimsJws(token).getBody();
        return claims.get("title", String.class);
    }
    public String getStatusForumFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyForum).build()
                .parseClaimsJws(token).getBody();
        return claims.get("status", String.class);
    }

    public String getRoleForumFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyForum).build()
                .parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }
}
