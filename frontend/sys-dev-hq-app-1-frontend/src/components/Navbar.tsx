import React from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * ナビゲーションバーコンポーネント
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

export const Navbar: React.FC = () => {
  const { isAuthenticated, isAdmin, logout, user } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          <Link to="/" style={{ color: "white", textDecoration: "none" }}>
            タイピングゲーム
          </Link>
        </Typography>

        <Box sx={{ display: "flex", gap: 2 }}>
          {isAuthenticated ? (
            <>
              <Button color="inherit" component={Link} to="/">
                ゲーム
              </Button>
              <Button color="inherit" component={Link} to="/history">
                履歴
              </Button>
              <Button color="inherit" component={Link} to="/ranking">
                ランキング
              </Button>
              {isAdmin && (
                <Button color="inherit" component={Link} to="/admin">
                  管理
                </Button>
              )}
              <Typography
                variant="body1"
                sx={{ display: "flex", alignItems: "center", mr: 2 }}
              >
                {user?.username}
              </Typography>
              <Button color="inherit" onClick={handleLogout}>
                ログアウト
              </Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={Link} to="/login">
                ログイン
              </Button>
              <Button color="inherit" component={Link} to="/signup">
                サインアップ
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};
