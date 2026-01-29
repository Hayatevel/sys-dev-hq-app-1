package com.example.sysdevhqapp1backend.challenge;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * タイピングチャレンジコントローラー チャレンジの取得エンドポイントを提供
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

  @Autowired
  private ChallengeService challengeService;

  /**
   * チャレンジの総数を取得
   *
   * @return チャレンジの総数
   */
  @GetMapping("/count")
  public ResponseEntity<?> getChallengeCount() {
    long count = challengeService.getChallengeCount();
    return ResponseEntity.ok(java.util.Map.of("count", count));
  }

  /**
   * 全てのチャレンジを取得
   *
   * @return チャレンジレスポンスのリスト
   */
  @GetMapping
  public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
    List<ChallengeResponse> challenges = challengeService.getAllChallenges();
    return ResponseEntity.ok(challenges);
  }

  /**
   * IDでチャレンジを取得
   *
   * @param id チャレンジID
   * @return チャレンジレスポンス
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getChallengeById(@PathVariable Long id) {
    try {
      ChallengeResponse challenge = challengeService.getChallengeById(id);
      return ResponseEntity.ok(challenge);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.status(404).body(java.util.Map.of("error", e.getMessage()));
    }
  }

  /**
   * ランダムにチャレンジを取得
   *
   * @return チャレンジレスポンス
   */
  @GetMapping("/random")
  public ResponseEntity<?> getRandomChallenge(@RequestParam(required = false) String language) {
    try {
      ChallengeResponse challenge;
      if (language != null && !language.isEmpty()) {
        challenge = challengeService.getRandomChallengeByLanguage(language);
      } else {
        challenge = challengeService.getRandomChallenge();
      }
      return ResponseEntity.ok(challenge);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.status(404).body(java.util.Map.of("error", e.getMessage()));
    }
  }
}
