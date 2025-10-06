# すれ違い SNS アプリ (Spans) - 完全開発ガイド

**最終更新日**: 2025 年 10 月 6 日  
**プロジェクト名**: Spans  
**現在のブランチ**: feature/init

## 前提条件

**現在の開発方針**: Android 単体での開発  
**将来の展望**: Compose Multiplatform 対応を視野に入れた技術選定  
**開発段階**: 初期構築フェーズ（モジュール構成完了、実装開始前）

---

## 目次

1. [プロジェクト概要](#1-プロジェクト概要)
2. [現在の実装状況](#2-現在の実装状況)
3. [技術スタック](#3-技術スタック)
4. [システムアーキテクチャ](#4-システムアーキテクチャ)
5. [データベース設計](#5-データベース設計)
6. [API 設計](#6-api設計)
7. [機能仕様](#7-機能仕様)
8. [UI/UX 設計](#8-uiux設計)
9. [セキュリティ設計](#9-セキュリティ設計)
10. [テスト戦略](#10-テスト戦略)
11. [デプロイメント計画](#11-デプロイメント計画)
12. [運用・保守計画](#12-運用保守計画)

---

## 1. プロジェクト概要

### 1.1 アプリケーションコンセプト

物理的にすれ違った人の投稿のみが閲覧できる、時間制限付き SNS アプリ。3DS のすれ違い通信をモチーフにしたリアルタイム近距離コミュニケーションプラットフォーム。

### 1.2 コア機能

| 機能             | 説明                                 | 優先度 |
| ---------------- | ------------------------------------ | ------ |
| すれ違い検出     | BLE 使用、約 10m 範囲で自動検出      | 高     |
| 時間制限付き閲覧 | すれ違ったタイミングまでの投稿のみ   | 高     |
| 投稿機能         | テキスト・画像・動画（256 文字制限） | 高     |
| いいね機能       | 投稿へのリアクション                 | 中     |
| 検索機能         | ユーザー・投稿内容検索               | 中     |
| プロフィール管理 | 自己紹介・アバター設定               | 中     |

### 1.3 ターゲットユーザー

- **プライマリ**: 都市部在住の 20-30 代、SNS 利用者
- **セカンダリ**: 大学生、社会人コミュニティ参加者
- **利用シーン**: 通勤・通学路、イベント会場、カフェ、コワーキングスペース

---

## 2. 現在の実装状況

### 2.1 プロジェクト構造

**リポジトリ情報**:

- **リポジトリ名**: Spans
- **オーナー**: segnities007

### 2.2 モジュール構成（実装済み）

以下のモジュールがすでに作成されています：

```
Spans/
├── :app                                    # アプリケーションモジュール ✅
│   └── Navigation.kt (初期実装)
│
├── :presentation
│   ├── :ui
│   │   ├── :auth                          # 認証画面モジュール ✅
│   │   ├── :signin                        # サインイン画面モジュール ✅
│   │   ├── :signup                        # サインアップ画面モジュール ✅
│   │   ├── :hub                           # メインハブモジュール ✅
│   │   ├── :timeline                      # タイムラインモジュール ✅
│   │   ├── :post                          # 投稿モジュール ✅
│   │   ├── :search                        # 検索モジュール ✅
│   │   ├── :profile                       # プロフィールモジュール ✅
│   │   ├── :settings                      # 設定モジュール ✅
│   │   ├── :component                     # 共通UIモジュール ✅
│   │   └── :tutorial                      # チュートリアルモジュール ✅
│   │
│   └── :navigation                        # ナビゲーションモジュール ✅
│       ├── AppNavRoute.kt
│       ├── AppNavGraph.kt
│       ├── auth/AuthNavRoute.kt
│       ├── auth/AuthNavGraph.kt
│       ├── hub/HubNavRoute.kt
│       └── hub/HubNavGraph.kt
│
├── :domain
│   ├── :model                             # ドメインモデルモジュール ✅
│   ├── :repository                        # リポジトリインターフェースモジュール ✅
│   └── :usecase                           # ユースケースモジュール ✅
│
├── :data
│   ├── :repository                        # リポジトリ実装モジュール ✅
│   ├── :remote                            # API通信モジュール ✅
│   └── :local
│       └── :db                            # Roomデータベースモジュール ✅
│
├── :di                                    # Koin DIモジュール ✅
└── :util                                  # ユーティリティモジュール ✅
```

### 2.3 実装済みコンポーネント

**Navigation 構造** (Type-Safe Navigation):

```kotlin
// AppNavRoute (メインルート)
sealed interface AppNavRoute {
    data class Auth(val route: AuthNavRoute? = null) : AppNavRoute
    data class Hub(val route: HubNavRoute? = null) : AppNavRoute
}

// AuthNavRoute (認証フロー)
sealed interface AuthNavRoute {
    data object SignIn : AuthNavRoute
    data object SignUp : AuthNavRoute
}

// HubNavRoute (メインアプリフロー)
sealed interface HubNavRoute {
    data object Plaza : HubNavRoute
    data object Timeline : HubNavRoute
    data object Search : HubNavRoute
    data object Profile : HubNavRoute
    data object Settings : HubNavRoute
}
```

**app/Navigation.kt** (初期実装):

- NavHostController の設定
- TopBar、BottomBar、FAB の動的切り替え機構
- 基本的な画面遷移フレームワーク

### 2.4 未実装部分

以下のコンポーネントは今後実装予定です：

**Presentation Layer**:

- [ ] 各 UI 画面の Composable 実装（auth, hub, timeline, post, search, profile, settings, tutorial）
- [ ] ViewModel の実装
- [ ] UiState/UiIntent/UiEffect 定義
- [ ] Reducer 実装
- [ ] 共通 UI コンポーネント（component）

**Domain Layer**:

- [ ] ドメインモデルの定義
- [ ] Repository Interface の定義
- [ ] UseCase の実装

**Data Layer**:

- [ ] Repository 実装
- [ ] Supabase/Ktor RemoteDataSource
- [ ] Room Entity、DAO 定義
- [ ] BLE 検出機能（:data:local:ble モジュール未作成）
- [ ] WorkManager 実装（:data:local:worker モジュール未作成）

**DI**:

- [ ] Koin モジュール定義

**その他**:

- [ ] テーマ・スタイルモジュール（:presentation:theme モジュール未作成）

### 2.5 注意事項

**Navigation.kt の整合性**:
現在`app/Navigation.kt`では以下のルートを使用していますが、`presentation:navigation`モジュールの構造と統一する必要があります：

```kotlin
// app/Navigation.kt (現在)
sealed interface NavRoute {
    data object Auth : NavRoute
    data object Plaza : NavRoute
    data object Timeline : NavRoute
    data object Setting : NavRoute
}

// presentation:navigation (Type-Safe)
sealed interface AppNavRoute {
    data class Auth(val route: AuthNavRoute? = null) : AppNavRoute
    data class Hub(val route: HubNavRoute? = null) : AppNavRoute
}
```

→ **TODO**: `app/Navigation.kt`を`AppNavRoute`を使用するように修正

---

## 3. 技術スタック

### 3.1 採用技術一覧（実装済みバージョン）

#### 基盤技術

| カテゴリ       | 技術                  | 実装バージョン | KMP 対応 | 状態        |
| -------------- | --------------------- | -------------- | -------- | ----------- |
| 開発言語       | Kotlin                | 2.2.20         | ✅       | ✅ 実装済み |
| ビルドシステム | Gradle                | 8.13.0         | ✅       | ✅ 実装済み |
| UI             | Jetpack Compose       | BOM 2025.09.01 | ✅       | ✅ 実装済み |
| 非同期処理     | Kotlin Coroutines     | 1.10.2         | ✅       | 📦 準備済み |
| リアクティブ   | Kotlin Flow           | -              | ✅       | 📦 準備済み |
| シリアライズ   | kotlinx.serialization | 1.9.0          | ✅       | ✅ 実装済み |

#### アーキテクチャ・ナビゲーション

| カテゴリ       | 技術                     | バージョン | KMP 対応 | 状態        |
| -------------- | ------------------------ | ---------- | -------- | ----------- |
| MVI            | GitHub: segnities007/mvi | 1.0.0      | ✅       | ✅ 実装済み |
| ナビゲーション | Navigation Compose       | 2.9.5      | ✅       | ✅ 実装済み |
| DI             | Koin                     | 計画中     | ✅       | ⏳ 未実装   |

#### ネットワーク（計画中）

| 技術        | 推奨バージョン | KMP 対応 | 状態      |
| ----------- | -------------- | -------- | --------- |
| Ktor Client | 3.0.0+         | ✅       | ⏳ 未実装 |
| Supabase-kt | 3.0.0+         | ✅       | ⏳ 未実装 |

#### ローカルストレージ（計画中）

| 技術         | 推奨バージョン | KMP 対応   | 状態      |
| ------------ | -------------- | ---------- | --------- |
| Room         | 2.7.0+         | ✅ (Alpha) | ⏳ 未実装 |
| Coil3 (画像) | 3.0.0+         | ✅         | ⏳ 未実装 |

#### Android 固有機能（将来的に KMP 対応検討）

| 技術               | 用途                 | iOS 代替案       | 状態      |
| ------------------ | -------------------- | ---------------- | --------- |
| BLE API            | すれ違い検出         | Core Bluetooth   | ⏳ 未実装 |
| WorkManager        | バックグラウンド処理 | Background Tasks | ⏳ 未実装 |
| Foreground Service | 常時動作             | Background Modes | ⏳ 未実装 |

### 3.2 依存関係バージョン管理

**Gradle Version Catalog** (`gradle/libs.versions.toml`) を使用して一元管理しています。

```toml
[versions]
agp = "8.13.0"
kotlin = "2.2.20"
coreKtx = "1.17.0"
composeBom = "2025.09.01"
lifecycleRuntimeKtx = "2.9.4"
activityCompose = "1.11.0"
navigationCompose = "2.9.5"
kotlinxCoroutines = "1.10.2"
kotlinxSerialization = "1.9.0"
material3 = "1.4.0"
mvi = "1.0.0"

[libraries]
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutines" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
mvi = { module = "com.github.segnities007:mvi", version.ref = "mvi" }
# ...その他のライブラリ
```

### 3.3 SDK バージョン

- **minSdk**: 28 (Android 9.0 Pie)
- **targetSdk**: 36 (Android 14+)
- **compileSdk**: 36
- **Java 互換性**: Java 11

---

## 4. システムアーキテクチャ

### 4.1 アーキテクチャパターン

**採用**: Clean Architecture + MVI (Model-View-Intent)

### 4.2 MVI 概要

**MVI (Model-View-Intent)** は単方向データフローを実現するアーキテクチャパターン

```
User Action (Intent)
    ↓
ViewModel (Intent処理)
    ↓
UseCase実行
    ↓
Reducer (State更新)
    ↓
State変更
    ↓
View (State監視・再描画)
    ↓
Effect (一度きりのイベント)
```

**MVI vs MVVM**:

- MVVM: 双方向データバインディング可能
- MVI: 完全な単方向データフロー、状態管理が明確

### 4.3 レイヤー構成

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  ┌──────────────────────────────────┐   │
│  │  Compose UI (View)               │   │
│  │  ViewModel (Intent → State)      │   │
│  │  UiState / UiIntent / UiEffect   │   │
│  │  Reducer                         │   │
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

### 4.4 MVI 実装パターン

#### UiState 定義

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

#### UiIntent 定義

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

#### UiEffect 定義

```kotlin
// 一度きりのイベントを表現（ナビゲーション、Snackbar表示など）
sealed interface TimelineEffect {
    data class NavigateToPostDetail(val postId: String) : TimelineEffect
    data class ShowSnackbar(val message: String) : TimelineEffect
    object ShowLikeAnimation : TimelineEffect
}
```

#### Reducer 定義

```kotlin
// State変更ロジックを集約
object TimelineReducer {

    fun reduce(
        currentState: TimelineUiState,
        result: TimelineResult
    ): TimelineUiState {
        return when (result) {
            is TimelineResult.Loading -> TimelineUiState.Loading

            is TimelineResult.PostsLoaded -> {
                if (currentState !is TimelineUiState.Success) {
                    return TimelineUiState.Success(
                        posts = result.posts,
                        hasMore = result.hasMore
                    )
                }
                currentState.copy(
                    posts = result.posts,
                    isRefreshing = false,
                    hasMore = result.hasMore
                )
            }

            is TimelineResult.PostsAppended -> {
                if (currentState !is TimelineUiState.Success) return currentState
                currentState.copy(
                    posts = currentState.posts + result.posts,
                    hasMore = result.hasMore
                )
            }

            is TimelineResult.PostLikeUpdated -> {
                if (currentState !is TimelineUiState.Success) return currentState
                currentState.copy(
                    posts = currentState.posts.map { post ->
                        if (post.id == result.postId) {
                            post.copy(
                                isLikedByMe = result.isLiked,
                                likeCount = if (result.isLiked) {
                                    post.likeCount + 1
                                } else {
                                    post.likeCount - 1
                                }
                            )
                        } else {
                            post
                        }
                    }
                )
            }

            is TimelineResult.Error -> TimelineUiState.Error(result.message)

            is TimelineResult.RefreshStarted -> {
                if (currentState !is TimelineUiState.Success) return currentState
                currentState.copy(isRefreshing = true)
            }
        }
    }
}

// Reducerに渡す中間結果
sealed interface TimelineResult {
    object Loading : TimelineResult
    data class PostsLoaded(val posts: List<Post>, val hasMore: Boolean) : TimelineResult
    data class PostsAppended(val posts: List<Post>, val hasMore: Boolean) : TimelineResult
    data class PostLikeUpdated(val postId: String, val isLiked: Boolean) : TimelineResult
    data class Error(val message: String) : TimelineResult
    object RefreshStarted : TimelineResult
}
```

#### ViewModel 設計（早期リターンパターン）

```kotlin
class TimelineViewModel(
    private val getTimelineUseCase: GetTimelineUseCase,
    private val likePostUseCase: LikePostUseCase
) : ViewModel() {

    // State保持
    private val _uiState = MutableStateFlow<TimelineUiState>(TimelineUiState.Loading)
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    // Effect送信用
    private val _effect = Channel<TimelineEffect>(Channel.BUFFERED)
    val effect: Flow<TimelineEffect> = _effect.receiveAsFlow()

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
            // 早期リターン: 既にローディング中の場合
            if (_uiState.value is TimelineUiState.Loading) return@launch

            reduceState(TimelineResult.Loading)

            val result = getTimelineUseCase()

            // 早期リターン: 失敗時
            if (result.isFailure) {
                val error = result.exceptionOrNull()
                reduceState(TimelineResult.Error(error?.message ?: "Unknown error"))
                return@launch
            }

            // 早期リターン: データがnullの場合
            val posts = result.getOrNull() ?: return@launch

            reduceState(TimelineResult.PostsLoaded(posts, hasMore = posts.size >= 20))
        }
    }

    private fun refreshTimeline() {
        viewModelScope.launch {
            // 早期リターン: Successでない場合はリフレッシュ不可
            if (_uiState.value !is TimelineUiState.Success) return@launch

            reduceState(TimelineResult.RefreshStarted)

            val result = getTimelineUseCase()

            // 早期リターン: 失敗時
            if (result.isFailure) {
                sendEffect(TimelineEffect.ShowSnackbar("更新に失敗しました"))
                return@launch
            }

            val posts = result.getOrNull() ?: return@launch
            reduceState(TimelineResult.PostsLoaded(posts, hasMore = posts.size >= 20))
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // 早期リターン: Successでない、または追加データがない場合
            if (currentState !is TimelineUiState.Success) return@launch
            if (!currentState.hasMore) return@launch

            val offset = currentState.posts.size
            val result = getTimelineUseCase(offset = offset)

            // 早期リターン: 失敗時
            if (result.isFailure) {
                sendEffect(TimelineEffect.ShowSnackbar("読み込みに失敗しました"))
                return@launch
            }

            val posts = result.getOrNull() ?: return@launch
            reduceState(TimelineResult.PostsAppended(posts, hasMore = posts.size >= 20))
        }
    }

    private fun likePost(postId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value

            // 早期リターン: Successでない場合
            if (currentState !is TimelineUiState.Success) return@launch

            val post = currentState.posts.find { it.id == postId }

            // 早期リターン: 投稿が見つからない場合
            if (post == null) return@launch

            val newLikedState = !post.isLikedByMe

            // 楽観的UI更新
            reduceState(TimelineResult.PostLikeUpdated(postId, newLikedState))
            sendEffect(TimelineEffect.ShowLikeAnimation)

            val result = if (newLikedState) {
                likePostUseCase(postId)
            } else {
                likePostUseCase.unlike(postId)
            }

            // 早期リターン: 成功した場合は何もしない
            if (result.isSuccess) return@launch

            // 失敗時はロールバック
            reduceState(TimelineResult.PostLikeUpdated(postId, !newLikedState))
            sendEffect(TimelineEffect.ShowSnackbar("いいねに失敗しました"))
        }
    }

    private fun navigateToPost(postId: String) {
        viewModelScope.launch {
            sendEffect(TimelineEffect.NavigateToPostDetail(postId))
        }
    }

    // Reducerを使用してState更新
    private fun reduceState(result: TimelineResult) {
        _uiState.value = TimelineReducer.reduce(_uiState.value, result)
    }

    // Effect送信
    private suspend fun sendEffect(effect: TimelineEffect) {
        _effect.send(effect)
    }
}
```

#### Compose UI

```kotlin
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = koinViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect処理
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TimelineEffect.NavigateToPostDetail -> {
                    navController.navigate(PostDetailRoute(effect.postId))
                }
                is TimelineEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is TimelineEffect.ShowLikeAnimation -> {
                    // アニメーション処理
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when (val state = uiState) {
            is TimelineUiState.Loading -> LoadingView()
            is TimelineUiState.Success -> TimelineContent(
                posts = state.posts,
                onIntent = viewModel::onIntent,
                modifier = Modifier.padding(padding)
            )
            is TimelineUiState.Error -> ErrorView(state.message)
        }
    }
}

@Composable
private fun TimelineContent(
    posts: List<Post>,
    onIntent: (TimelineIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
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

### 3.5 モジュール構成の概要

**詳細は [セクション 2.2 モジュール構成](#22-モジュール構成実装済み) を参照してください。**

本プロジェクトは、Clean Architecture の各レイヤーごとに 15 個の Gradle モジュールに分割されています：

- **Presentation Layer** (11 モジュール): UI 画面、ナビゲーション
- **Domain Layer** (3 モジュール): Model, Repository Interface, UseCase
- **Data Layer** (3 モジュール): Repository 実装, Remote DataSource, Local DB
- **DI & Utility** (2 モジュール): 依存性注入とユーティリティ

各モジュールは独立しており、レイヤー間の依存関係はインターフェースを通じて実現されます。

### 3.6 依存性注入設計（Koin）

#### モジュール分割

| モジュール       | 責務             | スコープ        |
| ---------------- | ---------------- | --------------- |
| AppModule        | Application 依存 | Singleton       |
| NetworkModule    | ネットワーク関連 | Singleton       |
| DatabaseModule   | DB 関連          | Singleton       |
| RepositoryModule | Repository       | Singleton       |
| ViewModelModule  | ViewModel        | ViewModel Scope |

### 3.7 Navigation Compose 設計

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

#### NavGraph 定義

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

### 3.8 エラーハンドリング（早期リターンパターン）

#### Result 型の採用

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

#### UseCase 実装例（早期リターン）

```kotlin
class GetTimelineUseCase(
    private val timelineRepository: TimelineRepository,
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(offset: Int = 0): Result<List<Post>> {
        // 早期リターン: すれ違い情報の取得失敗
        val encountersResult = encounterRepository.getEncounters()
        if (encountersResult.isFailure) {
            return Result.failure(
                encountersResult.exceptionOrNull() ?: Exception("Unknown error")
            )
        }

        val encounters = encountersResult.getOrNull()

        // 早期リターン: すれ違いがない場合
        if (encounters.isNullOrEmpty()) {
            return Result.success(emptyList())
        }

        // 早期リターン: タイムライン取得失敗
        val timelineResult = timelineRepository.getTimeline(
            encounterUuids = encounters.map { it.uuid },
            encounterTimestamps = encounters.associate { it.uuid to it.lastSeen },
            offset = offset
        )

        if (timelineResult.isFailure) {
            return Result.failure(
                timelineResult.exceptionOrNull() ?: Exception("Unknown error")
            )
        }

        return timelineResult
    }
}
```

#### Repository 実装例（早期リターン）

```kotlin
class TimelineRepositoryImpl(
    private val remoteDataSource: TimelineRemoteDataSource,
    private val localDataSource: TimelineLocalDataSource
) : TimelineRepository {

    override suspend fun getTimeline(
        encounterUuids: List<String>,
        encounterTimestamps: Map<String, Long>,
        offset: Int
    ): Result<List<Post>> {
        // 早期リターン: パラメータ検証失敗
        if (encounterUuids.isEmpty()) {
            return Result.failure(
                IllegalArgumentException("Encounter UUIDs cannot be empty")
            )
        }

        // ローカルキャッシュチェック
        val cachedPosts = localDataSource.getCachedPosts(offset)
        val cacheValid = localDataSource.isCacheValid()

        // 早期リターン: キャッシュが有効な場合
        if (cacheValid && cachedPosts.isNotEmpty()) {
            return Result.success(cachedPosts)
        }

        // リモート取得
        val remoteResult = remoteDataSource.fetchTimeline(
            encounterUuids = encounterUuids,
            encounterTimestamps = encounterTimestamps,
            offset = offset
        )

        // 早期リターン: リモート取得失敗
        if (remoteResult.isFailure) {
            // ネットワークエラー時は古いキャッシュを返す
            if (cachedPosts.isNotEmpty()) {
                return Result.success(cachedPosts)
            }
            return remoteResult
        }

        val posts = remoteResult.getOrNull() ?: return Result.success(emptyList())

        // キャッシュ保存（失敗しても無視）
        localDataSource.cachePosts(posts)

        return Result.success(posts)
    }
}
```

---

## 5. データベース設計

### 5.1 Supabase PostgreSQL 設計

#### 4.1.1 Users テーブル

| カラム           | 型        | 制約            | 説明                     |
| ---------------- | --------- | --------------- | ------------------------ |
| uuid             | VARCHAR   | PRIMARY KEY     | BLE 用 UUID              |
| google_id        | VARCHAR   | UNIQUE NOT NULL | Google 認証 ID           |
| nickname         | VARCHAR   | NOT NULL        | ユーザー名（2-20 文字）  |
| avatar_url       | VARCHAR   | NULL            | プロフィール画像 URL     |
| bio              | TEXT      | NULL            | 自己紹介（500 文字以内） |
| total_encounters | INT       | DEFAULT 0       | 総すれ違い数             |
| achievements     | TEXT[]    | NULL            | 獲得実績                 |
| is_active        | BOOLEAN   | DEFAULT TRUE    | アカウント有効フラグ     |
| created_at       | TIMESTAMP | DEFAULT NOW()   | 作成日時                 |
| updated_at       | TIMESTAMP | DEFAULT NOW()   | 更新日時                 |

**インデックス**:

- `idx_users_google_id` (google_id)
- `idx_users_nickname` (nickname)

#### 4.1.2 Posts テーブル

| カラム        | 型        | 制約                | 説明                   |
| ------------- | --------- | ------------------- | ---------------------- |
| id            | UUID      | PRIMARY KEY         | 投稿 ID                |
| author_uuid   | VARCHAR   | FOREIGN KEY → users | 投稿者 UUID            |
| content       | TEXT      | NOT NULL            | 投稿内容（1-256 文字） |
| media_url     | VARCHAR   | NULL                | メディア URL           |
| media_type    | VARCHAR   | NULL                | image/video            |
| thumbnail_url | VARCHAR   | NULL                | サムネイル URL         |
| like_count    | INT       | DEFAULT 0           | いいね数               |
| is_deleted    | BOOLEAN   | DEFAULT FALSE       | 論理削除フラグ         |
| created_at    | TIMESTAMP | DEFAULT NOW()       | 作成日時               |
| updated_at    | TIMESTAMP | DEFAULT NOW()       | 更新日時               |

**インデックス**:

- `idx_posts_author` (author_uuid)
- `idx_posts_created_at` (created_at DESC)
- `idx_posts_author_created` (author_uuid, created_at DESC)

#### 4.1.3 Likes テーブル

| カラム     | 型        | 制約                | 説明          |
| ---------- | --------- | ------------------- | ------------- |
| id         | UUID      | PRIMARY KEY         | いいね ID     |
| user_uuid  | VARCHAR   | FOREIGN KEY → users | ユーザー UUID |
| post_id    | UUID      | FOREIGN KEY → posts | 投稿 ID       |
| created_at | TIMESTAMP | DEFAULT NOW()       | 作成日時      |

**制約**: UNIQUE(user_uuid, post_id)

#### 4.1.4 Encounters テーブル

| カラム            | 型        | 制約                | 説明             |
| ----------------- | --------- | ------------------- | ---------------- |
| id                | UUID      | PRIMARY KEY         | すれ違い ID      |
| user_uuid_a       | VARCHAR   | FOREIGN KEY → users | ユーザー A       |
| user_uuid_b       | VARCHAR   | FOREIGN KEY → users | ユーザー B       |
| last_encounter_at | TIMESTAMP | NOT NULL            | 最終すれ違い日時 |
| encounter_count   | INT       | DEFAULT 1           | すれ違い回数     |
| average_rssi      | INT       | NULL                | 平均電波強度     |
| created_at        | TIMESTAMP | DEFAULT NOW()       | 初回すれ違い日時 |
| updated_at        | TIMESTAMP | DEFAULT NOW()       | 更新日時         |

**制約**:

- UNIQUE(user_uuid_a, user_uuid_b)
- CHECK(user_uuid_a < user_uuid_b)

#### 4.1.5 Blocked Users テーブル

| カラム       | 型        | 制約                | 説明                   |
| ------------ | --------- | ------------------- | ---------------------- |
| id           | UUID      | PRIMARY KEY         | ブロック ID            |
| blocker_uuid | VARCHAR   | FOREIGN KEY → users | ブロックしたユーザー   |
| blocked_uuid | VARCHAR   | FOREIGN KEY → users | ブロックされたユーザー |
| reason       | VARCHAR   | NULL                | ブロック理由           |
| created_at   | TIMESTAMP | DEFAULT NOW()       | ブロック日時           |

**制約**: UNIQUE(blocker_uuid, blocked_uuid)

#### 4.1.6 Privacy Settings テーブル

| カラム             | 型        | 制約             | 説明                           |
| ------------------ | --------- | ---------------- | ------------------------------ |
| user_uuid          | VARCHAR   | PRIMARY KEY      | ユーザー UUID                  |
| discovery_enabled  | BOOLEAN   | DEFAULT TRUE     | すれ違い検出有効               |
| profile_visibility | VARCHAR   | DEFAULT 'public' | public/encounters_only/private |
| location_sharing   | BOOLEAN   | DEFAULT FALSE    | 位置情報共有                   |
| updated_at         | TIMESTAMP | DEFAULT NOW()    | 更新日時                       |

#### 4.1.7 Reports テーブル

| カラム             | 型        | 制約                | 説明                                 |
| ------------------ | --------- | ------------------- | ------------------------------------ |
| id                 | UUID      | PRIMARY KEY         | 通報 ID                              |
| reporter_uuid      | VARCHAR   | FOREIGN KEY → users | 通報者                               |
| reported_post_id   | UUID      | FOREIGN KEY → posts | 通報された投稿                       |
| reported_user_uuid | VARCHAR   | FOREIGN KEY → users | 通報されたユーザー                   |
| reason             | VARCHAR   | NOT NULL            | 通報理由                             |
| description        | TEXT      | NULL                | 詳細説明                             |
| status             | VARCHAR   | DEFAULT 'pending'   | pending/reviewing/resolved/dismissed |
| created_at         | TIMESTAMP | DEFAULT NOW()       | 通報日時                             |
| updated_at         | TIMESTAMP | DEFAULT NOW()       | 更新日時                             |

#### 4.1.8 Analytics Events テーブル

| カラム     | 型        | 制約                | 説明           |
| ---------- | --------- | ------------------- | -------------- |
| id         | UUID      | PRIMARY KEY         | イベント ID    |
| user_uuid  | VARCHAR   | FOREIGN KEY → users | ユーザー UUID  |
| event_type | VARCHAR   | NOT NULL            | イベント種別   |
| event_data | JSONB     | NULL                | イベントデータ |
| created_at | TIMESTAMP | DEFAULT NOW()       | 発生日時       |

#### 4.1.9 トリガー設定

**updated_at 自動更新**: users, posts, encounters, privacy_settings, reports

**like_count 自動更新**: likes テーブルの INSERT/DELETE 時

**total_encounters 自動更新**: encounters テーブルの INSERT 時

#### 4.1.10 Row Level Security (RLS)

**Users**:

- SELECT: 全ユーザー閲覧可能
- UPDATE: 自分のプロフィールのみ

**Posts**:

- SELECT: is_deleted=FALSE のみ
- INSERT/UPDATE/DELETE: 自分の投稿のみ

**Likes**:

- SELECT: 全ユーザー閲覧可能
- INSERT: 認証済みユーザー
- DELETE: 自分のいいねのみ

### 4.2 Room Database 設計（ローカル）

#### 4.2.1 Encounters エンティティ

| カラム         | 型     | 制約        | 説明                       |
| -------------- | ------ | ----------- | -------------------------- |
| uuid           | String | PRIMARY KEY | すれ違い相手 UUID          |
| lastSeen       | Long   | NOT NULL    | 最終すれ違いタイムスタンプ |
| encounterDate  | String | NOT NULL    | すれ違い日付               |
| encounterCount | Int    | NOT NULL    | すれ違い回数               |
| rssiValues     | String | NOT NULL    | RSSI 値履歴（JSON）        |
| averageRssi    | Int    | NOT NULL    | 平均 RSSI                  |

#### 4.2.2 Post Cache エンティティ

| カラム         | 型      | 制約        | 説明             |
| -------------- | ------- | ----------- | ---------------- |
| id             | String  | PRIMARY KEY | 投稿 ID          |
| authorUuid     | String  | NOT NULL    | 投稿者 UUID      |
| authorNickname | String  | NOT NULL    | 投稿者名         |
| authorAvatar   | String  | NULL        | 投稿者アバター   |
| content        | String  | NOT NULL    | 投稿内容         |
| mediaUrl       | String  | NULL        | メディア URL     |
| mediaType      | String  | NULL        | メディアタイプ   |
| likeCount      | Int     | NOT NULL    | いいね数         |
| isLikedByMe    | Boolean | NOT NULL    | 自分のいいね状態 |
| createdAt      | Long    | NOT NULL    | 投稿日時         |
| cachedAt       | Long    | NOT NULL    | キャッシュ日時   |

#### 4.2.3 User Cache エンティティ

| カラム          | 型     | 制約        | 説明           |
| --------------- | ------ | ----------- | -------------- |
| uuid            | String | PRIMARY KEY | ユーザー UUID  |
| nickname        | String | NOT NULL    | ニックネーム   |
| avatarUrl       | String | NULL        | アバター URL   |
| bio             | String | NULL        | 自己紹介       |
| totalEncounters | Int    | NOT NULL    | 総すれ違い数   |
| cachedAt        | Long   | NOT NULL    | キャッシュ日時 |

### 4.3 データ同期戦略

#### 4.3.1 ローカル → サーバー同期

**すれ違い情報同期**:

- タイミング: 5 分間隔、WorkManager
- バッチサイズ: 最大 100 件/リクエスト
- リトライ: 失敗時 3 回まで

**投稿データ同期**:

- タイミング: 投稿作成直後
- オフライン対応: ローカル保存後、オンライン復帰時に同期

#### 4.3.2 サーバー → ローカル同期

**投稿キャッシュ更新**:

- タイミング: タイムライン表示時
- TTL: 5 分間
- 最大件数: 500 件

**ユーザーキャッシュ更新**:

- タイミング: プロフィール表示時
- TTL: 1 時間

---

## 6. API 設計

### 6.1 エンドポイント一覧

| メソッド | エンドポイント       | 説明                   | 認証 |
| -------- | -------------------- | ---------------------- | ---- |
| POST     | /auth/google         | Google OAuth 認証      | 不要 |
| GET      | /api/profile         | 自分のプロフィール取得 | 必要 |
| PUT      | /api/profile         | プロフィール更新       | 必要 |
| POST     | /api/posts           | 投稿作成               | 必要 |
| PUT      | /api/posts/{id}      | 投稿編集               | 必要 |
| DELETE   | /api/posts/{id}      | 投稿削除               | 必要 |
| POST     | /api/timeline        | タイムライン取得       | 必要 |
| POST     | /api/likes           | いいね追加             | 必要 |
| DELETE   | /api/likes/{post_id} | いいね削除             | 必要 |
| GET      | /api/search/users    | ユーザー検索           | 必要 |
| GET      | /api/search/posts    | 投稿検索               | 必要 |
| GET      | /api/users/{uuid}    | ユーザー情報取得       | 必要 |
| POST     | /api/encounters/sync | すれ違い情報同期       | 必要 |
| POST     | /api/reports         | 通報                   | 必要 |
| POST     | /api/blocks          | ブロック               | 必要 |
| DELETE   | /api/blocks/{uuid}   | ブロック解除           | 必要 |

### 5.2 認証フロー

#### Google OAuth 認証

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

### 5.3 タイムライン取得 API

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

| ステータス | コード              | 説明             |
| ---------- | ------------------- | ---------------- |
| 400        | INVALID_REQUEST     | リクエスト不正   |
| 401        | UNAUTHORIZED        | 認証失敗         |
| 403        | FORBIDDEN           | 権限不足         |
| 404        | NOT_FOUND           | リソース未検出   |
| 409        | CONFLICT            | データ競合       |
| 413        | PAYLOAD_TOO_LARGE   | データサイズ超過 |
| 429        | RATE_LIMIT_EXCEEDED | レート制限超過   |
| 500        | INTERNAL_ERROR      | サーバーエラー   |

### 5.5 レート制限

| エンドポイント    | 制限       |
| ----------------- | ---------- |
| /auth/\*          | 10 回/分   |
| /api/posts (POST) | 20 回/時間 |
| /api/likes/\*     | 30 回/分   |
| /api/timeline     | 60 回/分   |
| その他            | 60 回/分   |

---

## 7. 機能仕様

### 7.1 すれ違い検出機能

#### 6.1.1 BLE 検出仕様

**検出範囲**: 約 10m（RSSI -90dBm 以上）

**動作モード**:

| モード           | スキャン間隔 | スキャン時間 | 条件                   |
| ---------------- | ------------ | ------------ | ---------------------- |
| フォアグラウンド | 1 分         | 10 秒        | アプリ起動中           |
| バックグラウンド | 5 分         | 10 秒        | アプリバックグラウンド |
| 夜間モード       | 10 分        | 10 秒        | 23:00-6:00             |

**RSSI 判定**:

- -60dBm 以上: 近距離（high quality）
- -75dBm 以上: 中距離（medium quality）
- -90dBm 以上: 遠距離（low quality）
- -90dBm 未満: 検出対象外

**UUID 交換方式**:

- BLE Advertising: 自分の UUID を発信
- BLE Scanning: 周囲の UUID を受信
- Service UUID: 固定値（アプリ識別用）
- Manufacturer Data: ユーザー UUID 格納

#### 6.1.2 すれ違い判定ロジック

**新規すれ違い**:

1. UUID を初めて検出
2. ローカル DB に保存
3. サーバーに同期（5 分以内）

**既存すれ違い更新**:

1. 既存 UUID を再検出
2. lastSeen タイムスタンプ更新
3. encounterCount 加算
4. RSSI 履歴更新（最大 10 件保持）

**重複検出防止**: 同じ UUID の 5 分以内の再検出は無視

#### 6.1.3 バックグラウンド動作

**Foreground Service 使用**:

- 常駐通知表示（低優先度）
- Doze mode 対応
- Battery Optimization 除外要求

**権限要件**:

- `ACCESS_FINE_LOCATION`: BLE 検出に必須
- `BLUETOOTH_SCAN`: バックグラウンドスキャン
- `BLUETOOTH_ADVERTISE`: UUID 発信
- `POST_NOTIFICATIONS`: 通知表示
- `FOREGROUND_SERVICE`: フォアグラウンドサービス

### 6.2 投稿機能

#### 6.2.1 投稿作成

**テキスト投稿**:

- 文字数: 1-256 文字
- 制限: なし（無制限投稿可能）

**メディア投稿**:

- 対応形式: JPEG, PNG, GIF（画像）/ MP4, MOV（動画）
- 最大サイズ: 画像 10MB、動画 50MB
- 容量制限: Free 100MiB/月、Pro 無制限

**画像処理**:

- 最大解像度: 1920x1920px
- JPEG 品質: 85%
- サムネイル: 300x300px 自動生成
- EXIF 情報: 位置情報削除

**動画処理**:

- 最大長: 60 秒
- 解像度: 1080p 以下
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

- 初回読み込み: 20 件
- 追加読み込み: 20 件ずつ
- トリガー: 残り 5 件でロード開始

**キャッシュ戦略**:

- メモリキャッシュ: 最新 100 件
- ディスクキャッシュ: 最新 500 件
- TTL: 5 分間

#### 6.3.3 時刻フィルタリング

**閲覧制限ロジック**:

- すれ違ったユーザーの投稿のみ
- すれ違い時刻以前の投稿のみ
- 再度すれ違えば最新投稿取得可能

### 6.4 いいね機能

#### 6.4.1 いいね操作

**追加**: タップでいいね追加、楽観的 UI 更新

**削除**: 再タップでいいね削除、楽観的 UI 更新

**レート制限**: 1 分間 30 回まで

#### 6.4.2 表示仕様

**いいね数表示**:

- 0 件: 表示なし
- 1-999 件: そのまま表示
- 1,000 件以上: "1.2k"形式

### 6.5 検索機能

#### 6.5.1 ユーザー検索

**検索対象**: すれ違ったユーザーのみ

**検索条件**: ニックネーム部分一致

**表示内容**: アイコン、ニックネーム、自己紹介、すれ違い回数

#### 6.5.2 投稿検索

**検索対象**: すれ違ったユーザーの投稿

**検索条件**: 投稿内容全文検索、時刻フィルタリング適用

#### 6.5.3 検索 UI

**リアルタイム検索**: 入力中に結果更新（デバウンス 300ms）

### 6.6 プロフィール機能

#### 6.6.1 自分のプロフィール

**表示項目**:

- アバター画像
- ニックネーム
- 自己紹介
- 総すれ違い数
- 総投稿数

**編集項目**:

- アバター画像（最大 5MB）
- ニックネーム（2-20 文字）
- 自己紹介（最大 500 文字）

#### 6.6.2 他ユーザープロフィール

**表示項目**:

- 基本情報
- すれ違い回数
- 最終すれ違い日時
- 投稿一覧（時刻フィルタリング適用）

**操作**: ブロック、通報

### 6.7 設定機能

#### 6.7.1 プライバシー設定

**すれ違い検出**: ON/OFF 切替

**プロフィール公開範囲**:

- public: 全ユーザー
- encounters_only: すれ違ったユーザーのみ
- private: 非公開

#### 6.7.2 通知設定

**すれ違い通知**: 1 日 1 回集約通知（19:00）、ON/OFF 切替

**サイレント時間**: 22:00-8:00 は通知 OFF

#### 6.7.3 アカウント設定

**アカウント削除**: 全データ削除、30 日間猶予期間

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

## 8. UI/UX 設計

### 8.1 画面構成

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

**Material Design 3 準拠**:

- Primary: Dynamic Color 対応
- Surface: 背景色
- On-Surface: テキスト色
- Error: エラー表示色

**ダークモード対応**: システム設定に追従

#### 7.2.2 タイポグラフィ

| 要素     | フォントサイズ | ウェイト |
| -------- | -------------- | -------- |
| Display  | 32sp           | Bold     |
| Headline | 24sp           | SemiBold |
| Title    | 20sp           | Medium   |
| Body     | 16sp           | Regular  |
| Label    | 14sp           | Medium   |
| Caption  | 12sp           | Regular  |

#### 7.2.3 間隔・サイズ

**基本間隔**: 4 の倍数（4, 8, 12, 16, 24, 32, 48, 64）

**コンポーネントサイズ**:

- アイコン: 24dp 標準、48dp タッチターゲット
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
- 最近の活動（3-5 件）
- Bottom Navigation Bar

#### 7.3.2 Timeline Screen

**レイアウト**:

- ヘッダー: タイトル、更新ボタン
- Pull-to-Refresh 対応
- 投稿カード（Twitter 風）
- 無限スクロール
- FloatingActionButton（投稿作成）

#### 7.3.3 Post Create Screen

**レイアウト**:

- トップバー: キャンセル・投稿ボタン
- テキスト入力（複数行）
- 文字数カウンター（256 文字制限）
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

**いいねボタン**: Scale + Color 変化（150ms）

#### 7.4.2 フィードバック

**タップ**: Ripple 効果

**エラー**: Snackbar 表示（4 秒）

**成功**: Snackbar 表示（2 秒）

**ローディング**: CircularProgressIndicator

---

## 9. セキュリティ設計

### 9.1 認証・認可

**認証方式**: Google OAuth 2.0 + Supabase Auth

**トークン管理**:

- Access Token: JWT、有効期限 1 時間
- Refresh Token: 有効期限 30 日
- 自動更新: Access Token 期限切れ前
- 保存: ローカルに暗号化保存

### 8.2 データ暗号化

**通信**: HTTPS/TLS 1.3

**ローカルストレージ**: AndroidKeyStore で鍵管理

**UUID 生成**: SHA-256（Google ID + ソルト + タイムスタンプ）

### 8.3 入力値検証

**クライアント側**:

- 文字数制限チェック
- 不正文字フィルタリング
- ファイル形式・サイズ検証

**サーバー側**:

- SQL インジェクション対策
- XSS 対策
- CSRF 対策

### 8.4 プライバシー保護

**UUID 匿名性**: Google ID から直接 UUID 推測不可能

**位置情報**: GPS 使用しない、BLE のみ

**メディアファイル**: EXIF 位置情報自動削除

---

## 10. テスト戦略

### 10.1 テストレベル

#### 9.1.1 Unit Test（目標カバレッジ: 70%）

**対象**:

- Domain Layer（全 UseCase）
- Data Layer（Repository 実装）
- Utility 関数

**フレームワーク**:

- JUnit 4
- Kotlinx Coroutines Test
- Turbine（Flow テスト）
- MockK（モック）
- Koin Test

#### 9.1.2 Integration Test

**対象**:

- Repository + DataSource 連携
- Supabase API 通信
- Room Database 操作

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

### 9.2 β テスト計画

#### 期間・参加者

- **期間**: 2 週間
- **参加者**: 10-20 名
- **配信方法**: Google Play Internal Testing

#### テスト観点

1. **機能テスト**: 全機能の動作確認
2. **ユーザビリティ**: UI/UX フィードバック
3. **パフォーマンス**: バッテリー消費、動作速度
4. **実環境テスト**: 実際の外出先でのすれ違い検出

---

## 11. デプロイメント計画

### 11.1 リリース戦略

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

### 10.2 Google Play Console 設定

#### アプリ情報

| 項目     | 内容                       |
| -------- | -------------------------- |
| アプリ名 | すれ違い SNS（仮称）       |
| カテゴリ | ソーシャルネットワーキング |
| 対象年齢 | 12 歳以上                  |
| 価格     | 無料（アプリ内課金あり）   |

#### ストアリスティング

**短い説明（80 文字）**:

```
すれ違った人の投稿だけ見られる新しいSNS。リアルな出会いをオンラインで。
```

**スクリーンショット**: 5 枚必須（1080x1920px）

#### プライバシーポリシー

**必須記載事項**:

- 収集する情報
- 利用目的
- 第三者提供の有無
- データ保存期間
- ユーザーの権利

### 10.3 CI/CD 設定

#### GitHub Actions

**ワークフロー**:

1. **Pull Request 時**: Lint 検査、Unit Test、ビルド確認
2. **main/develop ブランチ push 時**: 上記すべて + APK ビルド
3. **Release タグ push 時**: Release APK/AAB ビルド + 署名

### 10.4 バージョン管理

#### セマンティックバージョニング

**形式**: Major.Minor.Patch (例: 1.2.3)

- **Major**: 破壊的変更
- **Minor**: 新機能追加
- **Patch**: バグ修正

---

## 12. 運用・保守計画

### 12.1 監視・分析

#### 11.1.1 Firebase Analytics

**トラッキングイベント**:

| イベント               | 目的         |
| ---------------------- | ------------ |
| app_open               | アプリ起動数 |
| encounter_detected     | すれ違い検出 |
| post_created           | 投稿作成     |
| post_liked             | いいね       |
| user_searched          | 検索実行     |
| subscription_purchased | Pro 購入     |

#### 11.1.2 Firebase Crashlytics

**監視項目**:

- クラッシュレポート
- 非致命的エラー
- カスタムログ

#### 11.1.3 Supabase 監視

**監視項目**:

- Database 使用量
- Storage 使用量
- API 呼び出し回数
- Edge Functions 実行回数

### 11.2 保守計画

#### 定期メンテナンス

**デイリー**: クラッシュレポート確認、エラーログ確認

**ウィークリー**: KPI レビュー、パフォーマンス分析

**マンスリー**: 総合分析、機能改善計画、セキュリティアップデート

#### アップデート計画

**緊急修正（Hotfix）**: 即座

**小規模アップデート**: 2 週間ごと

**大規模アップデート**: 月 1 回

#### サポート体制

**問い合わせ窓口**: メール、回答目標 3 営業日以内

**FAQ 作成**: アプリ内ヘルプ画面

### 11.3 コスト管理

#### 初期段階（~1,000 ユーザー）

| 項目        | 月額コスト         |
| ----------- | ------------------ |
| Supabase    | $0（無料枠）       |
| Firebase    | $0（無料枠）       |
| Google Play | $0（初回$25 のみ） |
| ドメイン    | 約 100 円          |
| **合計**    | **約 100 円**      |

#### 成長段階（1,000~10,000 ユーザー）

| 項目           | 月額コスト  |
| -------------- | ----------- |
| Supabase Pro   | $25         |
| Firebase Blaze | $0~$50      |
| その他         | $10         |
| **合計**       | **$35~$85** |

### 11.4 リスク管理

#### 技術リスク

| リスク           | 影響度 | 対策               |
| ---------------- | ------ | ------------------ |
| BLE 検出精度低下 | 高     | RSSI 閾値調整      |
| バッテリー消費大 | 高     | スキャン間隔最適化 |
| Supabase 障害    | 中     | ローカルキャッシュ |

#### ビジネスリスク

| リスク           | 影響度 | 対策               |
| ---------------- | ------ | ------------------ |
| ユーザー獲得困難 | 高     | マーケティング強化 |
| すれ違い機会少   | 高     | イベント施策       |
| Pro 転換率低迷   | 中     | 付加価値向上       |

#### 法的リスク

| リスク           | 影響度 | 対策                     |
| ---------------- | ------ | ------------------------ |
| プライバシー問題 | 高     | 個人情報保護法遵守       |
| 不適切投稿       | 中     | 通報機能、モデレーション |

---

## 付録

### A. 用語集

| 用語               | 説明                                               |
| ------------------ | -------------------------------------------------- |
| BLE                | Bluetooth Low Energy。低消費電力 Bluetooth         |
| UUID               | Universally Unique Identifier。ユーザー識別子      |
| RSSI               | Received Signal Strength Indicator。電波強度       |
| MVI                | Model-View-Intent。単方向データフローパターン      |
| Reducer            | State 変更ロジックを集約する純粋関数               |
| Effect             | 一度きりのイベント（ナビゲーション、Snackbar 等）  |
| すれ違い           | BLE で検出された他のユーザーとの物理的接近         |
| 時刻フィルタリング | すれ違い時刻以前の投稿のみ表示する機能             |
| 早期リターン       | 条件を満たさない場合に即座に関数から抜けるパターン |

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

- [ ] Supabase プロジェクト作成
- [ ] Google Cloud Platform 設定
- [ ] Google Play Console 登録
- [ ] GitHub リポジトリ作成
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

本仕様書は、Android アプリとして「すれ違い SNS (Spans)」を開発するための完全ガイドです。

### プロジェクトの現状（2025 年 10 月 6 日時点）

**実装済み**:

- ✅ マルチモジュール構成の完成（15 モジュール）
- ✅ Type-Safe Navigation の基本構造
- ✅ Gradle Version Catalog による依存関係管理
- ✅ Compose 環境のセットアップ
- ✅ MVI ライブラリの統合準備

**次のステップ**:

1. **Navigation 統合**: `app/Navigation.kt`を`AppNavRoute`構造に統一
2. **Domain 層実装**: モデル、Repository Interface、UseCase の定義
3. **Presentation 層実装**: 各画面の UI、ViewModel、UiState/Intent/Effect
4. **Data 層実装**: Repository 実装、RemoteDataSource、LocalDataSource
5. **DI 設定**: Koin モジュールの実装と統合

### 重要なポイント

1. **現在は Android 専用開発**: MVP 完成に集中
2. **Compose Multiplatform 対応技術選定**: 将来の iOS 展開を視野
3. **Clean Architecture + MVI**: 明確な単方向データフロー
4. **Reducer + Effect パターン**: 状態管理の明確化と副作用の分離
5. **早期リターンパターン**: 可読性が高く保守しやすいエラーハンドリング
6. **Navigation Compose 使用**: 公式 KMP 対応ナビゲーション（v2.9.5）
7. **ユーザープライバシー重視**: GPS 不使用、BLE のみ

### 技術バージョンサマリー

- **Kotlin**: 2.2.20
- **Gradle**: 8.13.0
- **Compose BOM**: 2025.09.01
- **Navigation Compose**: 2.9.5
- **Coroutines**: 1.10.2
- **minSdk**: 28 (Android 9.0)
- **targetSdk/compileSdk**: 36
