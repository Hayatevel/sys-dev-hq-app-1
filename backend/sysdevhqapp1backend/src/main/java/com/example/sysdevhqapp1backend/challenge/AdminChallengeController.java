package com.example.sysdevhqapp1backend.challenge;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

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
  public ResponseEntity<List<ChallengeResponse>> getAllChallenges(
      @RequestParam(required = false) String language,
      @RequestParam(required = false) String difficulty,
      @RequestParam(required = false) String keyword) {

    // フィルタリングパラメータが1つでも指定されている場合は検索を実行
    if ((language != null && !language.isEmpty()) || (difficulty != null && !difficulty.isEmpty())
        || (keyword != null && !keyword.isEmpty())) {
      List<ChallengeResponse> challenges =
          challengeService.searchChallenges(language, difficulty, keyword);
      return ResponseEntity.ok(challenges);
    }

    // パラメータなしの場合は全件取得
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
