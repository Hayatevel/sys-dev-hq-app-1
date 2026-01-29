package com.example.sysdevhqapp1backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * サインアップリクエストDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

  @NotBlank(message = "ユーザー名は必須です")
  @Size(min = 3, max = 50, message = "ユーザー名は3文字以上50文字以下である必要があります")
  private String username;

  @NotBlank(message = "パスワードは必須です")
  @Size(min = 6, max = 100, message = "パスワードは6文字以上である必要があります")
  private String password;

  @NotBlank(message = "メールアドレスは必須です")
  @Email(message = "有効なメールアドレスを入力してください")
  private String email;
}
