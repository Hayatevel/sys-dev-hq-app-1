package com.example.sysdevhqapp1backend.score;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

/**
 * スコアコントローラー スコアの作成と取得エンドポイントを提供
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/scores")
public class ScoreController {

  @Autowired
  private ScoreService scoreService;

  /**
   * スコアを作成
   *
   * @param userDetails 認証済みユーザー情報
   * @param request スコア作成リクエスト
   * @return スコアレスポンス
   */
  @PostMapping
  public ResponseEntity<?> createScore(@AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody ScoreCreateRequest request) {
    try {
      ScoreResponse score = scoreService.createScore(userDetails.getUsername(), request);
      return ResponseEntity.status(HttpStatus.CREATED).body(score);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  /**
   * ユーザーのスコア履歴を取得
   *
   * @param userDetails 認証済みユーザー情報
   * @param page ページ番号
   * @param size ページサイズ
   * @return スコアレスポンスのリスト
   */
  @GetMapping("/my-scores")
  public ResponseEntity<?> getMyScores(@AuthenticationPrincipal UserDetails userDetails,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    try {
      List<ScoreResponse> scores =
          scoreService.getUserScores(userDetails.getUsername(), page, size);
      return ResponseEntity.ok(scores);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
