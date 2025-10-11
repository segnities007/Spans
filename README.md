# Spans — すれ違い SNS (Working title)

短い説明
- 物理的にすれ違った相手の投稿だけを閲覧できる、時間制限付きの近距離SNSアプリケーション（コンセプト）。Jetpack Compose と Clean Architecture + MVI を採用したマルチモジュール Android プロジェクトです。

## コア機能
- すれ違い検出（BLE）
- 時刻フィルタリングされたタイムライン（すれ違い時刻以前の投稿のみ）
- 投稿（テキスト・画像・動画）
- いいね、検索、プロフィール管理

## リポジトリ構成（抜粋）
```
Spans/
├── :app            # アプリケーションモジュール（MainActivity が AppNavGraph を起動）
├── :presentation   # Compose UI / Navigation / 各画面モジュール
├── :domain         # ドメインモデル、UseCase、Repository インターフェース
├── :data           # リポジトリ実装、Remote/Local データソース
├── :di             # DI (Koin) モジュール（未実装の部分あり）
└── overview        # 仕様書・設計資料
```

## 技術スタック
- Kotlin 2.2.x
- Gradle 8.x
- Jetpack Compose (Compose BOM 2025.x)
- Navigation Compose (2.9.5)
- Kotlinx Serialization, Coroutines, Flow
- Planned: Koin（DI）、Ktor / Supabase（Remote API）、Room（Local DB）

## ナビゲーション（現状）
- 型安全なルート定義を `presentation/common` に置き、`presentation/navigation` に `AppNavGraph`, `AuthNavGraph`, `HubNavGraph` を実装しています。
- `app/src/.../MainActivity.kt` は `AppNavGraph()` を呼び出してナビゲーションを起動します。

## 優先実装／未実装（短期）
- ViewModel と UiState/Intent/Effect（MVI）の実装
- Koin を使った DI モジュールの追加
- Timeline のスケルトン（Composable + ViewModel + FakeRepository）
- BLE / WorkManager の実装は段階的に実施予定

## 簡単なセットアップ（開発環境）
1. JDK 11 をインストール
2. Android SDK & Android Studio を用意
3. プロジェクトのルートで Gradle ラッパーを使用してビルド:

```bash
./gradlew assembleDebug
```

4. Android Studio で `:app` モジュールを実行（もしくはエミュレータ / 実機で起動）

注: ネイティブの BLE 機能やフォアグラウンドサービスは実機でのテストが必要です。

## 開発ルール・推奨事項
- アーキテクチャ: Clean Architecture + MVI
- コードスタイル: Kotlin コミュニティ標準に従う（format/lint を CI で実行）
- テスト: Unit（UseCase / Repository）をまず実装。Compose の UI テストも追加する。

## 貢献ガイド
1. Issue を作成して変更点を共有
2. feature ブランチを切る（例: feature/my-feature）
3. Pull Request を作成。CI（Lint / Unit tests / Build）を通してからレビュー

## 参考資料
- `overview/PROJECT.md` — 仕様書（詳細は overview フォルダを参照してください）

---
