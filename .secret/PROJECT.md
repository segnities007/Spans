# すれ違いSNSアプリ - 完全開発ガイド

## 前提条件

**現在の開発方針**: Android単体での開発  
**将来の展望**: Compose Multiplatform対応を視野に入れた技術選定

---

## 目次

1. [プロジェクト概要](#1-プロジェクト概要)
2. [技術スタック](#2-技術スタック)
3. [システムアーキテクチャ](#3-システムアーキテクチャ)
4. [データベース設計](#4-データベース設計)
5. [API設計](#5-api設計)
6. [機能仕様](#6-機能仕様)
7. [UI/UX設計](#7-uiux設計)
8. [セキュリティ設計](#8-セキュリティ設計)
9. [テスト戦略](#9-テスト戦略)
10. [デプロイメント計画](#10-デプロイメント計画)
11. [運用・保守計画](#11-運用保守計画)

---

## 1. プロジェクト概要

### 1.1 アプリケーションコンセプト

物理的にすれ違った人の投稿のみが閲覧できる、時間制限付きSNSアプリ。3DSのすれ違い通信をモチーフにしたリアルタイム近距離コミュニケーションプラットフォーム。

### 1.2 コア機能

| 機能 | 説明 | 優先度 |
|------|------|--------|
| すれ違い検出 | BLE使用、約10m範囲で自動検出 | 高 |
| 時間制限付き閲覧 | すれ違ったタイミングまでの投稿のみ | 高 |
| 投稿機能 | テキスト・画像・動画（256文字制限） | 高 |
| いいね機能 | 投稿へのリアクション | 中 |
| 検索機能 | ユーザー・投稿内容検索 | 中 |
| プロフィール管理 | 自己紹介・アバター設定 | 中 |

### 1.3 ターゲットユーザー

- **プライマリ**: 都市部在住の20-30代、SNS利用者
- **セカンダリ**: 大学生、社会人コミュニティ参加者
- **利用シーン**: 通勤・通学路、イベント会場、カフェ、コワーキングスペース

---

## 2. 技術スタック

### 2.1 採用技術一覧

#### 基盤技術

| カテゴリ | 技術 | バージョン | KMP対応 |
|---------|------|-----------|---------|
| 開発言語 | Kotlin | 2.1.0+ | ✅ |
| UI | Jetpack Compose | 1.7.0+ | ✅ |
| 非同期処理 | Kotlin Coroutines | 1.9.0+ | ✅ |
| リアクティブ | Kotlin Flow | - | ✅ |

#### 依存性注入

| 技術 | バージョン | KMP対応 |
|------|-----------|---------|
| Koin | 4.1.0+ | ✅ |

#### ネットワーク

| 技術 | バージョン | KMP対応 |
|------|-----------|---------|
| Ktor Client | 3.2.0+ | ✅ |
| Supabase-kt | 3.0.0+ | ✅ |
| kotlinx.serialization | 1.7.0+ | ✅ |

#### ローカルデータベース

| 技術 | バージョン | KMP対応 |
|------|-----------|---------|
| Room | 2.7.0+ | ✅ (Alpha) |

#### 画像読み込み

| 技術 | バージョン | KMP対応 |
|------|-----------|---------|
| Coil3 | 3.0.0+ | ✅ |

#### ナビゲーション

| 技術 | バージョン | KMP対応 |
|------|-----------|---------|
| Navigation Compose | 2.9.0+ | ✅ |

**備考**: Navigation Compose Multiplatformは公式サポート開始済み（2025年）

#### その他共通ライブラリ

| 技術 | バージョン | KMP対応 | 用途 |
|------|-----------|---------|------|
| kotlinx.datetime | 0.7.0+ | ✅ | 日時処理 |
| Napier | 2.7.0+ | ✅ | ログ出力 |

#### Android固有（将来的に代替検討）

| 技術 | 用途 | iOS代替案 |
|------|------|----------|
| Android BLE API | すれ違い検出 | Core Bluetooth |
| WorkManager 2.10.0+ | バックグラウンド処理 | Background Tasks |
| Foreground Service | 常時動作 | Background Modes |
| Google Play Services Auth | OAuth認証 | Compose Auth（Supabase-kt） |

### 2.2 依存関係バージョン管理

**Gradle Version Catalog使用** (`libs.versions.toml`)

```toml
[versions]
kotlin = "2.1.0"
compose = "1.7.5"
koin = "4.1.0"
ktor = "3.2.3"
supabase = "3.0.5"
room = "2.7.0-alpha10"
coil3 = "3.0.4"
navigationCompose = "2.9.0"
kotlinxSerialization = "1.7.3"
kotlinxDatetime = "0.7.1"
napier = "2.7.1"
workManager = "2.10.0"

[libraries]
# Compose
androidx-navigation-compose = { module = "org.jetbrains.androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
# ...その他のライブラリ
```

#### 最小SDKバージョン

- **minSdk**: 26 (Android 8.0)
- **targetSdk**: 34 (Android 14)
- **compileSdk**: 34

---

## 3. システムアーキテクチャ

### 3.1 アーキテクチャパターン

**採用**: Clean Architecture + MVI (Model-View-Intent)

### 3.2 MVI概要

**MVI (Model-View-Intent)** は単方向データフローを実現するアーキテクチャパターン

```
User Action (Intent)
    ↓
ViewModel (Intent処理)
    ↓
UseCase実行
    ↓
Model更新
    ↓
State変更
    ↓
View (State監視・再描画)
```

**MVI vs MVVM**:
- MVVM: 双方向データバインディング可能
- MVI: 完全な単方向データフロー、状態管理が明確

### 3.3 レイヤー構成

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  ┌──────────────────────────────────┐   │
│  │  Compose UI (View)               │   │
│  │  ViewModel (Intent → State)      │   │
│  │  UiState / UiIntent              │   │
│  └────────────────┬─────────────────┘   │
└───────────────────┼─────────────────────┘
                    │
┌───────────────────▼─────────────────────┐
│         Domain Layer                    │
│  ┌──────────────────────────────────┐   │
│  │  UseCases                        │   │
│  │  Domain Models                   │   │
│  │  Repository Interfaces           │   │
│  └────────────────┬─────────────────┘   │
└───────────────────┼─────────────────────┘
                    │
┌───────────────────▼─────────────────────┐
│         Data Layer                      │
│  ┌──────────────────────────────────┐   │
│  │  Repository Implementations      │   │
│  │  Remote (Supabase + Ktor)       │   │
│  │  Local (Room)                    │   │
│  │  BLE Service (Android固有)       │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

### 3.4 MVI実装パターン

#### UiState定義

```kotlin
// UIの状態を表現
sealed interface TimelineUiState {
    object Loading : TimelineUiState
    data class Success(
        val posts: List<Post>,
        val isRefreshing: Boolean = false,
        val hasMore: Boolean = true
    ) : TimelineUiState
    data class Error(val message: String) : TimelineUiState
}
```

#### UiIntent定義

```kotlin
// ユーザーの意図を表現
sealed interface TimelineIntent {
    object LoadTimeline : TimelineIntent
    object RefreshTimeline : TimelineIntent
    object LoadMore : TimelineIntent
    data class LikePost(val postId: String) : TimelineIntent
    data class NavigateToPost(val postId: String) : TimelineIntent
}
```

#### ViewModel設計

```kotlin
class TimelineViewModel(
    private val getTimelineUseCase: GetTimelineUseCase,
    private val likePostUseCase: LikePostUseCase
) : ViewModel() {
    
    // State保持
    private val _uiState = MutableStateFlow<TimelineUiState>(TimelineUiState.Loading)
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()
    
    // Intent処理
    fun onIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.LoadTimeline -> loadTimeline()
            is TimelineIntent.RefreshTimeline -> refreshTimeline()
            is TimelineIntent.LoadMore -> loadMore()
            is TimelineIntent.LikePost -> likePost(intent.postId)
            is TimelineIntent.NavigateToPost -> navigateToPost(intent.postId)
        }
    }
    
    private fun loadTimeline() {
        viewModelScope.launch {
            _uiState.value = TimelineUiState.Loading
            getTimelineUseCase()
                .onSuccess { posts ->
                    _uiState.value = TimelineUiState.Success(posts)
                }
                .onFailure { error ->
                    _uiState.value = TimelineUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
```

#### Compose UI

```kotlin
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    when (val state = uiState) {
        is TimelineUiState.Loading -> LoadingView()
        is TimelineUiState.Success -> TimelineContent(
            posts = state.posts,
            onIntent = viewModel::onIntent
        )
        is TimelineUiState.Error -> ErrorView(state.message)
    }
}

@Composable
private fun TimelineContent(
    posts: List<Post>,
    onIntent: (TimelineIntent) -> Unit
) {
    LazyColumn {
        items(posts) { post ->
            PostCard(
                post = post,
                onLike = { onIntent(TimelineIntent.LikePost(post.id)) },
                onClick = { onIntent(TimelineIntent.NavigateToPost(post.id)) }
            )
        }
    }
}
```

### 3.5 モジュール構成

```
app/
├── presentation/
│   ├── ui/
│   │   ├── auth/          # 認証関連画面
│   │   ├── hub/           # メインハブ
│   │   ├── timeline/      # タイムライン
│   │   ├── post/          # 投稿作成・編集
│   │   ├── search/        # 検索
│   │   ├── profile/       # プロフィール
│   │   ├── settings/      # 設定
│   │   └── component/     # 共通UI
│   ├── mvi/               # MVIクラス群
│   │   ├── intent/        # UiIntent定義
│   │   ├── effect/        # UiEffect定義
│   │   ├── reducer/       # UiReducer定義
│   │   └── state/         # UiState定義
│   ├── navigation/        # Navigation Compose
│   └── theme/             # テーマ・スタイル
│
├── domain/
│   ├── model/             # ドメインモデル
│   ├── repository/        # リポジトリInterface
│   └── usecase/           # ユースケース
│
├── data/
│   ├── repository/        # Repository実装
│   ├── remote/            # API通信（Supabase + Ktor）
│   └── local/             # プラットフォーム固有
│       ├── db/            # ローカルDB（Room）
│       ├── ble/           # BLE実装
│       └── worker/        # WorkManager
│
├── di/                    # Koin DI設定
│   ├── AppModule.kt
│   ├── NetworkModule.kt
│   ├── DatabaseModule.kt
│   └── RepositoryModule.kt
│
└── util/                  # ユーティリティ
```

### 3.6 依存性注入設計（Koin）

#### モジュール分割

| モジュール | 責務 | スコープ |
|-----------|------|---------|
| AppModule | Application依存 | Singleton |
| NetworkModule | ネットワーク関連 | Singleton |
| DatabaseModule | DB関連 | Singleton |
| RepositoryModule | Repository | Singleton |
| ViewModelModule | ViewModel | ViewModel Scope |

### 3.7 Navigation Compose設計

#### ルート定義（Type-Safe Navigation）

```kotlin
// kotlinx.serialization使用
@Serializable
object AuthRoute

@Serializable
object PlazaRoute

@Serializable
object TimelineRoute

@Serializable
data class PostDetailRoute(val postId: String)

@Serializable
data class UserProfileRoute(val userUuid: String)
```

#### NavGraph定義

```kotlin
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PlazaRoute,
        modifier = modifier
    ) {
        composable<AuthRoute> {
            AuthScreen(
                onNavigateToPlaza = { navController.navigate(PlazaRoute) }
            )
        }
        
        composable<PlazaRoute> {
            PlazaScreen(navController)
        }
        
        composable<TimelineRoute> {
            TimelineScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId))
                }
            )
        }
        
        composable<PostDetailRoute> { backStackEntry ->
            val route: PostDetailRoute = backStackEntry.toRoute()
            PostDetailScreen(postId = route.postId)
        }
    }
}
```

### 3.8 エラーハンドリング

#### Result型の採用

```kotlin
sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
}

sealed class AppError {
    data class Network(val message: String) : AppError()
    data class Auth(val message: String) : AppError()
    data class Ble(val message: String) : AppError()
    data class Validation(val field: String, val message: String) : AppError()
    data class Unknown(val throwable: Throwable) : AppError()
}
```

---

## 4. データベース設計

### 4.1 Supabase PostgreSQL設計

#### 4.1.1 Users テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| uuid | VARCHAR | PRIMARY KEY | BLE用UUID |
| google_id | VARCHAR | UNIQUE NOT NULL | Google認証ID |
| nickname | VARCHAR | NOT NULL | ユーザー名（2-20文字） |
| avatar_url | VARCHAR | NULL | プロフィール画像URL |
| bio | TEXT | NULL | 自己紹介（500文字以内） |
| total_encounters | INT | DEFAULT 0 | 総すれ違い数 |
| achievements | TEXT[] | NULL | 獲得実績 |
| is_active | BOOLEAN | DEFAULT TRUE | アカウント有効フラグ |
| created_at | TIMESTAMP | DEFAULT NOW() | 作成日時 |
| updated_at | TIMESTAMP | DEFAULT NOW() | 更新日時 |

**インデックス**:
- `idx_users_google_id` (google_id)
- `idx_users_nickname` (nickname)

#### 4.1.2 Posts テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | 投稿ID |
| author_uuid | VARCHAR | FOREIGN KEY → users | 投稿者UUID |
| content | TEXT | NOT NULL | 投稿内容（1-256文字） |
| media_url | VARCHAR | NULL | メディアURL |
| media_type | VARCHAR | NULL | image/video |
| thumbnail_url | VARCHAR | NULL | サムネイルURL |
| like_count | INT | DEFAULT 0 | いいね数 |
| is_deleted | BOOLEAN | DEFAULT FALSE | 論理削除フラグ |
| created_at | TIMESTAMP | DEFAULT NOW() | 作成日時 |
| updated_at | TIMESTAMP | DEFAULT NOW() | 更新日時 |

**インデックス**:
- `idx_posts_author` (author_uuid)
- `idx_posts_created_at` (created_at DESC)
- `idx_posts_author_created` (author_uuid, created_at DESC)

#### 4.1.3 Likes テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | いいねID |
| user_uuid | VARCHAR | FOREIGN KEY → users | ユーザーUUID |
| post_id | UUID | FOREIGN KEY → posts | 投稿ID |
| created_at | TIMESTAMP | DEFAULT NOW() | 作成日時 |

**制約**: UNIQUE(user_uuid, post_id)

#### 4.1.4 Encounters テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | すれ違いID |
| user_uuid_a | VARCHAR | FOREIGN KEY → users | ユーザーA |
| user_uuid_b | VARCHAR | FOREIGN KEY → users | ユーザーB |
| last_encounter_at | TIMESTAMP | NOT NULL | 最終すれ違い日時 |
| encounter_count | INT | DEFAULT 1 | すれ違い回数 |
| average_rssi | INT | NULL | 平均電波強度 |
| created_at | TIMESTAMP | DEFAULT NOW() | 初回すれ違い日時 |
| updated_at | TIMESTAMP | DEFAULT NOW() | 更新日時 |

**制約**:
- UNIQUE(user_uuid_a, user_uuid_b)
- CHECK(user_uuid_a < user_uuid_b)

#### 4.1.5 Blocked Users テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | ブロックID |
| blocker_uuid | VARCHAR | FOREIGN KEY → users | ブロックしたユーザー |
| blocked_uuid | VARCHAR | FOREIGN KEY → users | ブロックされたユーザー |
| reason | VARCHAR | NULL | ブロック理由 |
| created_at | TIMESTAMP | DEFAULT NOW() | ブロック日時 |

**制約**: UNIQUE(blocker_uuid, blocked_uuid)

#### 4.1.6 Privacy Settings テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| user_uuid | VARCHAR | PRIMARY KEY | ユーザーUUID |
| discovery_enabled | BOOLEAN | DEFAULT TRUE | すれ違い検出有効 |
| profile_visibility | VARCHAR | DEFAULT 'public' | public/encounters_only/private |
| location_sharing | BOOLEAN | DEFAULT FALSE | 位置情報共有 |
| updated_at | TIMESTAMP | DEFAULT NOW() | 更新日時 |

#### 4.1.7 Reports テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | 通報ID |
| reporter_uuid | VARCHAR | FOREIGN KEY → users | 通報者 |
| reported_post_id | UUID | FOREIGN KEY → posts | 通報された投稿 |
| reported_user_uuid | VARCHAR | FOREIGN KEY → users | 通報されたユーザー |
| reason | VARCHAR | NOT NULL | 通報理由 |
| description | TEXT | NULL | 詳細説明 |
| status | VARCHAR | DEFAULT 'pending' | pending/reviewing/resolved/dismissed |
| created_at | TIMESTAMP | DEFAULT NOW() | 通報日時 |
| updated_at | TIMESTAMP | DEFAULT NOW() | 更新日時 |

#### 4.1.8 Analytics Events テーブル

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | UUID | PRIMARY KEY | イベントID |
| user_uuid | VARCHAR | FOREIGN KEY → users | ユーザーUUID |
| event_type | VARCHAR | NOT NULL | イベント種別 |
| event_data | JSONB | NULL | イベントデータ |
| created_at | TIMESTAMP | DEFAULT NOW() | 発生日時 |

#### 4.1.9 トリガー設定

**updated_at自動更新**: users, posts, encounters, privacy_settings, reports

**like_count自動更新**: likesテーブルのINSERT/DELETE時

**total_encounters自動更新**: encountersテーブルのINSERT時

#### 4.1.10 Row Level Security (RLS)

**Users**:
- SELECT: 全ユーザー閲覧可能
- UPDATE: 自分のプロフィールのみ

**Posts**:
- SELECT: is_deleted=FALSEのみ
- INSERT/UPDATE/DELETE: 自分の投稿のみ

**Likes**:
- SELECT: 全ユーザー閲覧可能
- INSERT: 認証済みユーザー
- DELETE: 自分のいいねのみ

### 4.2 Room Database設計（ローカル）

#### 4.2.1 Encounters エンティティ

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| uuid | String | PRIMARY KEY | すれ違い相手UUID |
| lastSeen | Long | NOT NULL | 最終すれ違いタイムスタンプ |
| encounterDate | String | NOT NULL | すれ違い日付 |
| encounterCount | Int | NOT NULL | すれ違い回数 |
| rssiValues | String | NOT NULL | RSSI値履歴（JSON） |
| averageRssi | Int | NOT NULL | 平均RSSI |

#### 4.2.2 Post Cache エンティティ

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| id | String | PRIMARY KEY | 投稿ID |
| authorUuid | String | NOT NULL | 投稿者UUID |
| authorNickname | String | NOT NULL | 投稿者名 |
| authorAvatar | String | NULL | 投稿者アバター |
| content | String | NOT NULL | 投稿内容 |
| mediaUrl | String | NULL | メディアURL |
| mediaType | String | NULL | メディアタイプ |
| likeCount | Int | NOT NULL | いいね数 |
| isLikedByMe | Boolean | NOT NULL | 自分のいいね状態 |
| createdAt | Long | NOT NULL | 投稿日時 |
| cachedAt | Long | NOT NULL | キャッシュ日時 |

#### 4.2.3 User Cache エンティティ

| カラム | 型 | 制約 | 説明 |
|--------|----|----|------|
| uuid | String | PRIMARY KEY | ユーザーUUID |
| nickname | String | NOT NULL | ニックネーム |
| avatarUrl | String | NULL | アバターURL |
| bio | String | NULL | 自己紹介 |
| totalEncounters | Int | NOT NULL | 総すれ違い数 |
| cachedAt | Long | NOT NULL | キャッシュ日時 |

### 4.3 データ同期戦略

#### 4.3.1 ローカル → サーバー同期

**すれ違い情報同期**:
- タイミング: 5分間隔、WorkManager
- バッチサイズ: 最大100件/リクエスト
- リトライ: 失敗時3回まで

**投稿データ同期**:
- タイミング: 投稿作成直後
- オフライン対応: ローカル保存後、オンライン復帰時に同期

#### 4.3.2 サーバー → ローカル同期

**投稿キャッシュ更新**:
- タイミング: タイムライン表示時
- TTL: 5分間
- 最大件数: 500件

**ユーザーキャッシュ更新**:
- タイミング: プロフィール表示時
- TTL: 1時間

---

## 5. API設計

### 5.1 エンドポイント一覧

| メソッド | エンドポイント | 説明 | 認証 |
|---------|---------------|------|------|
| POST | /auth/google | Google OAuth認証 | 不要 |
| GET | /api/profile | 自分のプロフィール取得 | 必要 |
| PUT | /api/profile | プロフィール更新 | 必要 |
| POST | /api/posts | 投稿作成 | 必要 |
| PUT | /api/posts/{id} | 投稿編集 | 必要 |
| DELETE | /api/posts/{id} | 投稿削除 | 必要 |
| POST | /api/timeline | タイムライン取得 | 必要 |
| POST | /api/likes | いいね追加 | 必要 |
| DELETE | /api/likes/{post_id} | いいね削除 | 必要 |
| GET | /api/search/users | ユーザー検索 | 必要 |
| GET | /api/search/posts | 投稿検索 | 必要 |
| GET | /api/users/{uuid} | ユーザー情報取得 | 必要 |
| POST | /api/encounters/sync | すれ違い情報同期 | 必要 |
| POST | /api/reports | 通報 | 必要 |
| POST | /api/blocks | ブロック | 必要 |
| DELETE | /api/blocks/{uuid} | ブロック解除 | 必要 |

### 5.2 認証フロー

#### Google OAuth認証

**リクエスト**:
```json
POST /auth/google
{
  "idToken": "Google IDトークン"
}
```

**レスポンス**:
```json
{
  "user": {
    "uuid": "生成されたUUID",
    "googleId": "Google ID",
    "nickname": "ユーザー名",
    "avatarUrl": "プロフィール画像URL"
  },
  "session": {
    "accessToken": "JWT Access Token",
    "refreshToken": "Refresh Token",
    "expiresAt": 1234567890
  }
}
```

### 5.3 タイムライン取得API

**リクエスト**:
```json
POST /api/timeline
{
  "encounterUuids": ["uuid1", "uuid2"],
  "encounterTimestamps": {
    "uuid1": 1234567890,
    "uuid2": 1234567891
  },
  "limit": 20,
  "offset": 0
}
```

**レスポンス**:
```json
{
  "posts": [
    {
      "id": "post_id",
      "authorUuid": "uuid1",
      "authorNickname": "ニックネーム",
      "authorAvatar": "avatar_url",
      "content": "投稿内容",
      "mediaUrl": "media_url",
      "mediaType": "image",
      "thumbnailUrl": "thumb_url",
      "likeCount": 5,
      "isLikedByMe": true,
      "createdAt": 1234567800
    }
  ],
  "hasMore": true
}
```

### 5.4 エラーレスポンス

**共通フォーマット**:
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "エラーメッセージ",
    "details": {}
  }
}
```

**エラーコード**:

| ステータス | コード | 説明 |
|-----------|--------|------|
| 400 | INVALID_REQUEST | リクエスト不正 |
| 401 | UNAUTHORIZED | 認証失敗 |
| 403 | FORBIDDEN | 権限不足 |
| 404 | NOT_FOUND | リソース未検出 |
| 409 | CONFLICT | データ競合 |
| 413 | PAYLOAD_TOO_LARGE | データサイズ超過 |
| 429 | RATE_LIMIT_EXCEEDED | レート制限超過 |
| 500 | INTERNAL_ERROR | サーバーエラー |

### 5.5 レート制限

| エンドポイント | 制限 |
|---------------|------|
| /auth/* | 10回/分 |
| /api/posts (POST) | 20回/時間 |
| /api/likes/* | 30回/分 |
| /api/timeline | 60回/分 |
| その他 | 60回/分 |

---

## 6. 機能仕様

### 6.1 すれ違い検出機能

#### 6.1.1 BLE検出仕様

**検出範囲**: 約10m（RSSI -90dBm以上）

**動作モード**:

| モード | スキャン間隔 | スキャン時間 | 条件 |
|--------|------------|------------|------|
| フォアグラウンド | 1分 | 10秒 | アプリ起動中 |
| バックグラウンド | 5分 | 10秒 | アプリバックグラウンド |
| 夜間モード | 10分 | 10秒 | 23:00-6:00 |

**RSSI判定**:
- -60dBm以上: 近距離（high quality）
- -75dBm以上: 中距離（medium quality）
- -90dBm以上: 遠距離（low quality）
- -90dBm未満: 検出対象外

**UUID交換方式**:
- BLE Advertising: 自分のUUIDを発信
- BLE Scanning: 周囲のUUIDを受信
- Service UUID: 固定値（アプリ識別用）
- Manufacturer Data: ユーザーUUID格納

#### 6.1.2 すれ違い判定ロジック

**新規すれ違い**:
1. UUIDを初めて検出
2. ローカルDBに保存
3. サーバーに同期（5分以内）

**既存すれ違い更新**:
1. 既存UUIDを再検出
2. lastSeenタイムスタンプ更新
3. encounterCount加算
4. RSSI履歴更新（最大10件保持）

**重複検出防止**: 同じUUIDの5分以内の再検出は無視

#### 6.1.3 バックグラウンド動作

**Foreground Service使用**:
- 常駐通知表示（低優先度）
- Doze mode対応
- Battery Optimization除外要求

**権限要件**:
- `ACCESS_FINE_LOCATION`: BLE検出に必須
- `BLUETOOTH_SCAN`: バックグラウンドスキャン
- `BLUETOOTH_ADVERTISE`: UUID発信
- `POST_NOTIFICATIONS`: 通知表示
- `FOREGROUND_SERVICE`: フォアグラウンドサービス

### 6.2 投稿機能

#### 6.2.1 投稿作成

**テキスト投稿**:
- 文字数: 1-256文字
- 制限: なし（無制限投稿可能）

**メディア投稿**:
- 対応形式: JPEG, PNG, GIF（画像）/ MP4, MOV（動画）
- 最大サイズ: 画像10MB、動画50MB
- 容量制限: Free 100MiB/月、Pro 無制限

**画像処理**:
- 最大解像度: 1920x1920px
- JPEG品質: 85%
- サムネイル: 300x300px自動生成
- EXIF情報: 位置情報削除

**動画処理**:
- 最大長: 60秒
- 解像度: 1080p以下
- コーデック: H.264
- サムネイル: 初フレームから生成

#### 6.2.2 投稿編集・削除

**編集**:
- 対象: テキストのみ
- メディア: 変更不可
- 表示: "編集済み"バッジ

**削除**:
- 方式: 論理削除（is_deleted=true）
- 表示: 即座にタイムラインから消失

### 6.3 タイムライン機能

#### 6.3.1 表示仕様

**表示順**: 投稿日時降順（新しい順）

**表示内容**:
- 投稿者アイコン・ニックネーム
- 投稿内容（テキスト・メディア）
- 投稿日時（相対時刻表示）
- いいね数・自分のいいね状態

#### 6.3.2 無限スクロール

**ページネーション**:
- 初回読み込み: 20件
- 追加読み込み: 20件ずつ
- トリガー: 残り5件でロード開始

**キャッシュ戦略**:
- メモリキャッシュ: 最新100件
- ディスクキャッシュ: 最新500件
- TTL: 5分間

#### 6.3.3 時刻フィルタリング

**閲覧制限ロジック**:
- すれ違ったユーザーの投稿のみ
- すれ違い時刻以前の投稿のみ
- 再度すれ違えば最新投稿取得可能

### 6.4 いいね機能

#### 6.4.1 いいね操作

**追加**: タップでいいね追加、楽観的UI更新

**削除**: 再タップでいいね削除、楽観的UI更新

**レート制限**: 1分間30回まで

#### 6.4.2 表示仕様

**いいね数表示**:
- 0件: 表示なし
- 1-999件: そのまま表示
- 1,000件以上: "1.2k"形式

### 6.5 検索機能

#### 6.5.1 ユーザー検索

**検索対象**: すれ違ったユーザーのみ

**検索条件**: ニックネーム部分一致

**表示内容**: アイコン、ニックネーム、自己紹介、すれ違い回数

#### 6.5.2 投稿検索

**検索対象**: すれ違ったユーザーの投稿

**検索条件**: 投稿内容全文検索、時刻フィルタリング適用

#### 6.5.3 検索UI

**リアルタイム検索**: 入力中に結果更新（デバウンス300ms）

### 6.6 プロフィール機能

#### 6.6.1 自分のプロフィール

**表示項目**:
- アバター画像
- ニックネーム
- 自己紹介
- 総すれ違い数
- 総投稿数

**編集項目**:
- アバター画像（最大5MB）
- ニックネーム（2-20文字）
- 自己紹介（最大500文字）

#### 6.6.2 他ユーザープロフィール

**表示項目**:
- 基本情報
- すれ違い回数
- 最終すれ違い日時
- 投稿一覧（時刻フィルタリング適用）

**操作**: ブロック、通報

### 6.7 設定機能

#### 6.7.1 プライバシー設定

**すれ違い検出**: ON/OFF切替

**プロフィール公開範囲**:
- public: 全ユーザー
- encounters_only: すれ違ったユーザーのみ
- private: 非公開

#### 6.7.2 通知設定

**すれ違い通知**: 1日1回集約通知（19:00）、ON/OFF切替

**サイレント時間**: 22:00-8:00は通知OFF

#### 6.7.3 アカウント設定

**アカウント削除**: 全データ削除、30日間猶予期間

**ログアウト**: セッション削除、ローカルキャッシュクリア

### 6.8 通報・ブロック機能

#### 6.8.1 通報機能

**通報対象**: 不適切な投稿、不適切なユーザー

**通報理由**: スパム、誹謗中傷、性的コンテンツ、暴力的コンテンツ、その他

#### 6.8.2 ブロック機能

**ブロック効果**:
- ブロックしたユーザーの投稿非表示
- 相互にすれ違い情報非表示
- 検索結果から除外

---

## 7. UI/UX設計

### 7.1 画面構成

```
Auth Flow
├── SignIn Screen (Google OAuth)
└── SignUp Screen (初回プロフィール設定)

Main Flow
├── Plaza Screen (メインハブ)
├── Timeline Screen
├── Post Create Screen
├── Post Detail Screen
├── Search Screen
├── User Profile Screen
└── Settings Screen

Navigation: Bottom Navigation Bar
├── Home (Plaza)
├── Timeline
├── Search
├── Profile
└── Settings
```

### 7.2 デザインシステム

#### 7.2.1 カラーパレット

**Material Design 3準拠**:
- Primary: Dynamic Color対応
- Surface: 背景色
- On-Surface: テキスト色
- Error: エラー表示色

**ダークモード対応**: システム設定に追従

#### 7.2.2 タイポグラフィ

| 要素 | フォントサイズ | ウェイト |
|------|--------------|---------|
| Display | 32sp | Bold |
| Headline | 24sp | SemiBold |
| Title | 20sp | Medium |
| Body | 16sp | Regular |
| Label | 14sp | Medium |
| Caption | 12sp | Regular |

#### 7.2.3 間隔・サイズ

**基本間隔**: 4の倍数（4, 8, 12, 16, 24, 32, 48, 64）

**コンポーネントサイズ**:
- アイコン: 24dp標準、48dpタッチターゲット
- ボタン高さ: 48dp
- カード角丸: 12dp
- 画面パディング: 16dp

### 7.3 主要画面仕様

#### 7.3.1 Plaza Screen（メインハブ）

**レイアウト**:
- ヘッダー: アプリロゴ
- 今日の統計カード
    - 今日のすれ違い数
    - 累計すれ違い数
- クイックアクション
    - タイムラインを見る
    - 投稿を作成
    - ユーザーを探す
- 最近の活動（3-5件）
- Bottom Navigation Bar

#### 7.3.2 Timeline Screen

**レイアウト**:
- ヘッダー: タイトル、更新ボタン
- Pull-to-Refresh対応
- 投稿カード（Twitter風）
- 無限スクロール
- FloatingActionButton（投稿作成）

#### 7.3.3 Post Create Screen

**レイアウト**:
- トップバー: キャンセル・投稿ボタン
- テキスト入力（複数行）
- 文字数カウンター（256文字制限）
- メディア選択ボタン
- メディアプレビュー

#### 7.3.4 Search Screen

**レイアウト**:
- 検索バー（トップ固定）
- タブ切替: ユーザー / 投稿
- 検索結果リスト
- 検索履歴表示

#### 7.3.5 User Profile Screen

**レイアウト**:
- ヘッダー画像（背景）
- アバター（中央大）
- ニックネーム、自己紹介
- 統計情報
- タブ: 投稿 / 実績
- 投稿リスト

#### 7.3.6 Settings Screen

**レイアウト**:
- セクション分け
    - プライバシー
    - 通知
    - アカウント
    - アプリ情報
- リスト形式

### 7.4 インタラクション設計

#### 7.4.1 アニメーション

**画面遷移**: Slide（300ms）

**要素出現**: Fade In（200ms）

**いいねボタン**: Scale + Color変化（150ms）

#### 7.4.2 フィードバック

**タップ**: Ripple効果

**エラー**: Snackbar表示（4秒）

**成功**: Snackbar表示（2秒）

**ローディング**: CircularProgressIndicator

---

## 8. セキュリティ設計

### 8.1 認証・認可

**認証方式**: Google OAuth 2.0 + Supabase Auth

**トークン管理**:
- Access Token: JWT、有効期限1時間
- Refresh Token: 有効期限30日
- 自動更新: Access Token期限切れ前
- 保存: ローカルに暗号化保存

### 8.2 データ暗号化

**通信**: HTTPS/TLS 1.3

**ローカルストレージ**: AndroidKeyStoreで鍵管理

**UUID生成**: SHA-256（Google ID + ソルト + タイムスタンプ）

### 8.3 入力値検証

**クライアント側**:
- 文字数制限チェック
- 不正文字フィルタリング
- ファイル形式・サイズ検証

**サーバー側**:
- SQLインジェクション対策
- XSS対策
- CSRF対策

### 8.4 プライバシー保護

**UUID匿名性**: Google IDから直接UUID推測不可能

**位置情報**: GPS使用しない、BLEのみ

**メディアファイル**: EXIF位置情報自動削除

---

## 9. テスト戦略

### 9.1 テストレベル

#### 9.1.1 Unit Test（目標カバレッジ: 70%）

**対象**:
- Domain Layer（全UseCase）
- Data Layer（Repository実装）
- Utility関数

**フレームワーク**:
- JUnit 4
- Kotlinx Coroutines Test
- Turbine（Flow テスト）
- MockK（モック）
- Koin Test

#### 9.1.2 Integration Test

**対象**:
- Repository + DataSource連携
- Supabase API通信
- Room Database操作

**環境**: Supabase Test Project

#### 9.1.3 UI Test

**対象**:
- 主要画面の表示確認
- ユーザー操作フロー
- 状態遷移

**フレームワーク**: Compose Test API

#### 9.1.4 E2E Test

**対象**:
- ユーザー登録～投稿～閲覧の全フロー
- すれ違い検出～タイムライン更新
- オフライン/オンライン切り替え

**実施方法**: 複数実機での手動テスト

### 9.2 βテスト計画

#### 期間・参加者

- **期間**: 2週間
- **参加者**: 10-20名
- **配信方法**: Google Play Internal Testing

#### テスト観点

1. **機能テスト**: 全機能の動作確認
2. **ユーザビリティ**: UI/UXフィードバック
3. **パフォーマンス**: バッテリー消費、動作速度
4. **実環境テスト**: 実際の外出先でのすれ違い検出

---

## 10. デプロイメント計画

### 10.1 リリース戦略

#### 段階的リリース

```
Internal Testing (1週間)
    ↓
Closed Testing / βテスト (2週間)
    ↓
Open Testing (1週間)
    ↓
Production Release (段階的公開)
    - Day 1: 10%
    - Day 3: 25%
    - Day 7: 50%
    - Day 14: 100%
```

### 10.2 Google Play Console設定

#### アプリ情報

| 項目 | 内容 |
|------|------|
| アプリ名 | すれ違いSNS（仮称） |
| カテゴリ | ソーシャルネットワーキング |
| 対象年齢 | 12歳以上 |
| 価格 | 無料（アプリ内課金あり） |

#### ストアリスティング

**短い説明（80文字）**:
```
すれ違った人の投稿だけ見られる新しいSNS。リアルな出会いをオンラインで。
```

**スクリーンショット**: 5枚必須（1080x1920px）

#### プライバシーポリシー

**必須記載事項**:
- 収集する情報
- 利用目的
- 第三者提供の有無
- データ保存期間
- ユーザーの権利

### 10.3 CI/CD設定

#### GitHub Actions

**ワークフロー**:

1. **Pull Request時**: Lint検査、Unit Test、ビルド確認
2. **main/developブランチpush時**: 上記すべて + APKビルド
3. **Releaseタグpush時**: Release APK/AABビルド + 署名

### 10.4 バージョン管理

#### セマンティックバージョニング

**形式**: Major.Minor.Patch (例: 1.2.3)

- **Major**: 破壊的変更
- **Minor**: 新機能追加
- **Patch**: バグ修正

---

## 11. 運用・保守計画

### 11.1 監視・分析

#### 11.1.1 Firebase Analytics

**トラッキングイベント**:

| イベント | 目的 |
|---------|------|
| app_open | アプリ起動数 |
| encounter_detected | すれ違い検出 |
| post_created | 投稿作成 |
| post_liked | いいね |
| user_searched | 検索実行 |
| subscription_purchased | Pro購入 |

#### 11.1.2 Firebase Crashlytics

**監視項目**:
- クラッシュレポート
- 非致命的エラー
- カスタムログ

#### 11.1.3 Supabase監視

**監視項目**:
- Database使用量
- Storage使用量
- API呼び出し回数
- Edge Functions実行回数

### 11.2 保守計画

#### 定期メンテナンス

**デイリー**: クラッシュレポート確認、エラーログ確認

**ウィークリー**: KPI レビュー、パフォーマンス分析

**マンスリー**: 総合分析、機能改善計画、セキュリティアップデート

#### アップデート計画

**緊急修正（Hotfix）**: 即座

**小規模アップデート**: 2週間ごと

**大規模アップデート**: 月1回

#### サポート体制

**問い合わせ窓口**: メール、回答目標3営業日以内

**FAQ作成**: アプリ内ヘルプ画面

### 11.3 コスト管理

#### 初期段階（~1,000ユーザー）

| 項目 | 月額コスト |
|------|-----------|
| Supabase | $0（無料枠） |
| Firebase | $0（無料枠） |
| Google Play | $0（初回$25のみ） |
| ドメイン | 約100円 |
| **合計** | **約100円** |

#### 成長段階（1,000~10,000ユーザー）

| 項目 | 月額コスト |
|------|-----------|
| Supabase Pro | $25 |
| Firebase Blaze | $0~$50 |
| その他 | $10 |
| **合計** | **$35~$85** |

### 11.4 リスク管理

#### 技術リスク

| リスク | 影響度 | 対策 |
|--------|--------|------|
| BLE検出精度低下 | 高 | RSSI閾値調整 |
| バッテリー消費大 | 高 | スキャン間隔最適化 |
| Supabase障害 | 中 | ローカルキャッシュ |

#### ビジネスリスク

| リスク | 影響度 | 対策 |
|--------|--------|------|
| ユーザー獲得困難 | 高 | マーケティング強化 |
| すれ違い機会少 | 高 | イベント施策 |
| Pro転換率低迷 | 中 | 付加価値向上 |

#### 法的リスク

| リスク | 影響度 | 対策 |
|--------|--------|------|
| プライバシー問題 | 高 | 個人情報保護法遵守 |
| 不適切投稿 | 中 | 通報機能、モデレーション |

---

## 付録

### A. 用語集

| 用語 | 説明 |
|------|------|
| BLE | Bluetooth Low Energy。低消費電力Bluetooth |
| UUID | Universally Unique Identifier。ユーザー識別子 |
| RSSI | Received Signal Strength Indicator。電波強度 |
| MVI | Model-View-Intent。単方向データフローパターン |
| すれ違い | BLEで検出された他のユーザーとの物理的接近 |
| 時刻フィルタリング | すれ違い時刻以前の投稿のみ表示する機能 |

### B. 参考資料

**技術ドキュメント**:
- Supabase Documentation
- Kotlin Multiplatform Documentation
- Jetpack Compose Documentation
- Android BLE Guide
- Koin Documentation
- Room Documentation
- Navigation Compose Documentation

### C. チェックリスト

#### 開発開始前

- [ ] Supabaseプロジェクト作成
- [ ] Google Cloud Platform設定
- [ ] Google Play Console登録
- [ ] GitHubリポジトリ作成
- [ ] 開発環境構築

#### リリース前

- [ ] 全機能テスト完了
- [ ] パフォーマンステスト完了
- [ ] セキュリティチェック完了
- [ ] プライバシーポリシー作成
- [ ] 利用規約作成
- [ ] ストアリスティング完成

#### リリース後

- [ ] 監視ダッシュボード確認
- [ ] ユーザーフィードバック収集
- [ ] サポート体制稼働

---

## まとめ

本仕様書は、Androidアプリとして「すれ違いSNS」を開発するための完全ガイドです。

**重要なポイント**:

1. **現在はAndroid専用開発**: MVP完成に集中
2. **Compose Multiplatform対応技術選定**: 将来のiOS展開を視野
3. **Clean Architecture + MVI**: 明確な単方向データフロー
4. **Navigation Compose使用**: 公式KMP対応ナビゲーション
5. **ユーザープライバシー重視**: GPS不使用、BLEのみ