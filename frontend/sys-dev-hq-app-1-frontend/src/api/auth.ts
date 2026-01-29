import { apiClient } from "../utils/api";
import type { AuthResponse, LoginRequest, SignupRequest } from "../types";

/**
 * 認証API
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const authApi = {
  /**
   * ログイン
   */
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>("/auth/login", credentials, false);
  },

  /**
   * サインアップ
   */
  signup: async (data: SignupRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>("/auth/signup", data, false);
  },

  /**
   * ログアウト
   */
  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },
};
