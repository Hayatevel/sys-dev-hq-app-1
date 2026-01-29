package com.example.sysdevhqapp1backend.challenge;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * タイピングチャレンジリポジトリインターフェース
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Repository
public interface TypingChallengeRepository extends JpaRepository<TypingChallenge, Long> {

  /**
   * プログラミング言語でフィルタリング
   *
   * @param language プログラミング言語
   * @return タイピングチャレンジのリスト
   */
  List<TypingChallenge> findByLanguage(String language);

  /**
   * 難易度でフィルタリング
   *
   * @param difficulty 難易度
   * @return タイピングチャレンジのリスト
   */
  List<TypingChallenge> findByDifficulty(TypingChallenge.ChallengeDifficulty difficulty);

  /**
   * ランダムにチャレンジを1つ取得
   *
   * @return タイピングチャレンジ（存在する場合）
   */
  @Query(value = "SELECT * FROM typing_challenges ORDER BY RAND() LIMIT 1", nativeQuery = true)
  Optional<TypingChallenge> findRandomChallenge();

  /**
   * 言語を指定してランダムにチャレンジを1つ取得
   *
   * @param language プログラミング言語
   * @return タイピングチャレンジ（存在する場合）
   */
  @Query(value = "SELECT * FROM typing_challenges WHERE language = ?1 ORDER BY RAND() LIMIT 1",
      nativeQuery = true)
  Optional<TypingChallenge> findRandomChallengeByLanguage(String language);
}
