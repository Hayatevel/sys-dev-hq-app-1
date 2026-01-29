package com.example.sysdevhqapp1backend.challenge;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理者用チャレンジコントローラー チャレンジの作成、更新、削除エンドポイントを提供（管理者専用）
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@RestController
@RequestMapping("/api/admin/challenges")
@PreAuthorize("hasRole('ADMIN')")
public class AdminChallengeController {

  @Autowired
  private ChallengeService challengeService;

  /**
   * 全てのチャレンジを取得（管理者用）
   *
   * @return チャレンジレスポンスのリスト
   */
  @GetMapping
  public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
    List<ChallengeResponse> challenges = challengeService.getAllChallenges();
    return ResponseEntity.ok(challenges);
  }

  /**
   * チャレンジを作成
   *
   * @param request チャレンジ作成リクエスト
   * @return チャレンジレスポンス
   */
  @PostMapping
  public ResponseEntity<?> createChallenge(@Valid @RequestBody ChallengeCreateRequest request) {
    try {
      ChallengeResponse challenge = challengeService.createChallenge(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(challenge);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  /**
   * チャレンジを更新
   *
   * @param id チャレンジID
   * @param request チャレンジ更新リクエスト
   * @return チャレンジレスポンス
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> updateChallenge(@PathVariable Long id,
      @Valid @RequestBody ChallengeCreateRequest request) {
    try {
      ChallengeResponse challenge = challengeService.updateChallenge(id, request);
      return ResponseEntity.ok(challenge);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * チャレンジを削除
   *
   * @param id チャレンジID
   * @return レスポンス
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteChallenge(@PathVariable Long id) {
    try {
      challengeService.deleteChallenge(id);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
