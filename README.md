# タイピングゲーム Web アプリケーション

プログラミング言語のコードスニペットを使用したタイピング練習Webアプリケーションです。

## 機能

- ✅ ユーザー認証（サインアップ、ログイン、ログアウト）
- ✅ JWT認証
- ✅ タイピングゲーム（Java、Python、JavaScript、C++、Go、TypeScriptなど）
- ✅ WPM（Words Per Minute）と正確性の計算
- ✅ スコア履歴の確認
- ✅ ランキング機能
- ✅ 管理者によるお題管理（参照、追加、編集、削除）

## 技術スタック

### バックエンド

- Spring Boot 3.5.3
- Java 21
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0

### フロントエンド

- React 19
- TypeScript
- Material-UI
- React Router
- Vite

### インフラ

- Docker / Docker Compose
- Devcontainer

## セットアップ

### 前提条件

- Docker
- Docker Compose
- VS Code（推奨）
- Dev Containers拡張機能（VS Code使用時）

### 起動方法

#### 1. データベースの起動

データベースが既に起動していない場合は、Docker Composeで起動します。

```bash
docker-compose up -d db
```

#### 2. バックエンドの起動

```bash
cd /workspace
./startup-backend.sh
```

または

```bash
cd /workspace/backend/sysdevhqapp1backend
./mvnw spring-boot:run
```

バックエンドは http://localhost:8080 で起動します。

#### 3. フロントエンドの起動

別のターミナルで以下を実行：

```bash
cd /workspace
./startup-frontend.sh
```

または

```bash
cd /workspace/frontend/sys-dev-hq-app-1-frontend
npm install
npm run dev
```

フロントエンドは http://localhost:5173 で起動します。

## 使い方

### 1. アカウント作成

1. ブラウザで http://localhost:5173 にアクセス
2. 「サインアップ」をクリック
3. ユーザー名、メールアドレス、パスワードを入力して登録

### 2. タイピングゲームをプレイ

1. ログイン後、トップページでタイピングゲームが表示されます
2. 表示されたコードスニペットを入力エリアに正確に入力
3. 完了すると、WPM、正確性、所要時間が表示されます
4. 「次のチャレンジ」ボタンで新しいお題に挑戦

### 3. スコア履歴を確認

- ナビゲーションバーの「履歴」をクリック
- 過去のプレイ記録を確認できます

### 4. ランキングを確認

- ナビゲーションバーの「ランキング」をクリック
- 全ユーザーの上位スコアを確認できます

### 5. 管理者機能（管理者のみ）

- 管理者アカウント：
  - ユーザー名: `admin`
  - パスワード: `admin123`
- ログイン後、「管理」ページでお題の追加、編集、削除ができます

## API エンドポイント

### 認証

- `POST /api/auth/signup` - サインアップ
- `POST /api/auth/login` - ログイン

### チャレンジ

- `GET /api/challenges` - 全チャレンジ取得
- `GET /api/challenges/random` - ランダムチャレンジ取得
- `GET /api/challenges/{id}` - チャレンジ詳細取得

### スコア

- `POST /api/scores` - スコア作成（認証必須）
- `GET /api/scores/my-scores` - 自分のスコア履歴取得（認証必須）
- `GET /api/rankings` - ランキング取得

### 管理者（ADMIN権限必須）

- `GET /api/admin/challenges` - 全チャレンジ取得
- `POST /api/admin/challenges` - チャレンジ作成
- `PUT /api/admin/challenges/{id}` - チャレンジ更新
- `DELETE /api/admin/challenges/{id}` - チャレンジ削除

## データベース

初期データとして以下が設定されています：

- 管理者ユーザー（username: admin, password: admin123）
- 各プログラミング言語のサンプルコードスニペット（Java、Python、JavaScript、C++、Go、TypeScript）

## トラブルシューティング

### ログインできない場合

1. **バックエンドを再起動**

   ```bash
   # Ctrl+C でバックエンドを停止してから
   cd /workspace/backend/sysdevhqapp1backend
   ./mvnw clean spring-boot:run
   ```

2. **初回起動時は初期データが自動的に投入されます**
   - `spring.sql.init.mode=always` により、data.sqlが実行されます
   - 管理者ユーザー（admin / admin123）とサンプルチャレンジが作成されます

3. **ログを確認**
   - バックエンドのコンソールでエラーログを確認してください
   - `DEBUG`レベルでログが出力されます

### チャレンジの読み込みに失敗する場合

1. **バックエンドが起動しているか確認**

   ```bash
   curl http://localhost:8080/api/challenges/random
   ```

2. **CORS設定を確認**
   - フロントエンドが http://localhost:5173 で起動していることを確認
   - バックエンドのCORS設定で許可されています

3. **データベースにデータが存在するか確認**
   - 初回起動時にdata.sqlが実行され、サンプルデータが投入されます

## 開発

### バックエンドの再ビルド

```bash
cd /workspace/backend/sysdevhqapp1backend
./mvnw clean compile
```

### フロントエンドの再ビルド

```bash
cd /workspace/frontend/sys-dev-hq-app-1-frontend
npm run build
```

## ライセンス

MIT License

## 作成者

Hayate Aoki
