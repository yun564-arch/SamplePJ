# SamplePJ

## 概要
Spring Boot、MyBatis、PostgreSQLを使用して作成したTODO管理アプリです。
ユーザーごとにログインしてタスクを管理できます。

---

## 機能一覧
- ユーザー登録
- ログイン / ログアウト(Spring Security導入)
- タスク一覧表示
- タスク新規登録
- タスク編集
- タスク削除
- ページング機能(1ページ10件表示)
- バリデーション(Bean Validation)

---

## 使用技術
- Java 21
- Spring Boot
- Spring Security
- MyBatis
- PostgreSQL
- Thymeleaf(thymeleaf-extras-springsecurity6)
- HTML / CSS
- Git / GitHub

---

## セキュリティ対策
- パスワードはBCryptでハッシュ化して保存(平文保存なし)
- タスクの編集・削除は所有者本人のみ操作可能(IDOR対策)
- Spring SecurityによるCSRF対策(状態変更を伴うフォームにはトークンを自動付与)
- ログイン処理・認可制御をSpring Securityに統一

---

## 環境変数の設定

アプリを起動する前に、以下の環境変数を設定してください。

| 環境変数名 | 説明 | 例 |
|---|---|---|
| `DB_URL` | PostgreSQLの接続URL | `jdbc:postgresql://localhost:5432/todo_app` |
| `DB_USER` | PostgreSQLの接続ユーザー名 | `postgres` |
| `DB_PASSWORD` | PostgreSQLの接続パスワード | `your_password` |

### Eclipseでの設定方法
1. プロジェクトを右クリック → **Run As** → **Run Configurations**
2. **Spring Boot App** → 対象の設定を選択
3. **Environment**タブ → **Add**で上記3つの変数を追加

---

## データベースのセットアップ

1. PostgreSQLで`todo_app`という名前のデータベースを作成してください。

```sql
CREATE DATABASE todo_app;
```

2. 以下のコマンドでテーブルを作成します。

```bash
psql -U あなたのDBユーザー名 -d todo_app -f src/main/resources/db/schema.sql
```

または、pgAdmin4のQuery Toolに`src/main/resources/db/schema.sql`の内容を貼り付けて実行してください。

> `schema.sql`には`tasks.username`への外部キー制約(FK)とINDEXが含まれています。

---

## バリデーション

### ユーザー登録
- ユーザー名必須・半角英数字
- パスワード必須・半角英数字
- ユーザー名重複チェック

### タスク登録
- タイトル必須
- 終了日は開始日以降(日付相関チェック、項目別エラー表示対応)

Bean Validation(`@NotBlank`等)とThymeleafの`th:object`/`th:field`によるフォームバインディングで、項目ごとにエラーメッセージを表示します。

---

## アーキテクチャ・設計

- レイヤ構成:Controller / Service(interface + impl)/ Mapper(interface + XML)/ Entity
- 全クラスでコンストラクタインジェクションを採用
- Service層に`@Transactional`を付与(書き込み系/`readOnly`系を分離)
- 業務例外(`TaskNotFoundException`)と`@ControllerAdvice`による例外の一元管理
- 例外ログはSLF4Jロガーで出力(printStackTrace不使用)
- SQLは`#{}`によるバインドでSQLインジェクション対策済み

---

## 工夫した点
- Spring SecurityでログインガードとCSRF対策を実装
- SQLのLIMIT/OFFSETを利用してページング機能を実装
- ユーザーごとにタスクを管理できるよう、所有者チェックを徹底
- 共通CSS(`common.css`)に切り出してスタイルの重複を解消(inline style排除)
- `label`の`for`属性を整備し、アクセシビリティに配慮

---

## 今後の課題
- タスク検索機能
- 共有タスク機能