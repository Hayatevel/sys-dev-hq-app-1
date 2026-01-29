package com.example.sysdevhqapp1backend.score;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ランキングコントローラー スコアのランキング取得エンドポイントを提供
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/rankings")
public class RankingController {

  @Autowired
  private ScoreService scoreService;

  /**
   * 上位スコアを取得
   *
   * @param limit 取得件数（デフォルト: 10）
   * @return スコアレスポンスのリスト
   */
  @GetMapping
  public ResponseEntity<List<ScoreResponse>> getTopScores(
      @RequestParam(defaultValue = "10") int limit) {
    List<ScoreResponse> scores = scoreService.getTopScores(limit);
    return ResponseEntity.ok(scores);
  }
}
