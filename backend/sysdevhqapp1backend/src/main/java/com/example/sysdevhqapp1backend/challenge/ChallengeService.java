package com.example.sysdevhqapp1backend.challenge;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タイピングチャレンジサービスクラス
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Service
public class ChallengeService {

  private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);

  @Autowired
  private TypingChallengeRepository challengeRepository;

  /**
   * チャレンジの総数を取得
   *
   * @return チャレンジの総数
   */
  public long getChallengeCount() {
    return challengeRepository.count();
  }

  /**
   * 全てのチャレンジを取得
   *
   * @return チャレンジレスポンスのリスト
   */
  public List<ChallengeResponse> getAllChallenges() {
    return challengeRepository.findAll().stream().map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  /**
   * IDでチャレンジを取得
   *
   * @param id チャレンジID
   * @return チャレンジレスポンス
   * @throws RuntimeException チャレンジが見つからない場合
   */
  public ChallengeResponse getChallengeById(Long id) {
    TypingChallenge challenge =
        challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("チャレンジが見つかりません"));
    return convertToResponse(challenge);
  }

  /**
   * ランダムにチャレンジを取得
   *
   * @return チャレンジレスポンス
   * @throws RuntimeException チャレンジが見つからない場合
   */
  public ChallengeResponse getRandomChallenge() {
    logger.info("Fetching random challenge from database...");
    long count = challengeRepository.count();
    logger.info("Total challenges in database: {}", count);

    TypingChallenge challenge = challengeRepository.findRandomChallenge().orElseThrow(() -> {
      logger.error("No challenges found in database!");
      return new RuntimeException("チャレンジが見つかりません");
    });
    logger.info("Found challenge: id={}, language={}", challenge.getId(), challenge.getLanguage());
    return convertToResponse(challenge);
  }

  /**
   * 言語を指定してランダムにチャレンジを取得
   *
   * @param language プログラミング言語
   * @return チャレンジレスポンス
   * @throws RuntimeException チャレンジが見つからない場合
   */
  public ChallengeResponse getRandomChallengeByLanguage(String language) {
    TypingChallenge challenge = challengeRepository.findRandomChallengeByLanguage(language)
        .orElseThrow(() -> new RuntimeException("指定された言語のチャレンジが見つかりません"));
    return convertToResponse(challenge);
  }

  /**
   * チャレンジを作成
   *
   * @param request チャレンジ作成リクエスト
   * @return チャレンジレスポンス
   */
  @Transactional
  public ChallengeResponse createChallenge(ChallengeCreateRequest request) {
    TypingChallenge challenge = new TypingChallenge();
    challenge.setLanguage(request.getLanguage());
    challenge.setCodeSnippet(request.getCodeSnippet());
    challenge.setCharacterCount(request.getCharacterCount());

    if (request.getDifficulty() != null) {
      challenge.setDifficulty(TypingChallenge.ChallengeDifficulty.valueOf(request.getDifficulty()));
    }

    TypingChallenge savedChallenge = challengeRepository.save(challenge);
    return convertToResponse(savedChallenge);
  }

  /**
   * チャレンジを更新
   *
   * @param id チャレンジID
   * @param request チャレンジ更新リクエスト
   * @return チャレンジレスポンス
   * @throws RuntimeException チャレンジが見つからない場合
   */
  @Transactional
  public ChallengeResponse updateChallenge(Long id, ChallengeCreateRequest request) {
    TypingChallenge challenge =
        challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("チャレンジが見つかりません"));

    challenge.setLanguage(request.getLanguage());
    challenge.setCodeSnippet(request.getCodeSnippet());
    challenge.setCharacterCount(request.getCharacterCount());

    if (request.getDifficulty() != null) {
      challenge.setDifficulty(TypingChallenge.ChallengeDifficulty.valueOf(request.getDifficulty()));
    }

    TypingChallenge updatedChallenge = challengeRepository.save(challenge);
    return convertToResponse(updatedChallenge);
  }

  /**
   * チャレンジを削除
   *
   * @param id チャレンジID
   * @throws RuntimeException チャレンジが見つからない場合
   */
  @Transactional
  public void deleteChallenge(Long id) {
    if (!challengeRepository.existsById(id)) {
      throw new RuntimeException("チャレンジが見つかりません");
    }
    challengeRepository.deleteById(id);
  }

  /**
   * フィルタリング条件でチャレンジを検索
   *
   * @param language プログラミング言語（nullまたは空の場合は全言語）
   * @param difficulty 難易度（nullまたは空の場合は全難易度）
   * @param keyword コードスニペット内の検索キーワード（nullまたは空の場合は検索しない）
   * @return チャレンジレスポンスのリスト
   */
  public List<ChallengeResponse> searchChallenges(String language, String difficulty,
      String keyword) {
    // パラメータの正規化
    String normalizedLanguage =
        (language != null && !language.trim().isEmpty()) ? language.trim() : null;
    TypingChallenge.ChallengeDifficulty normalizedDifficulty = null;
    if (difficulty != null && !difficulty.trim().isEmpty()) {
      try {
        normalizedDifficulty = TypingChallenge.ChallengeDifficulty.valueOf(difficulty.trim());
      } catch (IllegalArgumentException e) {
        logger.warn("Invalid difficulty value: {}", difficulty);
      }
    }
    String normalizedKeyword =
        (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

    // フィルタリング実行
    List<TypingChallenge> challenges = challengeRepository.findByFilters(normalizedLanguage,
        normalizedDifficulty, normalizedKeyword);

    logger.info("Search results: language={}, difficulty={}, keyword={}, count={}",
        normalizedLanguage, normalizedDifficulty, normalizedKeyword, challenges.size());

    return challenges.stream().map(this::convertToResponse).collect(Collectors.toList());
  }

  /**
   * エンティティをレスポンスDTOに変換
   *
   * @param challenge タイピングチャレンジエンティティ
   * @return チャレンジレスポンス
   */
  private ChallengeResponse convertToResponse(TypingChallenge challenge) {
    return new ChallengeResponse(challenge.getId(), challenge.getLanguage(),
        challenge.getCodeSnippet(), challenge.getCharacterCount(), challenge.getDifficulty().name(),
        challenge.getCreatedAt(), challenge.getUpdatedAt());
  }
}
