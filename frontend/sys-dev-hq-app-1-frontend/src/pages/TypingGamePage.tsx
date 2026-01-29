import React, { useState, useEffect, useRef } from "react";
import {
  Container,
  Box,
  Paper,
  Typography,
  Button,
  Alert,
  CircularProgress,
} from "@mui/material";
import { challengeApi } from "../api/challenge";
import { scoreApi } from "../api/score";
import type { TypingChallenge } from "../types";

/**
 * タイピングゲームページ
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const TypingGamePage: React.FC = () => {
  const [challenge, setChallenge] = useState<TypingChallenge | null>(null);
  const [input, setInput] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [isCompleted, setIsCompleted] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState<{
    wpm: number;
    accuracy: number;
    timeTaken: number;
  } | null>(null);

  const inputRef = useRef<HTMLTextAreaElement>(null);

  const loadChallenge = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await challengeApi.getRandomChallenge();
      setChallenge(data);
      setInput("");
      setStartTime(null);
      setIsCompleted(false);
      setStats(null);
    } catch (err) {
      setError("チャレンジの読み込みに失敗しました");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadChallenge();
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    setInput(value);

    if (!startTime) {
      setStartTime(Date.now());
    }

    if (challenge && value === challenge.codeSnippet) {
      const end = Date.now();
      setIsCompleted(true);
      calculateStats(startTime!, end, value);
    }
  };

  const calculateStats = async (
    start: number,
    end: number,
    typedText: string,
  ) => {
    if (!challenge) return;

    const timeTaken = (end - start) / 1000; // 秒
    const words = typedText.split(/\s+/).length;
    const wpm = Math.round((words / timeTaken) * 60);

    // 正確性の計算
    const targetText = challenge.codeSnippet;
    let correctChars = 0;
    for (let i = 0; i < Math.min(typedText.length, targetText.length); i++) {
      if (typedText[i] === targetText[i]) {
        correctChars++;
      }
    }
    const accuracy = Math.round((correctChars / targetText.length) * 100);

    setStats({ wpm, accuracy, timeTaken: Math.round(timeTaken) });

    // スコアを保存
    try {
      await scoreApi.createScore({
        challengeId: challenge.id,
        wpm,
        accuracy,
        timeTakenSeconds: Math.round(timeTaken),
      });
    } catch (err) {
      console.error("スコアの保存に失敗しました:", err);
    }
  };

  const resetGame = () => {
    loadChallenge();
    inputRef.current?.focus();
  };

  if (loading) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ display: "flex", justifyContent: "center", mt: 8 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ mt: 4 }}>
          <Alert severity="error">{error}</Alert>
          <Button onClick={loadChallenge} sx={{ mt: 2 }}>
            再試行
          </Button>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          タイピングゲーム
        </Typography>

        {challenge && (
          <>
            <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                言語: {challenge.language} | 難易度: {challenge.difficulty}
              </Typography>
              <Box
                sx={{
                  backgroundColor: "#f5f5f5",
                  p: 2,
                  borderRadius: 1,
                  fontFamily: "monospace",
                  whiteSpace: "pre-wrap",
                  fontSize: "1.1rem",
                  lineHeight: 1.6,
                }}
              >
                {challenge.codeSnippet}
              </Box>
            </Paper>

            <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                入力エリア
              </Typography>
              <textarea
                ref={inputRef}
                value={input}
                onChange={handleInputChange}
                disabled={isCompleted}
                style={{
                  width: "100%",
                  minHeight: "200px",
                  fontFamily: "monospace",
                  fontSize: "1.1rem",
                  padding: "16px",
                  border: "1px solid #ccc",
                  borderRadius: "4px",
                  resize: "vertical",
                }}
                autoFocus
              />
            </Paper>

            {isCompleted && stats && (
              <Paper
                elevation={3}
                sx={{ p: 3, mb: 3, backgroundColor: "#e8f5e9" }}
              >
                <Typography
                  variant="h5"
                  gutterBottom
                  align="center"
                  color="success.main"
                >
                  完了！
                </Typography>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-around",
                    mt: 2,
                  }}
                >
                  <Box sx={{ textAlign: "center" }}>
                    <Typography variant="h4" color="primary">
                      {stats.wpm}
                    </Typography>
                    <Typography variant="body1">WPM</Typography>
                  </Box>
                  <Box sx={{ textAlign: "center" }}>
                    <Typography variant="h4" color="primary">
                      {stats.accuracy}%
                    </Typography>
                    <Typography variant="body1">正確性</Typography>
                  </Box>
                  <Box sx={{ textAlign: "center" }}>
                    <Typography variant="h4" color="primary">
                      {stats.timeTaken}秒
                    </Typography>
                    <Typography variant="body1">所要時間</Typography>
                  </Box>
                </Box>
                <Box sx={{ textAlign: "center", mt: 3 }}>
                  <Button variant="contained" size="large" onClick={resetGame}>
                    次のチャレンジ
                  </Button>
                </Box>
              </Paper>
            )}

            {!isCompleted && (
              <Box sx={{ textAlign: "center" }}>
                <Button variant="outlined" onClick={resetGame}>
                  リセット
                </Button>
              </Box>
            )}
          </>
        )}
      </Box>
    </Container>
  );
};
