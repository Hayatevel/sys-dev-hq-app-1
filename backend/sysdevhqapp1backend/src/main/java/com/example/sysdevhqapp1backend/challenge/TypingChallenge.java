package com.example.sysdevhqapp1backend.challenge;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイピングチャレンジエンティティクラス
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Entity
@Table(name = "typing_challenges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingChallenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String language;

  @Column(name = "code_snippet", nullable = false, columnDefinition = "TEXT")
  private String codeSnippet;

  @Column(name = "character_count", nullable = false)
  private Integer characterCount;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ChallengeDifficulty difficulty = ChallengeDifficulty.MEDIUM;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * チャレンジ難易度
   */
  public enum ChallengeDifficulty {
    EASY, MEDIUM, HARD
  }
}
