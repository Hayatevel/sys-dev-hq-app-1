package com.example.sysdevhqapp1backend.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインリクエストDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "ユーザー名は必須です")
  private String username;

  @NotBlank(message = "パスワードは必須です")
  private String password;
}
