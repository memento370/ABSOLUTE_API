package com.example.absoluteweb.config;

import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key key;
    private final Key keyForum;

    public JwtUtils(
            @Value("${jwt.secret}") String secretBase64,
            @Value("${jwt.secret.forum}") String secretForumBase64
    ) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretBase64));
        this.keyForum = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretForumBase64));
    }

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

    public String getAccountLoginFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getAccountRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }

    public String generateTokenForum(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("status", user.getStatus())
                .claim("role", user.getRole())
                .claim("title", user.getTitle())
                .claim("nick", user.getNick())
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

    public boolean validateTokenForum(UserDTO user) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(keyForum)
                    .build()
                    .parseClaimsJws(user.getToken())
                    .getBody();

            // 1) Перевіряємо суб’єкт (ник)
            if (!user.getId().toString().equals(claims.getSubject())) {
                return false;
            }
            // 2) Перевіряємо інші кастомні поля
            if (!user.getStatus().equals(claims.get("status", String.class))) {
                return false;
            }
            if (!user.getRole().equals(claims.get("role", String.class))) {
                return false;
            }
            if (!user.getTitle().equals(claims.get("title", String.class))) {
                return false;
            }


            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // підпис, структура або access до claims невалідні
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

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyForum).build()
                .parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
