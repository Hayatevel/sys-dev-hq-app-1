# Flyway データベースマイグレーション

このプロジェクトでは、データベーススキーマとデータの管理にFlywayを使用しています。

## マイグレーションファイルの概要

### V1\_\_Create_tables.sql

- 目的: 初期テーブル構造の作成
- 内容: users, typing_challenges, scores テーブルとインデックスの作成

### V2\_\_Insert_initial_data.sql

- 目的: 初期データの投入
- 内容: 管理者ユーザーと18個のサンプルチャレンジデータ
- 注意: V5でパスワードハッシュが修正されています

### V3\_\_Fix_admin_user_password.sql

- 目的: 管理者パスワードの修正試行（失敗）
- 注意: 廃止 - V5を使用してください

### V4\_\_Ensure_admin_user.sql

- 目的: 管理者ユーザーの再作成試行（失敗）
- 注意: 廃止 - V5を使用してください

### V5\_\_Fix_admin_password_hash.sql

- 目的: 正しいBCryptハッシュでadminユーザーを作成
- 内容: admin/admin123（パスワード）で管理者アカウントを作成
- ステータス: **本番環境で使用する正しいマイグレーション**

## 本番環境デプロイ時の注意事項

### 1. クリーンな環境での初期セットアップ

新規環境（本番環境など）では、以下の手順でセットアップしてください：

1. データベースを空の状態で作成
2. アプリケーションを起動すると、Flywayが自動的にV1→V2→V3→V4→V5を順番に実行
3. V3とV4は不要な試行ですが、Flywayのバージョン管理上削除できません

### 2. 既存環境のマイグレーション

開発環境から本番環境への移行時：

1. `flyway_schema_history` テーブルでマイグレーション履歴を確認
2. 未適用のマイグレーションがあれば自動的に適用されます
3. V5が適用されていることを確認（`SELECT * FROM flyway_schema_history WHERE version = '5'`）

### 3. セキュリティ考慮事項

本番環境では以下の対応が必要です：

- **管理者パスワードの変更**: デフォルトの `admin123` から強力なパスワードへ変更
- **デバッグエンドポイントの無効化**: `/api/debug/**` エンドポイントを本番環境では無効にする
- **環境変数の使用**: データベース接続情報を環境変数で管理

## Flyway設定（application.properties）

```properties
# Flyway設定
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true

# Hibernate設定（Flywayがスキーマを管理）
spring.jpa.hibernate.ddl-auto=validate
```

## トラブルシューティング

### マイグレーション失敗時

1. `flyway_schema_history` テーブルを確認
2. 失敗したマイグレーションのバージョンを特定
3. 必要に応じて `flyway repair` コマンドを実行

### ロールバック

Flywayの無料版にはロールバック機能がありません。以下の方法で対応：

1. データベースバックアップから復元
2. 新しいマイグレーションで修正内容を適用

## マイグレーション命名規則

- `V{バージョン}__{説明}.sql`: バージョン管理されたマイグレーション
- バージョンは連番（V1, V2, V3...）
- 説明はアンダースコアで単語を区切る（例: `Create_tables`, `Insert_initial_data`）
- 一度適用されたマイグレーションは絶対に変更しない

## 参考リンク

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
