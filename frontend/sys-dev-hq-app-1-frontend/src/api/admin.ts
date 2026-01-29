import { apiClient } from "../utils/api";
import type { TypingChallenge, ChallengeCreateRequest } from "../types";

/**
 * 管理者用チャレンジAPI
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const adminApi = {
  /**
   * 全てのチャレンジを取得
   */
  getAllChallenges: async (): Promise<TypingChallenge[]> => {
    return apiClient.get<TypingChallenge[]>("/admin/challenges");
  },

  /**
   * チャレンジを作成
   */
  createChallenge: async (
    data: ChallengeCreateRequest,
  ): Promise<TypingChallenge> => {
    return apiClient.post<TypingChallenge>("/admin/challenges", data);
  },

  /**
   * チャレンジを更新
   */
  updateChallenge: async (
    id: number,
    data: ChallengeCreateRequest,
  ): Promise<TypingChallenge> => {
    return apiClient.put<TypingChallenge>(`/admin/challenges/${id}`, data);
  },

  /**
   * チャレンジを削除
   */
  deleteChallenge: async (id: number): Promise<void> => {
    return apiClient.delete(`/admin/challenges/${id}`);
  },
};
