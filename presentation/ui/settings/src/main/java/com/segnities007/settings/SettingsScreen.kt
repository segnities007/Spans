package com.segnities007.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.navigation.hub.HubNavRoute

/**
 * 設定画面
 * 
 * アプリの設定を管理する画面です。
 *
 * @param onHubNavigate Hubフロー内のナビゲーションコールバック
 * @param onLogout ログアウト時のコールバック
 */
@Composable
fun SettingsScreen(
    onHubNavigate: (HubNavRoute) -> Unit = {},
    onLogout: () -> Unit = {},
) {
    SettingsContent(
        onHubNavigate = onHubNavigate,
        onLogout = onLogout
    )
}

@Composable
private fun SettingsContent(
    onHubNavigate: (HubNavRoute) -> Unit,
    onLogout: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen")
        // TODO: 実装する
        // - アカウント設定
        // - プライバシー設定
        // - 通知設定
        // - ログアウトボタン: onLogout()
        // - 戻るボタン: onHubNavigate(HubNavRoute.Plaza)
    }
}
