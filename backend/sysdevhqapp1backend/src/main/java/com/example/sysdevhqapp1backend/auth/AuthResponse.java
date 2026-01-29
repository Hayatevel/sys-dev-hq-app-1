package com.example.sysdevhqapp1backend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認証レスポンスDTO
 *
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

  private String token;
  private String username;
  private String email;
  private String role;
}
