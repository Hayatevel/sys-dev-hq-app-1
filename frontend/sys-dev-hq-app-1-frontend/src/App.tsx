import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { CssBaseline, Box } from "@mui/material";
import { AuthProvider } from "./contexts/AuthContext";
import { Navbar } from "./components/Navbar";
import { PrivateRoute } from "./components/PrivateRoute";
import { AdminRoute } from "./components/AdminRoute";
import { LoginPage } from "./pages/LoginPage";
import { SignupPage } from "./pages/SignupPage";
import { TypingGamePage } from "./pages/TypingGamePage";
import { HistoryPage } from "./pages/HistoryPage";
import { RankingPage } from "./pages/RankingPage";
import { AdminPage } from "./pages/AdminPage";

/**
 * メインアプリケーションコンポーネント
 * @version 1.0
 * @since 2026-02-01
 * @author Hayate Aoki
 */

function App() {
  return (
    <Router>
      <AuthProvider>
        <CssBaseline />
        <Box sx={{ minHeight: "100vh", backgroundColor: "#f5f5f5" }}>
          <Navbar />
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route
              path="/"
              element={
                <PrivateRoute>
                  <TypingGamePage />
                </PrivateRoute>
              }
            />
            <Route
              path="/history"
              element={
                <PrivateRoute>
                  <HistoryPage />
                </PrivateRoute>
              }
            />
            <Route path="/ranking" element={<RankingPage />} />
            <Route
              path="/admin"
              element={
                <AdminRoute>
                  <AdminPage />
                </AdminRoute>
              }
            />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Box>
      </AuthProvider>
    </Router>
  );
}

export default App;
