package com.segnities007.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.route.HubNavRoute

/**
 * タイムライン画面
 * 
 * すれ違ったユーザーの投稿を時系列で表示します。
 *
 * @param onHubNavigate Hubフロー内のナビゲーションコールバック
 */
@Composable
fun TimelineScreen(
    onHubNavigate: (HubNavRoute) -> Unit = {},
) {
    TimelineContent(
        onHubNavigate = onHubNavigate
    )
}

@Composable
private fun TimelineContent(
    onHubNavigate: (HubNavRoute) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Timeline Screen")
        // TODO: 実装する
        // - 投稿一覧 (時系列順)
        // - 各投稿アイテム
    // - 戻るボタン: onHubNavigate(HubNavRoute.Plaza)
    }
}
