package com.example.sysdevhqapp1backend.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWTユーティリティクラス JWTトークンの生成と検証を行う
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Component
public class JwtUtil {

  @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationAndValidation12345}")
  private String secret;

  @Value("${jwt.expiration:86400000}") // 24時間（ミリ秒）
  private Long expiration;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  /**
   * トークンからユーザー名を抽出
   *
   * @param token JWTトークン
   * @return ユーザー名
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * トークンから有効期限を抽出
   *
   * @param token JWTトークン
   * @return 有効期限
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * トークンからクレームを抽出
   *
   * @param token JWTトークン
   * @param claimsResolver クレームリゾルバ関数
   * @return 抽出されたクレーム
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  /**
   * トークンの有効期限切れを確認
   *
   * @param token JWTトークン
   * @return 有効期限切れの場合true
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * JWTトークンを生成
   *
   * @param userDetails ユーザー詳細情報
   * @return 生成されたJWTトークン
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  /**
   * JWTトークンを生成（クレーム付き）
   *
   * @param claims クレーム情報
   * @param subject サブジェクト（ユーザー名）
   * @return 生成されたJWTトークン
   */
  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  /**
   * トークンの妥当性を検証
   *
   * @param token JWTトークン
   * @param userDetails ユーザー詳細情報
   * @return トークンが有効な場合true
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
