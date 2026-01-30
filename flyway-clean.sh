#!/usr/bin/bash

# Flywayを使ってデータベースをクリーンにするスクリプト
echo "==================================="
echo "Flyway Clean 実行"
echo "==================================="

cd /workspace/backend/sysdevhqapp1backend

echo "データベースをクリーンにしています..."
./mvnw flyway:clean \
  -Dflyway.url=jdbc:mysql://db:3306/sys-dev-hq-app-1-db \
  -Dflyway.user=root \
  -Dflyway.password=root \
  -Dflyway.cleanDisabled=false

if [ $? -eq 0 ]; then
    echo "✓ データベースクリーン完了"
    echo ""
    echo "次のコマンドでバックエンドを起動してください："
    echo "  bash startup-backend.sh"
else
    echo "✗ データベースクリーン失敗"
    exit 1
fi
