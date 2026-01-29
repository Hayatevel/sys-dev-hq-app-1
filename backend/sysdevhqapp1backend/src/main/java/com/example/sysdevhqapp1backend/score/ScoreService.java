package com.example.sysdevhqapp1backend.score;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sysdevhqapp1backend.challenge.TypingChallenge;
import com.example.sysdevhqapp1backend.challenge.TypingChallengeRepository;
import com.example.sysdevhqapp1backend.user.User;
import com.example.sysdevhqapp1backend.user.UserRepository;

/**
 * スコアサービスクラス
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Service
public class ScoreService {

  @Autowired
  private ScoreRepository scoreRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TypingChallengeRepository challengeRepository;

  /**
   * スコアを作成
   *
   * @param username ユーザー名
   * @param request スコア作成リクエスト
   * @return スコアレスポンス
   * @throws RuntimeException ユーザーまたはチャレンジが見つからない場合
   */
  @Transactional
  public ScoreResponse createScore(String username, ScoreCreateRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

    TypingChallenge challenge = challengeRepository.findById(request.getChallengeId())
        .orElseThrow(() -> new RuntimeException("チャレンジが見つかりません"));

    Score score = new Score();
    score.setUser(user);
    score.setChallenge(challenge);
    score.setWpm(request.getWpm());
    score.setAccuracy(request.getAccuracy());
    score.setTimeTakenSeconds(request.getTimeTakenSeconds());

    Score savedScore = scoreRepository.save(score);
    return convertToResponse(savedScore);
  }

  /**
   * ユーザーのスコア履歴を取得
   *
   * @param username ユーザー名
   * @param page ページ番号
   * @param size ページサイズ
   * @return スコアレスポンスのリスト
   * @throws RuntimeException ユーザーが見つからない場合
   */
  public List<ScoreResponse> getUserScores(String username, int page, int size) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

    Pageable pageable = PageRequest.of(page, size);
    Page<Score> scores = scoreRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

    return scores.stream().map(this::convertToResponse).collect(Collectors.toList());
  }

  /**
   * ランキングを取得（上位スコア）
   *
   * @param limit 取得件数
   * @return スコアレスポンスのリスト
   */
  public List<ScoreResponse> getTopScores(int limit) {
    List<Score> scores = scoreRepository.findTopScores(limit);
    return scores.stream().map(this::convertToResponse).collect(Collectors.toList());
  }

  /**
   * エンティティをレスポンスDTOに変換
   *
   * @param score スコアエンティティ
   * @return スコアレスポンス
   */
  private ScoreResponse convertToResponse(Score score) {
    return new ScoreResponse(score.getId(), score.getUser().getId(), score.getUser().getUsername(),
        score.getChallenge().getId(), score.getChallenge().getLanguage(), score.getWpm(),
        score.getAccuracy(), score.getTimeTakenSeconds(), score.getCreatedAt());
  }
}
