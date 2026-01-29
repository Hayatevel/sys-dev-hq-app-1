import { apiClient } from "../utils/api";
import type { Score, ScoreCreateRequest } from "../types";

/**
 * スコアAPI
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const scoreApi = {
  /**
   * スコアを作成
   */
  createScore: async (data: ScoreCreateRequest): Promise<Score> => {
    return apiClient.post<Score>("/scores", data);
  },

  /**
   * 自分のスコア履歴を取得
   */
  getMyScores: async (page = 0, size = 10): Promise<Score[]> => {
    return apiClient.get<Score[]>(
      `/scores/my-scores?page=${page}&size=${size}`,
    );
  },

  /**
   * ランキングを取得
   */
  getRankings: async (limit = 10): Promise<Score[]> => {
    return apiClient.get<Score[]>(`/rankings?limit=${limit}`, false);
  },
};
