package com.segnities007.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.Auth

/**
 * サインアップ画面
 * 
 * 新規ユーザー登録のための画面です。
 *
 * @param onAuthNavigate 認証フロー内のナビゲーションコールバック
 * @param onAuthSuccess 認証成功時のコールバック
 */
@Composable
fun SignUpScreen(
    onAuthNavigate: (Auth) -> Unit = {},
    onAuthSuccess: () -> Unit = {},
) {
    SignUpContent(
        onAuthNavigate = onAuthNavigate,
        onAuthSuccess = onAuthSuccess
    )
}

@Composable
private fun SignUpContent(
    onAuthNavigate: (Auth) -> Unit,
    onAuthSuccess: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("SignUp Screen")
        // TODO: 実装する
        // - ユーザー名入力フィールド
        // - メールアドレス入力フィールド
        // - パスワード入力フィールド
        // - サインアップボタン (成功時: onAuthSuccess())
        // - サインインリンク (クリック時: onAuthNavigate(Auth.SignIn))
    }
}
