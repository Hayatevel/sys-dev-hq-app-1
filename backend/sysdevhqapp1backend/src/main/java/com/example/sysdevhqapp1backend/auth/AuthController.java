package com.example.sysdevhqapp1backend.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 認証コントローラー
 * サインアップとログインのエンドポイントを提供
 * 
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  /**
   * ユーザーサインアップ
   * 
   * @param request サインアップリクエスト
   * @return 認証レスポンス
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
    try {
      AuthResponse response = authService.signup(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(java.util.Map.of("error", e.getMessage()));
    }
  }

  /**
   * ユーザーログイン
   * 
   * @param request ログインリクエスト
   * @return 認証レスポンス
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    try {
      AuthResponse response = authService.login(request);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(java.util.Map.of("error", e.getMessage()));
    }
  }
}
