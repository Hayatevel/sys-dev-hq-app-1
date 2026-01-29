import { apiClient } from "../utils/api";
import type { TypingChallenge } from "../types";

/**
 * チャレンジAPI
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const challengeApi = {
  /**
   * ランダムなチャレンジを取得
   */
  getRandomChallenge: async (language?: string): Promise<TypingChallenge> => {
    const endpoint = language
      ? `/challenges/random?language=${encodeURIComponent(language)}`
      : "/challenges/random";
    return apiClient.get<TypingChallenge>(endpoint, false);
  },

  /**
   * 全てのチャレンジを取得
   */
  getAllChallenges: async (): Promise<TypingChallenge[]> => {
    return apiClient.get<TypingChallenge[]>("/challenges");
  },

  /**
   * チャレンジをIDで取得
   */
  getChallengeById: async (id: number): Promise<TypingChallenge> => {
    return apiClient.get<TypingChallenge>(`/challenges/${id}`);
  },
};
