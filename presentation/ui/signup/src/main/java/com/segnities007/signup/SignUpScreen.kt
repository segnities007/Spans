package com.segnities007.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.common.route.AuthNavRoute
import com.segnities007.signup.mvi.SignUpEffect
import com.segnities007.signup.mvi.SignUpIntent
import com.segnities007.signup.mvi.SignUpUiState
import com.segnities007.signup.mvi.SignUpViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * サインアップ画面
 * 
 * 新規ユーザー登録のための画面です。
 *
 * @param onAuthNavigate 認証フロー内のナビゲーションコールバック
 * @param onAuthSuccess 認証成功時のコールバック
 * @param viewModel SignUpViewModel（Koinで注入）
 */
@Composable
fun SignUpScreen(
    onAuthNavigate: (AuthNavRoute) -> Unit = {},
    onAuthSuccess: () -> Unit = {},
    viewModel: SignUpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect処理
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.NavigateToPlaza -> onAuthSuccess()
                is SignUpEffect.NavigateToSignIn -> onAuthNavigate(AuthNavRoute.SignIn)
                is SignUpEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is SignUpEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is SignUpEffect.ShowAvatarPicker -> {
                    // TODO: アバター選択ダイアログ実装
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        SignUpContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SignUpContent(
    uiState: SignUpUiState,
    onIntent: (SignUpIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is SignUpUiState.Initial -> {
            // 初期状態（通常は即座にEditingに移行）
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SignUpUiState.Editing -> {
            SignUpForm(
                state = uiState,
                onIntent = onIntent,
                modifier = modifier
            )
        }

        is SignUpUiState.Loading -> {
            LoadingContent(modifier = modifier)
        }

        is SignUpUiState.Success -> {
            SuccessContent(
                nickname = uiState.user.nickname,
                modifier = modifier
            )
        }

        is SignUpUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = { onIntent(SignUpIntent.RetryClicked) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun SignUpForm(
    state: SignUpUiState.Editing,
    onIntent: (SignUpIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "アカウント作成",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ニックネーム入力
        OutlinedTextField(
            value = state.nickname,
            onValueChange = { onIntent(SignUpIntent.NicknameChanged(it)) },
            label = { Text("ニックネーム") },
            isError = state.nicknameError != null,
            supportingText = state.nicknameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 自己紹介入力
        OutlinedTextField(
            value = state.bio,
            onValueChange = { onIntent(SignUpIntent.BioChanged(it)) },
            label = { Text("自己紹介（任意）") },
            isError = state.bioError != null,
            supportingText = state.bioError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: アバター選択ボタン
        // Button(
        //     onClick = { viewModel.showAvatarPicker() },
        //     modifier = Modifier.fillMaxWidth()
        // ) {
        //     Text("アバター画像を選択")
        // }

        Spacer(modifier = Modifier.height(32.dp))

        // サインアップボタン
        Button(
            onClick = { onIntent(SignUpIntent.SignUpClicked) },
            enabled = state.isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登録する")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // サインインへのリンク
        TextButton(
            onClick = { onIntent(SignUpIntent.NavigateToSignIn) }
        ) {
            Text("既にアカウントをお持ちの方はこちら")
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("アカウントを作成中...")
        }
    }
}

@Composable
private fun SuccessContent(
    nickname: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ようこそ、${nickname}さん！",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("アカウントが作成されました")
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "エラーが発生しました",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text("再試行")
            }
        }
    }
}
