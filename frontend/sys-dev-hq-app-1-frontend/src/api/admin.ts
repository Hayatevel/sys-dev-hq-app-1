import { apiClient } from "../utils/api";
import type { TypingChallenge, ChallengeCreateRequest } from "../types";

/**
 * 検索・フィルタリング用のパラメータ型定義
 */
export interface ChallengeSearchParams {
  language?: string;
  difficulty?: string;
  keyword?: string;
}

/**
 * 管理者用チャレンジAPI
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const adminApi = {
  /**
   * 全てのチャレンジを取得（検索・フィルタリング対応）
   */
  getAllChallenges: async (
    params?: ChallengeSearchParams,
  ): Promise<TypingChallenge[]> => {
    const queryParams = new URLSearchParams();
    if (params?.language) queryParams.append("language", params.language);
    if (params?.difficulty) queryParams.append("difficulty", params.difficulty);
    if (params?.keyword) queryParams.append("keyword", params.keyword);

    const queryString = queryParams.toString();
    const url = queryString
      ? `/admin/challenges?${queryString}`
      : "/admin/challenges";

    return apiClient.get<TypingChallenge[]>(url);
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
