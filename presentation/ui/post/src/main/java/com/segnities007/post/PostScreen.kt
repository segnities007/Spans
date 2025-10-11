package com.segnities007.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.route.HubNavRoute

/**
 * 投稿画面 (Plaza)
 * 
 * すれ違ったユーザーの投稿を表示し、新しい投稿を作成する画面です。
 *
 * @param onHubNavigate Hubフロー内のナビゲーションコールバック
 */
@Composable
fun PostScreen(
    onHubNavigate: (HubNavRoute) -> Unit = {},
) {
    PostContent(
        onHubNavigate = onHubNavigate
    )
}

@Composable
private fun PostContent(
    onHubNavigate: (HubNavRoute) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Post Screen")
        // TODO: 実装する
        // - すれ違ったユーザーの投稿一覧
        // - 投稿作成フォーム
        // - ボトムナビゲーションバー
    //   - Timeline: onHubNavigate(HubNavRoute.Timeline)
    //   - Search: onHubNavigate(HubNavRoute.Search)
    //   - Profile: onHubNavigate(HubNavRoute.Profile)
    //   - Settings: onHubNavigate(HubNavRoute.Settings)
    }
}