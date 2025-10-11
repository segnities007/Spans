package com.segnities007.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.route.HubNavRoute

/**
 * プロフィール画面
 * 
 * ユーザーのプロフィール情報を表示・編集する画面です。
 *
 * @param onHubNavigate Hubフロー内のナビゲーションコールバック
 */
@Composable
fun ProfileScreen(
    onHubNavigate: (HubNavRoute) -> Unit = {},
) {
    ProfileContent(
        onHubNavigate = onHubNavigate
    )
}

@Composable
private fun ProfileContent(
    onHubNavigate: (HubNavRoute) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile Screen")
        // TODO: 実装する
        // - プロフィール画像
        // - ユーザー名、バイオ、その他情報
        // - 編集ボタン
    // - 設定ボタン: onHubNavigate(HubNavRoute.Settings)
    // - 戻るボタン: onHubNavigate(HubNavRoute.Plaza)
    }
}
