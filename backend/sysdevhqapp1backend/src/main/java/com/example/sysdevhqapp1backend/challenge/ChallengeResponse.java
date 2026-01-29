package com.example.sysdevhqapp1backend.challenge;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイピングチャレンジレスポンスDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

  private Long id;
  private String language;
  private String codeSnippet;
  private Integer characterCount;
  private String difficulty;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
