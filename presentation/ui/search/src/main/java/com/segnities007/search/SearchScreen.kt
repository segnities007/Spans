package com.segnities007.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.common.Hub

/**
 * 検索画面
 * 
 * ユーザーや投稿を検索します。
 *
 * @param onHubNavigate Hubフロー内のナビゲーションコールバック
 */
@Composable
fun SearchScreen(
    onHubNavigate: (Hub) -> Unit = {},
) {
    SearchContent(
        onHubNavigate = onHubNavigate
    )
}

@Composable
private fun SearchContent(
    onHubNavigate: (Hub) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Search Screen")
        // TODO: 実装する
        // - 検索バー
        // - 検索結果一覧 (ユーザー、投稿)
        // - 戻るボタン: onHubNavigate(Hub.Plaza)
    }
}
