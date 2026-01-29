-- ===================================================================
-- タイピングゲームアプリケーション データベース初期化スクリプト
-- ===================================================================
--
-- 注意: 現在このファイルは使用されていません
--
-- 理由:
-- - テーブル構造は Spring Boot の Hibernate (ddl-auto=update) によって自動作成されます
-- - 初期データは DataInitializer.java によってアプリケーション起動時に自動投入されます
--
-- このファイルは以下の場合に備えて保持されています:
-- - 本番環境でのマイグレーション (ddl-auto=none の場合)
-- - 手動でのデータベース再構築
-- - Devcontainer を使用しない環境での開発
--
-- 必要に応じて、以下のコマンドでデータベースを手動で初期化できます:
-- mysql -h db -u root -prootpassword sys-dev-hq-app-1-db < /workspace/db/init/init.sql
--
-- ===================================================================

-- データベースの作成
CREATE DATABASE IF NOT EXISTS `sys-dev-hq-app-1-db` 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;
