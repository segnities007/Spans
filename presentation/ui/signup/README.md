# SignUpViewModel - 実装ガイド

## 概要

SignUpViewModel は、**MVI ライブラリ（com.github.segnities007:mvi）を継承**してサインアップ画面の状態管理を行います。

## MVI ライブラリの使用

### 基底クラスの継承構造

```kotlin
// 1. UiState - MVIライブラリの基底インターフェース
sealed interface SignUpUiState : UiState {
    data object Initial : SignUpUiState
    data class Editing(...) : SignUpUiState
    // ...
}

// 2. UiIntent - MVIライブラリの基底インターフェース
sealed interface SignUpIntent : UiIntent {
    data class NicknameChanged(val nickname: String) : SignUpIntent
    data object SignUpClicked : SignUpIntent
    // ...
}

// 3. UiEffect - MVIライブラリの基底インターフェース
sealed interface SignUpEffect : UiEffect {
    data object NavigateToPlaza : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
    // ...
}

// 4. Reducer - MVIライブラリのReducerインターフェース実装
class SignUpReducer : Reducer<SignUpUiState, SignUpIntent> {
    override fun reduce(state: SignUpUiState, intent: SignUpIntent): SignUpUiState {
        return when (intent) {
            is SignUpIntent.NicknameChanged -> reduceNicknameChanged(state, intent.nickname)
            is SignUpIntent.BioChanged -> reduceBioChanged(state, intent.bio)
            // ... 他のIntent処理
        }
    }

    private fun reduceNicknameChanged(state: SignUpUiState, nickname: String): SignUpUiState {
        // 純粋関数として状態を変換
    }
}

// 5. ViewModel - BaseViewModelを継承
class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpUiState, SignUpIntent, SignUpEffect>(
    initialState = SignUpUiState.Initial
) {
    private val reducer = SignUpReducer()

    // onIntent()をオーバーライドしてIntent処理を実装
    override fun onIntent(intent: SignUpIntent) {
        // Reducerで処理
        updateState(reducer.reduce(uiState.value, intent))
    }
}
```

### MVI ライブラリの提供機能

**BaseViewModel 基底クラスが提供するもの：**

- `uiState: StateFlow<STATE>` - UI 状態の監視用 StateFlow（public readonly）
- `effect: Flow<EFFECT>` - 副作用イベントの Flow（public readonly）
- `sendEffect(effect: EFFECT)` - 副作用送信メソッド（protected）

**実装すべき抽象メソッド：**

- `abstract fun onIntent(intent: INTENT)` - Intent 処理ロジック

**⚠️ 現在の制限事項：**

- BaseViewModel の `_uiState` は private のため、状態更新には Reflection が必要
- **推奨対応**: BaseViewModel に `protected fun setState(newState: STATE)` メソッドを追加
- または、BaseViewModel を使わず独自に ViewModel 実装を行う

## 作成されたファイル

### 1. MVI 構造

````
presentation/ui/signup/src/main/java/com/segnities007/signup/mvi/
├── SignUpUiState.kt      # UI状態（UiState継承）
├── SignUpIntent.kt       # ユーザーIntent（UiIntent継承）
├── SignUpEffect.kt       # 副作用（UiEffect継承）
├── SignUpReducer.kt      # 状態変更ロジック（純粋関数）
└── SignUpViewModel.kt    # ViewModel（MviViewModel継承）
```l - 実装ガイド

## 概要
SignUpViewModelは、MVIパターンを使用してサインアップ画面の状態管理を行います。

## 作成されたファイル

### 1. MVI構造
````

presentation/ui/signup/src/main/java/com/segnities007/signup/mvi/
├── SignUpUiState.kt # UI 状態の定義
├── SignUpIntent.kt # ユーザーインテントの定義
├── SignUpEffect.kt # 副作用（一度きりのイベント）の定義
├── SignUpReducer.kt # 状態変更ロジック（純粋関数）
└── SignUpViewModel.kt # ViewModel の実装

```

### 2. DI設定
```

di/src/main/java/com/segnities007/di/
├── UseCaseModule.kt # UseCase の依存性注入
├── ViewModelModule.kt # ViewModel の依存性注入
└── AppModules.kt # 更新済み

```

### 3. 画面実装
```

presentation/ui/signup/src/main/java/com/segnities007/signup/
└── SignUpScreen.kt # 更新済み（ViewModel を使用）

```

## MVIデータフロー（MVIライブラリ使用）

```

User Action (Intent)
↓
ViewModel.onIntent() ← MVI ライブラリが提供
↓
handleIntent() ← アプリが実装
↓
Reducer.reduce() ← 純粋関数
↓
updateState() ← MVI ライブラリが提供
↓
StateFlow 更新
↓
Compose Recomposition
↓
UI 更新

副作用（Effect）
↓
sendEffect() ← MVI ライブラリが提供
↓
Effect Flow
↓
Screen (LaunchedEffect)
↓
ナビゲーション/Snackbar 等

````

### MVIライブラリによる利点

1. **定型コード削減**: StateFlow、Channel、sendEffectの管理が不要
2. **型安全**: ジェネリクスで State、Intent、Effect が型安全に管理
3. **一貫性**: 全てのViewModelが同じパターンで実装可能
4. **テスト容易性**: 基底クラスの動作が保証されている

## 使用例

### 画面での使用
```kotlin
@Composable
fun SignUpScreen(
    onAuthSuccess: () -> Unit = {},
    viewModel: SignUpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Effect処理
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.NavigateToPlaza -> onAuthSuccess()
                is SignUpEffect.ShowError -> { /* Snackbar */ }
            }
        }
    }

    // UI状態に応じた表示
    when (uiState) {
        is SignUpUiState.Editing -> SignUpForm(...)
        is SignUpUiState.Loading -> LoadingView()
        is SignUpUiState.Success -> SuccessView()
        is SignUpUiState.Error -> ErrorView()
    }
}
````

