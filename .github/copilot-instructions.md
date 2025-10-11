# GitHub Copilot Instructions for Spans Project

すれ違いSNS Android アプリケーションの開発ガイドライン

## アーキテクチャ

- **Clean Architecture + MVI パターン**
- **マルチモジュール構成**: presentation, domain, data層を分離
- **単方向データフロー**: Intent → ViewModel → Reducer → State → View → Effect

## コーディング原則

### 1. 言語とコメント
- **日本語**でコメント、エラーメッセージ、KDocを記述
- 自明なコードには不要なKDocを書かない（コードが自己説明的であるべき）

### 2. エラーハンドリング
- **Kotlin標準のResult<T>型**を使用（カスタムResult型は作らない）
- UseCaseとRepositoryは`Result<T>`を返す
- 例外は`DomainException`階層を使用：
  - `NetworkError`: ネットワークエラー
  - `AuthError`: 認証エラー
  - `ValidationError`: バリデーションエラー
  - `BleError`: BLE関連エラー
- **早期リターンパターン**を使用してネストを避ける

### 3. MVI実装ルール

#### UiState
- sealed interfaceで定義
- 不変データクラス（data class, immutable properties）
- 状態はStateで管理（フィールドはinlineで保持）

#### UiIntent
- ユーザーアクションを表現するsealed interface
- 各Intentは具体的な操作を示す（例: `NicknameChanged`, `SignUpClicked`）

#### UiEffect
- 一度きりのイベント（ナビゲーション、Snackbar表示など）
- StateFlowではなくSharedFlowで管理

#### Reducer
- **純粋関数**として実装（副作用なし）
- Intent + State → 新しいState
- バリデーションロジックはReducer内にprivate関数として実装（共通化が必要になるまで切り出さない）

#### ViewModel
- UseCaseの実行とEffect送信を担当
- Reducerを使って状態遷移
- Result<T>から直接エラー処理（不要な中間型変換を避ける）

### 4. SOLID原則

#### S - Single Responsibility Principle (単一責任の原則)
- 各クラス/関数は1つの責務のみ
- ViewModelは状態管理、UseCaseはビジネスロジック、Repositoryはデータアクセス
- 1つのクラスを変更する理由は1つだけであるべき

#### O - Open/Closed Principle (開放閉鎖の原則)
- 拡張に対して開いている、修正に対して閉じている
- sealed interfaceで拡張可能な型を定義
- 既存コードを変更せず、新しい機能を追加できる設計

#### L - Liskov Substitution Principle (リスコフの置換原則)
- サブタイプは基底型と置換可能であるべき
- interfaceの実装は契約を守る
- 例: `Repository`実装は全て同じ契約に従う

#### I - Interface Segregation Principle (インターフェース分離の原則)
- クライアントは使用しないメソッドに依存すべきでない
- 大きなinterfaceは小さく分割
- 例: `UserRepository`と`PostRepository`を分離

#### D - Dependency Inversion Principle (依存性逆転の原則)
- 上位レイヤーは下位レイヤーに依存しない
- 両方とも抽象（interface）に依存する
- ViewModelはRepository interfaceに依存（実装には依存しない）

### 5. その他の重要な設計原則

#### KISS (Keep It Simple, Stupid)
- シンプルな実装を優先
- 過度な抽象化を避ける
- 複雑さは必要な時だけ導入

#### YAGNI (You Aren't Gonna Need It)
- 必要になるまで実装しない
- 将来使うかもしれない機能は実装しない
- 共通化は行うようにする。工数はAIで削減できるので

#### DRY (Don't Repeat Yourself)
- **意味のある重複**のみを除去
- 偶然の重複は放置（2回目の使用で初めて共通化を検討）
- ビジネスロジックの重複は避けるが、構造の類似は許容

#### Composition Over Inheritance (合成優先原則)
- 継承よりも合成を使用
- 共通処理はヘルパー関数やUtilityクラスで提供
- is-a関係よりhas-a関係を優先

#### Law of Demeter (デメテルの法則)
- 最小知識の原則: 直接知っている相手としか対話しない
- `object.getA().getB().getC()`のようなチェーンを避ける
- 例: `repository.getData()`（内部構造を隠蔽）

#### Fail Fast (早期失敗)
- エラーは早期に検出して報告
- 不正な状態を放置しない
- 早期リターンでネストを減らす

#### Separation of Concerns (関心の分離)
- 各モジュール/クラスは独立した関心事を持つ
- UIロジックとビジネスロジックを分離
- データ取得とデータ変換を分離

### 6. Compose ベストプラクティス
- **状態ホイスティング**: コンポーネントは状態を引数で受け取る
- **副作用の分離**: LaunchedEffectで副作用を管理
- **再コンポーズ最適化**: remember, key(), derivedStateOfを活用

### 6. コードスタイル
- **早期リターン**でネストを減らす
- **null安全**を徹底（`?.`, `?:`, `!!`の適切な使用）
- **型推論**を活用（冗長な型宣言を避ける）
- **分割代入**で可読性向上（Pair, Tripleなど）

## 禁止事項

❌ カスタムResult型の作成（Kotlin標準Result<T>を使用）  
❌ 過度な抽象化（必要になるまで抽象化しない）  
❌ 不要なKDoc（自明なコードへのコメント）  
❌ ViewModelでのビジネスロジック実装（UseCaseに委譲）  
❌ Reducerでの副作用（純粋関数として実装）  
❌ 早すぎる共通化（2回目の使用まで待つ）

## ファイル参照

詳細なアーキテクチャ設計は以下を参照：
- `/overview/AGENTS.md`: 詳細な開発ガイド（1295行）
- `/overview/PROJECT.md`: プロジェクト構造

## 依存関係

- **UI**: Jetpack Compose
- **DI**: Koin
- **Database**: Room
- **Network**: Ktor + Supabase
- **Coroutines**: kotlinx.coroutines
- **BLE**: Android BLE API
