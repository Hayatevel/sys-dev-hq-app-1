FROM ubuntu:24.04

# 環境変数の設定
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8 \
    DEBIAN_FRONTEND=noninteractive

# すべてのパッケージを一度にインストール
RUN apt-get update && \
  apt-get install -y --no-install-recommends \
    locales \
    git \
    curl \
    wget \
    ca-certificates \
    gnupg \
    sudo \
    zsh \
    openjdk-21-jdk \
    maven \
  && locale-gen en_US.UTF-8 \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

# JAVA_HOME の設定
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Node.js (最新LTS v22.x) のインストール
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
  apt-get install -y --no-install-recommends nodejs && \
  apt-get clean && \
  rm -rf /var/lib/apt/lists/*

WORKDIR /workspace
