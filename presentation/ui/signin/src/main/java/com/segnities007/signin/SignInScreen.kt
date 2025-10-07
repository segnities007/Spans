package com.segnities007.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.Auth

/**
 * サインイン画面
 * 
 * ユーザーがログインするための画面です。
 *
 * @param onAuthNavigate 認証フロー内のナビゲーションコールバック
 * @param onAuthSuccess 認証成功時のコールバック
 */
@Composable
fun SignInScreen(
    onAuthNavigate: (Auth) -> Unit = {},
    onAuthSuccess: () -> Unit = {},
) {
    SignInContent(
        onAuthNavigate = onAuthNavigate,
        onAuthSuccess = onAuthSuccess
    )
}

@Composable
private fun SignInContent(
    onAuthNavigate: (Auth) -> Unit,
    onAuthSuccess: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("SignIn Screen")
        // TODO: 実装する
        // - メールアドレス入力フィールド
        // - パスワード入力フィールド
        // - サインインボタン (成功時: onAuthSuccess())
        // - サインアップリンク (クリック時: onAuthNavigate(Auth.SignUp))
    }
}
