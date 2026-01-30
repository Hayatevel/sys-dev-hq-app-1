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
  Stack,
  Chip,
} from "@mui/material";
import {
  Delete as DeleteIcon,
  Edit as EditIcon,
  Search as SearchIcon,
  Clear as ClearIcon,
} from "@mui/icons-material";
import { adminApi, type ChallengeSearchParams } from "../api/admin";
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
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [editingChallenge, setEditingChallenge] =
    useState<TypingChallenge | null>(null);
  const [deletingChallenge, setDeletingChallenge] =
    useState<TypingChallenge | null>(null);
  const [formData, setFormData] = useState<ChallengeCreateRequest>({
    language: "",
    codeSnippet: "",
    characterCount: 0,
    difficulty: "MEDIUM",
  });

  // 検索・フィルタリング用のステート
  const [searchParams, setSearchParams] = useState<ChallengeSearchParams>({
    language: "",
    difficulty: "",
    keyword: "",
  });

  // 利用可能な言語のリスト（既存データから抽出）
  const [availableLanguages, setAvailableLanguages] = useState<string[]>([]);

  const loadChallenges = async (filters?: ChallengeSearchParams) => {
    setLoading(true);
    setError("");
    try {
      const data = await adminApi.getAllChallenges(filters);
      setChallenges(data);

      // 言語リストを抽出（重複削除）
      const languages = Array.from(new Set(data.map((c) => c.language))).sort();
      setAvailableLanguages(languages);
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
      loadChallenges(getActiveFilters());
    } catch (err) {
      setError("保存に失敗しました");
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await adminApi.deleteChallenge(id);
      setOpenDeleteDialog(false);
      setDeletingChallenge(null);
      loadChallenges(getActiveFilters());
    } catch (err) {
      setError("削除に失敗しました");
    }
  };

  const handleOpenDeleteDialog = (challenge: TypingChallenge) => {
    setDeletingChallenge(challenge);
    setOpenDeleteDialog(true);
  };

  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
    setDeletingChallenge(null);
  };

  // フィルタリング実行
  const handleSearch = () => {
    loadChallenges(getActiveFilters());
  };

  // フィルタークリア
  const handleClearFilters = () => {
    setSearchParams({
      language: "",
      difficulty: "",
      keyword: "",
    });
    loadChallenges();
  };

  // アクティブなフィルターを取得
  const getActiveFilters = (): ChallengeSearchParams | undefined => {
    const filters: ChallengeSearchParams = {};
    if (searchParams.language) filters.language = searchParams.language;
    if (searchParams.difficulty) filters.difficulty = searchParams.difficulty;
    if (searchParams.keyword) filters.keyword = searchParams.keyword;

    return Object.keys(filters).length > 0 ? filters : undefined;
  };

  // アクティブフィルター数を計算
  const activeFilterCount = [
    searchParams.language,
    searchParams.difficulty,
    searchParams.keyword,
  ].filter((v) => v && v.trim() !== "").length;

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

        {/* 検索・フィルタリングセクション */}
        <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom>
            検索・フィルタリング
            {activeFilterCount > 0 && (
              <Chip
                label={`${activeFilterCount}件のフィルター適用中`}
                color="primary"
                size="small"
                sx={{ ml: 2 }}
              />
            )}
          </Typography>
          <Stack spacing={2}>
            <Box
              sx={{
                display: "flex",
                flexDirection: { xs: "column", md: "row" },
                gap: 2,
              }}
            >
              <FormControl sx={{ minWidth: 150, flex: 1 }} size="small">
                <InputLabel>言語</InputLabel>
                <Select
                  value={searchParams.language || ""}
                  onChange={(e) =>
                    setSearchParams({
                      ...searchParams,
                      language: e.target.value,
                    })
                  }
                  label="言語"
                >
                  <MenuItem value="">
                    <em>すべて</em>
                  </MenuItem>
                  {availableLanguages.map((lang) => (
                    <MenuItem key={lang} value={lang}>
                      {lang}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl sx={{ minWidth: 150, flex: 1 }} size="small">
                <InputLabel>難易度</InputLabel>
                <Select
                  value={searchParams.difficulty || ""}
                  onChange={(e) =>
                    setSearchParams({
                      ...searchParams,
                      difficulty: e.target.value,
                    })
                  }
                  label="難易度"
                >
                  <MenuItem value="">
                    <em>すべて</em>
                  </MenuItem>
                  <MenuItem value="EASY">EASY</MenuItem>
                  <MenuItem value="MEDIUM">MEDIUM</MenuItem>
                  <MenuItem value="HARD">HARD</MenuItem>
                </Select>
              </FormControl>

              <TextField
                sx={{ flex: 2 }}
                size="small"
                label="コードスニペット検索"
                placeholder="キーワードを入力..."
                value={searchParams.keyword || ""}
                onChange={(e) =>
                  setSearchParams({ ...searchParams, keyword: e.target.value })
                }
                onKeyPress={(e) => {
                  if (e.key === "Enter") {
                    handleSearch();
                  }
                }}
              />

              <Box sx={{ display: "flex", gap: 1, minWidth: 150 }}>
                <Button
                  variant="contained"
                  startIcon={<SearchIcon />}
                  onClick={handleSearch}
                  sx={{ flex: 1 }}
                >
                  検索
                </Button>
                {activeFilterCount > 0 && (
                  <IconButton
                    color="default"
                    onClick={handleClearFilters}
                    title="フィルタークリア"
                  >
                    <ClearIcon />
                  </IconButton>
                )}
              </Box>
            </Box>
          </Stack>
        </Paper>

        <Box
          sx={{
            mb: 2,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography variant="body2" color="text.secondary">
            {challenges.length}件のチャレンジ
          </Typography>
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
                      onClick={() => handleOpenDeleteDialog(challenge)}
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

        {/* 削除確認モーダル */}
        <Dialog
          open={openDeleteDialog}
          onClose={handleCloseDeleteDialog}
          maxWidth="sm"
          fullWidth
        >
          <DialogTitle sx={{ color: "error.main" }}>
            チャレンジの削除
          </DialogTitle>
          <DialogContent>
            <Typography variant="body1" gutterBottom sx={{ mt: 2 }}>
              以下のチャレンジを削除してもよろしいですか？
            </Typography>
            {deletingChallenge && (
              <Paper
                elevation={1}
                sx={{
                  p: 2,
                  mt: 2,
                  bgcolor: "grey.50",
                  border: 1,
                  borderColor: "grey.300",
                }}
              >
                <Typography variant="subtitle2" color="text.secondary">
                  ID: {deletingChallenge.id}
                </Typography>
                <Typography variant="body2" sx={{ mt: 1 }}>
                  <strong>言語:</strong> {deletingChallenge.language}
                </Typography>
                <Typography variant="body2">
                  <strong>難易度:</strong> {deletingChallenge.difficulty}
                </Typography>
                <Typography variant="body2" sx={{ mt: 1 }}>
                  <strong>コードスニペット:</strong>
                </Typography>
                <Box
                  sx={{
                    mt: 1,
                    p: 1,
                    bgcolor: "grey.900",
                    color: "grey.100",
                    borderRadius: 1,
                    fontFamily: "monospace",
                    fontSize: "0.875rem",
                    maxHeight: 200,
                    overflow: "auto",
                    whiteSpace: "pre-wrap",
                    wordBreak: "break-all",
                  }}
                >
                  {deletingChallenge.codeSnippet}
                </Box>
              </Paper>
            )}
            <Alert severity="warning" sx={{ mt: 2 }}>
              この操作は取り消せません。
            </Alert>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDeleteDialog} variant="outlined">
              キャンセル
            </Button>
            <Button
              onClick={() =>
                deletingChallenge && handleDelete(deletingChallenge.id)
              }
              variant="contained"
              color="error"
              startIcon={<DeleteIcon />}
            >
              削除する
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Container>
  );
};
