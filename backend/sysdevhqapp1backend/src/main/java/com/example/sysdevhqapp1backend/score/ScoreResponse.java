package com.example.sysdevhqapp1backend.score;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * スコアレスポンスDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponse {

  private Long id;
  private Long userId;
  private String username;
  private Long challengeId;
  private String language;
  private BigDecimal wpm;
  private BigDecimal accuracy;
  private Integer timeTakenSeconds;
  private LocalDateTime createdAt;
}
