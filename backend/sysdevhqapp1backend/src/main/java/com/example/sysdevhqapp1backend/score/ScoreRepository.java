package com.example.sysdevhqapp1backend.score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * スコアリポジトリインターフェース
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

  /**
   * ユーザーIDでスコアを検索（新しい順）
   *
   * @param userId ユーザーID
   * @param pageable ページング情報
   * @return スコアのページ
   */
  Page<Score> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

  /**
   * 全ユーザーのスコアをWPMの高い順に取得（ランキング用）
   *
   * @param pageable ページング情報
   * @return スコアのページ
   */
  Page<Score> findAllByOrderByWpmDesc(Pageable pageable);

  /**
   * ユーザーIDでスコアを検索
   *
   * @param userId ユーザーID
   * @return スコアのリスト
   */
  List<Score> findByUserId(Long userId);

  /**
   * チャレンジIDでスコアを検索
   *
   * @param challengeId チャレンジID
   * @return スコアのリスト
   */
  List<Score> findByChallengeId(Long challengeId);

  /**
   * 上位N件のスコアを取得（ランキング用）
   *
   * @param limit 取得件数
   * @return スコアのリスト
   */
  @Query(value = "SELECT * FROM scores ORDER BY wpm DESC LIMIT ?1", nativeQuery = true)
  List<Score> findTopScores(int limit);
}
