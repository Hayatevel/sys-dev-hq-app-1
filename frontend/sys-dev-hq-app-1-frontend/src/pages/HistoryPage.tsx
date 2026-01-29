import React, { useState, useEffect } from "react";
import {
  Container,
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
  Alert,
} from "@mui/material";
import { scoreApi } from "../api/score";
import type { Score } from "../types";

/**
 * スコア履歴ページ
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const HistoryPage: React.FC = () => {
  const [scores, setScores] = useState<Score[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadScores = async () => {
      try {
        const data = await scoreApi.getMyScores(0, 50);
        setScores(data);
      } catch (err) {
        setError("スコアの読み込みに失敗しました");
      } finally {
        setLoading(false);
      }
    };

    loadScores();
  }, []);

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
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          プレイ履歴
        </Typography>

        {scores.length === 0 ? (
          <Paper elevation={3} sx={{ p: 4, textAlign: "center" }}>
            <Typography variant="h6" color="text.secondary">
              まだプレイ履歴がありません
            </Typography>
          </Paper>
        ) : (
          <TableContainer component={Paper} elevation={3}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>日時</TableCell>
                  <TableCell>言語</TableCell>
                  <TableCell align="right">WPM</TableCell>
                  <TableCell align="right">正確性</TableCell>
                  <TableCell align="right">所要時間</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {scores.map((score) => (
                  <TableRow key={score.id}>
                    <TableCell>
                      {new Date(score.createdAt).toLocaleString("ja-JP")}
                    </TableCell>
                    <TableCell>{score.language}</TableCell>
                    <TableCell align="right">{score.wpm.toFixed(2)}</TableCell>
                    <TableCell align="right">
                      {score.accuracy.toFixed(2)}%
                    </TableCell>
                    <TableCell align="right">
                      {score.timeTakenSeconds}秒
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Box>
    </Container>
  );
};
