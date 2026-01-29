package com.example.sysdevhqapp1backend.score;

import com.example.sysdevhqapp1backend.user.User;
import com.example.sysdevhqapp1backend.challenge.TypingChallenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * スコアエンティティクラス
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Entity
@Table(name = "scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "challenge_id", nullable = false)
  private TypingChallenge challenge;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal wpm;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal accuracy;

  @Column(name = "time_taken_seconds", nullable = false)
  private Integer timeTakenSeconds;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
