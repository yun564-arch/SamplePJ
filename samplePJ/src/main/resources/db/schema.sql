-- ====================================
-- samplePJ データベーススキーマ定義
-- 第三者がこのSQLを実行することで、同じテーブル構造を再現できます。
-- ====================================

-- ユーザー情報テーブル
CREATE TABLE login (
  id BIGSERIAL PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  created_at TIMESTAMP DEFAULT date_trunc('second', now()),
  updated_at TIMESTAMP DEFAULT date_trunc('second', now())
);

-- タスク情報テーブル
CREATE TABLE tasks (
  id BIGSERIAL PRIMARY KEY,
  username varchar(50) NOT NULL,
  title varchar(255) NOT NULL,
  content TEXT,
  name varchar(100),
  start_date DATE,
  end_date DATE,
  created_at TIMESTAMP DEFAULT date_trunc('second', now()),
  updated_at TIMESTAMP DEFAULT date_trunc('second', now())
);