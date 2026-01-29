package com.example.sysdevhqapp1backend.challenge;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイピングチャレンジ作成リクエストDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCreateRequest {

  @NotBlank(message = "プログラミング言語は必須です")
  private String language;

  @NotBlank(message = "コードスニペットは必須です")
  private String codeSnippet;

  @NotNull(message = "文字数は必須です")
  @Min(value = 1, message = "文字数は1以上である必要があります")
  private Integer characterCount;

  private String difficulty;
}
