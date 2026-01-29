package com.example.sysdevhqapp1backend.score;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * スコア作成リクエストDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreCreateRequest {

  @NotNull(message = "チャレンジIDは必須です")
  private Long challengeId;

  @NotNull(message = "WPMは必須です")
  @DecimalMin(value = "0.0", message = "WPMは0以上である必要があります")
  private BigDecimal wpm;

  @NotNull(message = "正確性は必須です")
  @DecimalMin(value = "0.0", message = "正確性は0以上である必要があります")
  @DecimalMax(value = "100.0", message = "正確性は100以下である必要があります")
  private BigDecimal accuracy;

  @NotNull(message = "所要時間は必須です")
  private Integer timeTakenSeconds;
}
