/**
 * 型定義ファイル
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export interface User {
  id: number;
  username: string;
  email: string;
  role: "USER" | "ADMIN";
}

export interface AuthResponse {
  token: string;
  username: string;
  email: string;
  role: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  password: string;
  email: string;
}

export interface TypingChallenge {
  id: number;
  language: string;
  codeSnippet: string;
  characterCount: number;
  difficulty: "EASY" | "MEDIUM" | "HARD";
  createdAt: string;
  updatedAt: string;
}

export interface Score {
  id: number;
  userId: number;
  username: string;
  challengeId: number;
  language: string;
  wpm: number;
  accuracy: number;
  timeTakenSeconds: number;
  createdAt: string;
}

export interface ScoreCreateRequest {
  challengeId: number;
  wpm: number;
  accuracy: number;
  timeTakenSeconds: number;
}

export interface ChallengeCreateRequest {
  language: string;
  codeSnippet: string;
  characterCount: number;
  difficulty: string;
}
