import React, { useState, useEffect } from "react";
import {
  Container,
  Box,
  Typography,
  Paper,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  CircularProgress,
  IconButton,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@mui/material";
import { Delete as DeleteIcon, Edit as EditIcon } from "@mui/icons-material";
import { adminApi } from "../api/admin";
import type { TypingChallenge, ChallengeCreateRequest } from "../types";

/**
 * 管理者ページ
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const AdminPage: React.FC = () => {
  const [challenges, setChallenges] = useState<TypingChallenge[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [openDialog, setOpenDialog] = useState(false);
  const [editingChallenge, setEditingChallenge] =
    useState<TypingChallenge | null>(null);
  const [formData, setFormData] = useState<ChallengeCreateRequest>({
    language: "",
    codeSnippet: "",
    characterCount: 0,
    difficulty: "MEDIUM",
  });

  const loadChallenges = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await adminApi.getAllChallenges();
      setChallenges(data);
    } catch (err) {
      setError("チャレンジの読み込みに失敗しました");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadChallenges();
  }, []);

  const handleOpenDialog = (challenge?: TypingChallenge) => {
    if (challenge) {
      setEditingChallenge(challenge);
      setFormData({
        language: challenge.language,
        codeSnippet: challenge.codeSnippet,
        characterCount: challenge.characterCount,
        difficulty: challenge.difficulty,
      });
    } else {
      setEditingChallenge(null);
      setFormData({
        language: "",
        codeSnippet: "",
        characterCount: 0,
        difficulty: "MEDIUM",
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingChallenge(null);
  };

  const handleSave = async () => {
    try {
      const dataToSave = {
        ...formData,
        characterCount: formData.codeSnippet.length,
      };

      if (editingChallenge) {
        await adminApi.updateChallenge(editingChallenge.id, dataToSave);
      } else {
        await adminApi.createChallenge(dataToSave);
      }

      handleCloseDialog();
      loadChallenges();
    } catch (err) {
      setError("保存に失敗しました");
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("本当に削除しますか？")) {
      return;
    }

    try {
      await adminApi.deleteChallenge(id);
      loadChallenges();
    } catch (err) {
      setError("削除に失敗しました");
    }
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

  return (
    <Container maxWidth="xl">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          チャレンジ管理
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box sx={{ mb: 2 }}>
          <Button variant="contained" onClick={() => handleOpenDialog()}>
            新規作成
          </Button>
        </Box>

        <TableContainer component={Paper} elevation={3}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>言語</TableCell>
                <TableCell>難易度</TableCell>
                <TableCell>文字数</TableCell>
                <TableCell>コードスニペット</TableCell>
                <TableCell>作成日時</TableCell>
                <TableCell align="center">操作</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {challenges.map((challenge) => (
                <TableRow key={challenge.id}>
                  <TableCell>{challenge.id}</TableCell>
                  <TableCell>{challenge.language}</TableCell>
                  <TableCell>{challenge.difficulty}</TableCell>
                  <TableCell>{challenge.characterCount}</TableCell>
                  <TableCell>
                    <Box
                      sx={{
                        maxWidth: 400,
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                        fontFamily: "monospace",
                      }}
                    >
                      {challenge.codeSnippet}
                    </Box>
                  </TableCell>
                  <TableCell>
                    {new Date(challenge.createdAt).toLocaleString("ja-JP")}
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      color="primary"
                      onClick={() => handleOpenDialog(challenge)}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      color="error"
                      onClick={() => handleDelete(challenge.id)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        <Dialog
          open={openDialog}
          onClose={handleCloseDialog}
          maxWidth="md"
          fullWidth
        >
          <DialogTitle>
            {editingChallenge ? "チャレンジ編集" : "新規チャレンジ作成"}
          </DialogTitle>
          <DialogContent>
            <Box
              sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}
            >
              <TextField
                label="プログラミング言語"
                value={formData.language}
                onChange={(e) =>
                  setFormData({ ...formData, language: e.target.value })
                }
                required
                fullWidth
              />

              <FormControl fullWidth>
                <InputLabel>難易度</InputLabel>
                <Select
                  value={formData.difficulty}
                  onChange={(e) =>
                    setFormData({ ...formData, difficulty: e.target.value })
                  }
                  label="難易度"
                >
                  <MenuItem value="EASY">EASY</MenuItem>
                  <MenuItem value="MEDIUM">MEDIUM</MenuItem>
                  <MenuItem value="HARD">HARD</MenuItem>
                </Select>
              </FormControl>

              <TextField
                label="コードスニペット"
                value={formData.codeSnippet}
                onChange={(e) =>
                  setFormData({ ...formData, codeSnippet: e.target.value })
                }
                required
                fullWidth
                multiline
                rows={10}
                InputProps={{
                  style: { fontFamily: "monospace" },
                }}
              />

              <Typography variant="body2" color="text.secondary">
                文字数: {formData.codeSnippet.length}
              </Typography>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>キャンセル</Button>
            <Button
              onClick={handleSave}
              variant="contained"
              disabled={!formData.language || !formData.codeSnippet}
            >
              保存
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Container>
  );
};