### ユーザーアクション処理（MVI ライブラリの onIntent 使用）

```kotlin
// onIntent()はMviViewModelが提供 → handleIntent()が呼ばれる
viewModel.onIntent(SignUpIntent.NicknameChanged("新しいニックネーム"))
viewModel.onIntent(SignUpIntent.SignUpClicked)
viewModel.onIntent(SignUpIntent.NavigateToSignIn)

// ViewModel内部での状態更新（MVIライブラリのメソッド使用）
override fun handleIntent(intent: SignUpIntent) {
    when (intent) {
        is SignUpIntent.NicknameChanged -> {
            // currentStateプロパティで現在の状態にアクセス
            val newState = SignUpReducer.reduceNicknameChanged(currentState, intent.nickname)
            // updateState()で状態更新
            updateState(newState)
        }
        SignUpIntent.SignUpClicked -> {
            viewModelScope.launch {
                // 状態更新
                updateState(SignUpUiState.Loading)
                // UseCase実行
                val result = signUpUseCase(...)
                // Effect送信
                sendEffect(SignUpEffect.NavigateToPlaza)
            }
        }
    }
}
```

## UiState 一覧

### 1. Initial

初期状態（通常は即座に Editing に移行）

### 2. Editing

```kotlin
data class Editing(
    val nickname: String = "",
    val bio: String = "",
    val avatarUri: String? = null,
    val nicknameError: String? = null,
    val bioError: String? = null,
    val isFormValid: Boolean = false
)
```

### 3. Loading

```kotlin
data class Loading(
    val nickname: String,
    val bio: String,
    val avatarUri: String?
)
```

### 4. Success

```kotlin
data class Success(val user: User)
```

### 5. Error

```kotlin
data class Error(
    val message: String,
    val nickname: String = "",
    val bio: String = "",
    val avatarUri: String? = null
)
```

## Intent 一覧

| Intent           | 説明               | パラメータ       |
| ---------------- | ------------------ | ---------------- |
| NicknameChanged  | ニックネーム変更   | nickname: String |
| BioChanged       | 自己紹介変更       | bio: String      |
| AvatarSelected   | アバター選択       | uri: String?     |
| AvatarRemoved    | アバター削除       | なし             |
| SignUpClicked    | サインアップ実行   | なし             |
| NavigateToSignIn | サインイン画面へ   | なし             |
| RetryClicked     | エラー後のリトライ | なし             |

## Effect 一覧

| Effect           | 説明                   | パラメータ      |
| ---------------- | ---------------------- | --------------- |
| NavigateToPlaza  | Plaza 画面へ遷移       | なし            |
| NavigateToSignIn | サインイン画面へ遷移   | なし            |
| ShowError        | エラーメッセージ表示   | message: String |
| ShowSuccess      | 成功メッセージ表示     | message: String |
| ShowAvatarPicker | アバター選択ダイアログ | なし            |

## Reducer の役割

Reducer は純粋関数として状態変更ロジックを集約します：

```kotlin
object SignUpReducer {
    fun reduceNicknameChanged(
        currentState: SignUpUiState,
        nickname: String
    ): SignUpUiState {
        // バリデーション
        // 新しい状態を生成
        return SignUpUiState.Editing(...)
    }
}
```

### 主な特徴

- 純粋関数（副作用なし）
- テストが容易
- 状態変更ロジックが一箇所に集約
- 早期リターンパターンを使用

## バリデーション

### ニックネーム

- 必須
- 2 文字以上 20 文字以内
- リアルタイムバリデーション

### 自己紹介

- 任意
- 500 文字以内
- リアルタイムバリデーション

## エラーハンドリング

```kotlin
private suspend fun executeSignUp(...) {
    val result = signUpUseCase(...)

    // 早期リターン: 失敗時
    if (result.isFailure) {
        val error = result.exceptionOrNull()
        _uiState.value = SignUpReducer.reduceSignUpError(_uiState.value, error)
        sendEffect(SignUpEffect.ShowError(error.message))
        return
    }

    // 成功時
    val user = result.getOrNull()
    _uiState.value = SignUpReducer.reduceSignUpSuccess(_uiState.value, user)
    sendEffect(SignUpEffect.NavigateToPlaza)
}
```

## 次のステップ

### 今後の実装が必要な機能

1. アバター画像選択機能

   - 画像ピッカーの実装
   - 画像圧縮処理
   - ByteArray 変換

2. 他の画面の ViewModel 実装

   - SignInViewModel
   - TimelineViewModel
   - PostViewModel
   - ProfileViewModel
   - SearchViewModel
   - SettingsViewModel

3. テストの実装
   - SignUpViewModel の Unit テスト
   - SignUpReducer の Unit テスト
   - SignUpScreen の UI テスト

## トラブルシューティング

### Koin の依存性注入エラー

```kotlin
// MainApplication.ktでKoinを初期化
startKoin {
    androidContext(this@MainApplication)
    modules(appModules)
}
```

### ViewModel が取得できない

```kotlin
// build.gradle.ktsに依存関係を追加
implementation(libs.koin.androidx.compose)
```

## 参考資料

- `overview/AGENTS.md` - アーキテクチャ設計原則
- `overview/PROJECT.md` - プロジェクト仕様書
- Android 公式ドキュメント - ViewModel
- Koin 公式ドキュメント
