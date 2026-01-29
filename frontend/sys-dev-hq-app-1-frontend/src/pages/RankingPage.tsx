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
 * ランキングページ
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const RankingPage: React.FC = () => {
  const [scores, setScores] = useState<Score[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadRankings = async () => {
      try {
        const data = await scoreApi.getRankings(50);
        setScores(data);
      } catch (err) {
        setError("ランキングの読み込みに失敗しました");
      } finally {
        setLoading(false);
      }
    };

    loadRankings();
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
          ランキング
        </Typography>

        {scores.length === 0 ? (
          <Paper elevation={3} sx={{ p: 4, textAlign: "center" }}>
            <Typography variant="h6" color="text.secondary">
              まだスコアがありません
            </Typography>
          </Paper>
        ) : (
          <TableContainer component={Paper} elevation={3}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>順位</TableCell>
                  <TableCell>ユーザー名</TableCell>
                  <TableCell>言語</TableCell>
                  <TableCell align="right">WPM</TableCell>
                  <TableCell align="right">正確性</TableCell>
                  <TableCell align="right">所要時間</TableCell>
                  <TableCell>日時</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {scores.map((score, index) => (
                  <TableRow key={score.id}>
                    <TableCell>
                      <Typography
                        variant="h6"
                        sx={{
                          fontWeight: "bold",
                          color:
                            index === 0
                              ? "gold"
                              : index === 1
                                ? "silver"
                                : index === 2
                                  ? "#cd7f32"
                                  : "inherit",
                        }}
                      >
                        {index + 1}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body1" sx={{ fontWeight: "bold" }}>
                        {score.username}
                      </Typography>
                    </TableCell>
                    <TableCell>{score.language}</TableCell>
                    <TableCell align="right">
                      <Typography variant="body1" sx={{ fontWeight: "bold" }}>
                        {score.wpm.toFixed(2)}
                      </Typography>
                    </TableCell>
                    <TableCell align="right">
                      {score.accuracy.toFixed(2)}%
                    </TableCell>
                    <TableCell align="right">
                      {score.timeTakenSeconds}秒
                    </TableCell>
                    <TableCell>
                      {new Date(score.createdAt).toLocaleString("ja-JP")}
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
