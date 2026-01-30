package com.example.sysdevhqapp1backend.auth;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * デバッグ用コントローラー パスワードハッシュの検証やユーザー情報の確認用（開発環境のみで使用） 本番環境では無効化されます（@Profile("!prod")）
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/debug")
@Profile("!prod")
public class DebugController {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * BCryptハッシュとパスワードの一致を確認
   *
   * @param request ハッシュとパスワードを含むリクエスト
   * @return 検証結果
   */
  @PostMapping("/verify-password")
  public ResponseEntity<Map<String, Object>> verifyPassword(
      @RequestBody Map<String, String> request) {
    String hash = request.get("hash");
    String password = request.get("password");

    Map<String, Object> response = new HashMap<>();
    response.put("hash", hash);
    response.put("password", password);
    response.put("matches", passwordEncoder.matches(password, hash));

    return ResponseEntity.ok(response);
  }

  /**
   * パスワードのBCryptハッシュを生成
   *
   * @param request パスワードを含むリクエスト
   * @return 生成されたハッシュ
   */
  @PostMapping("/generate-hash")
  public ResponseEntity<Map<String, String>> generateHash(
      @RequestBody Map<String, String> request) {
    String password = request.get("password");
    String hash = passwordEncoder.encode(password);

    Map<String, String> response = new HashMap<>();
    response.put("password", password);
    response.put("hash", hash);

    return ResponseEntity.ok(response);
  }
}
