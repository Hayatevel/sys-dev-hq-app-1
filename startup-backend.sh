#!/usr/bin/bash

# 自分自身のフルパス
self_path=$(realpath "$0");
echo "自分自身のフルパス (realpath): ${self_path}";

# 自分自身のディレクトリ
self_dir=$(dirname "$self_path");
echo "自分自身のディレクトリ (dirname): ${self_dir}";

# 自分自身のファイル名
self_base=$(basename "$self_path");
echo "自分自身のファイル名 (basename): ${self_base}";

# 実行ディレクトリ
echo "実行ディレクトリ (PWD): ${PWD}";

# backend を実行
cd ./backend/sysdevhqapp1backend
./mvnw install
./mvnw spring-boot:run
