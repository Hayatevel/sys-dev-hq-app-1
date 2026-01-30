package com.example.sysdevhqapp1backend.challenge;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  /**
   * 言語と難易度でフィルタリング（両方が指定された場合）
   *
   * @param language プログラミング言語
   * @param difficulty 難易度
   * @return タイピングチャレンジのリスト
   */
  List<TypingChallenge> findByLanguageAndDifficulty(String language,
      TypingChallenge.ChallengeDifficulty difficulty);

  /**
   * コードスニペット内を検索（部分一致）
   *
   * @param keyword 検索キーワード
   * @return タイピングチャレンジのリスト
   */
  List<TypingChallenge> findByCodeSnippetContaining(String keyword);

  /**
   * 言語、難易度、コードスニペット検索を組み合わせたフィルタリング
   *
   * @param language プログラミング言語（nullの場合はフィルタリングしない）
   * @param difficulty 難易度（nullの場合はフィルタリングしない）
   * @param keyword 検索キーワード（nullまたは空の場合は検索しない）
   * @return タイピングチャレンジのリスト
   */
  @Query("SELECT c FROM TypingChallenge c WHERE "
      + "(:language IS NULL OR c.language = :language) AND "
      + "(:difficulty IS NULL OR c.difficulty = :difficulty) AND "
      + "(:keyword IS NULL OR :keyword = '' OR c.codeSnippet LIKE %:keyword%)")
  List<TypingChallenge> findByFilters(@Param("language") String language,
      @Param("difficulty") TypingChallenge.ChallengeDifficulty difficulty,
      @Param("keyword") String keyword);
}
